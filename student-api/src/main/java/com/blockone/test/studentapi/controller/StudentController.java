package com.blockone.test.studentapi.controller;

import com.blockone.test.studentapi.exception.ResourceNotFoundException;
import com.blockone.test.studentapi.model.Student;
import com.blockone.test.studentapi.repository.StudentRepo;
import com.blockone.test.studentapi.response.ResponseHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Student controller.
 * This class contains CRUD operations mapping
 */
@RestController
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = {"student"})
public class StudentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentController.class);

    private static final String STUDENT = "Student";
    private static final String ID = "id";

    @Autowired
    private StudentRepo repo;

    /**
     * Gets student by id.
     *
     * @param id the id
     * @return the student by id
     */
    @ApiOperation(value = "Fetch students by unit id")
    @GetMapping(value = "/fetchStudents", params = StudentController.ID)
    public ResponseEntity<Student> getStudentById(@RequestParam Long id) {
        LOGGER.debug("Fetching student details with with id [{}]", id);
        Student student = isStudentAvailable(id);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    /**
     * Gets student by clazz.
     *
     * @param clazz the clazz
     * @return the student by clazz
     */
    @ApiOperation(value = "Fetch students by clazz")
    @GetMapping(value = "/fetchStudents", params = "clazz")
    public ResponseEntity<List<Student>> getStudentByClazz(@RequestParam String clazz) {
        LOGGER.debug("Fetching student details with with clazz [{}]", clazz);
        List<Student> students = repo.getStudentsByClazz(clazz);
        if (students.isEmpty()) {
            LOGGER.debug("No students are available in clazz [{}]", clazz);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    /**
     * Add student response entity.
     *
     * @param student the student
     * @return the response entity
     */
    @ApiOperation(value = "Create a student resource",
            notes = "If student with given id exists, it writes a warning message in the console")
    @PostMapping(value = "/addStudent", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addStudent(@RequestBody Student student) {
        if (student == null) {
            LOGGER.error("Student details cannot be null");
            return ResponseHandler.generateResponse("Student details cannot be null",
                    HttpStatus.BAD_REQUEST,
                    null);
        }

        Optional<Student> studentExist = repo.findById(student.getId());
        if (studentExist.isEmpty()) {
            try {
                Student result = repo.save(student);
                LOGGER.debug("Student with id [{}] added successfully", student.getId());
                return ResponseHandler.generateResponse(
                        "Student with id=" + student.getId() + " added successfully",
                        HttpStatus.CREATED, result);
            } catch (Exception e) {
                LOGGER.error("{}", e.getMessage());
                return ResponseHandler.generateResponse(
                        e.getMessage(),
                        HttpStatus.BAD_REQUEST, null);
            }
        } else {
            LOGGER.warn("Student with id [{}] already available", student.getId());
            return ResponseHandler.generateResponse(
                    "Student with id=" + student.getId() + " already available",
                    HttpStatus.OK, null);
        }
    }

    /**
     * Update student response entity.
     *
     * @param studentDetails the student details
     * @return the response entity
     */
    @ApiOperation(value = "Updates student details", notes = "Unique id to be used to update student details")
    @PutMapping("/updateStudent")
    public ResponseEntity<?> updateStudent(@RequestBody Student studentDetails) {

        if (studentDetails == null || studentDetails.getId() == null) {
            LOGGER.error("Student details or id cannot be null");
            return ResponseHandler.generateResponse("Student details or id cannot be null",
                    HttpStatus.BAD_REQUEST,
                    null);
        }
        LOGGER.debug("Updating student with id [{}]", studentDetails.getId());

        Student student = isStudentAvailable(studentDetails.getId());
        student.setClazz(studentDetails.getClazz() == null ? student.getClazz() : studentDetails.getClazz());
        student.setFirstName(studentDetails.getFirstName() == null ? student.getFirstName() : studentDetails.getFirstName());
        student.setLastName(studentDetails.getLastName() == null ? student.getLastName() : studentDetails.getLastName());
        student.setNationality(studentDetails.getNationality() == null ? student.getNationality() : studentDetails.getNationality());

        Student result = repo.save(student);
        LOGGER.debug("Student with id={} updated successfully", studentDetails.getId());
        return ResponseHandler.generateResponse("Student with id=" + studentDetails.getId() + " updated successfully",
                HttpStatus.OK,
                result);
    }

    /**
     * Delete student response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @ApiOperation(value = "Delete student details", notes = "Unique id to be used to delete the student details")
    @DeleteMapping("/deleteStudent")
    public ResponseEntity<?> deleteStudent(@RequestParam Long id) {
        LOGGER.debug("Deleting student with id [{}]", id);
        Student student = isStudentAvailable(id);
        repo.deleteById(id);
        return ResponseHandler.generateResponse(
                "Student with id=" + id + " deleted successfully",
                HttpStatus.OK,
                student);
    }

    private Student isStudentAvailable(@RequestParam Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(STUDENT, ID, id));
    }
}
