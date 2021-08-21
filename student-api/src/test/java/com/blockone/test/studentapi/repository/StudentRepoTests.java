package com.blockone.test.studentapi.repository;

import com.blockone.test.studentapi.model.Student;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ActiveProfiles("test")
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentRepoTests {

    @Resource
    private StudentRepo repo;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testAddingStudent_success() {
        Student student = new Student(1L, "Mahesh", "G", "1 A", "India");
        repo.save(student);
        Assert.assertEquals(student.getFirstName(), repo.getById(student.getId()).getFirstName());
        Assert.assertEquals(student.getClazz(), repo.getById(student.getId()).getClazz());
        Assert.assertEquals(student.getLastName(), repo.getById(student.getId()).getLastName());
        Assert.assertEquals(student.getId(), repo.getById(student.getId()).getId());
        Assert.assertEquals(student.getNationality(), repo.getById(student.getId()).getNationality());
    }

    @Test
    public void testAddingStudent_nullStudentId() {
        thrown.expect(JpaSystemException.class);
        thrown.expectMessage("ids for this class must be manually assigned before calling save()");
        Student student = new Student(null, "Mahesh", "G", "1 A", "India");
        repo.save(student);
    }

    @Test
    public void testFetchStudentById_success() {
        Student student = new Student(1L, "Mahesh", "G", "1 A", "India");
        repo.save(student);

        Optional<Student> fetchStudent = repo.findById(student.getId());

        Assert.assertEquals(student.getFirstName(), fetchStudent.get().getFirstName());
        Assert.assertEquals(student.getClazz(), fetchStudent.get().getClazz());
        Assert.assertEquals(student.getLastName(), fetchStudent.get().getLastName());
        Assert.assertEquals(student.getId(), fetchStudent.get().getId());
        Assert.assertEquals(student.getNationality(), fetchStudent.get().getNationality());
    }

    @Test
    public void testFetchStudentByClazz_success() {
        Student student1 = new Student(1L, "Mahesh", "G", "1 A", "India");
        Student student2 = new Student(2L, "Mahesh", "G", "2 A", "India");
        Student student3 = new Student(3L, "Mahesh", "G", "1 A", "India");

        repo.save(student1);
        repo.save(student2);
        repo.save(student3);

        List<Student> studentsByClazz = repo.getStudentsByClazz("1 A");

        Assert.assertEquals(2, studentsByClazz.size());

        Assert.assertEquals(student1.getClazz(), studentsByClazz.get(0).getClazz());
        Assert.assertEquals(student3.getClazz(), studentsByClazz.get(1).getClazz());
    }

    @Test
    public void testFetchStudentByClazz_emptyResources() {
        Student student1 = new Student(1L, "Mahesh", "G", "1 A", "India");
        repo.save(student1);

        List<Student> studentsByClazz = repo.getStudentsByClazz("2 A");
        Assert.assertEquals(0, studentsByClazz.size());
    }

    @Test
    public void testDeleteStudent_success() {
        Student student1 = new Student(1L, "Mahesh", "G", "1 A", "India");
        repo.save(student1);

        repo.deleteById(student1.getId());

        List<Student> studentsByClazz = repo.getStudentsByClazz("1 A");
        Assert.assertEquals(0, studentsByClazz.size());
    }

    @Test
    public void testDeleteStudent_resourceNotFound() {
        thrown.expect(EmptyResultDataAccessException.class);
        thrown.expectMessage("No class com.blockone.test.studentapi.model.Student entity with id 1 exists!");
        repo.deleteById(1L);

        List<Student> studentsByClazz = repo.getStudentsByClazz("1 A");
        Assert.assertEquals(0, studentsByClazz.size());
    }


}
