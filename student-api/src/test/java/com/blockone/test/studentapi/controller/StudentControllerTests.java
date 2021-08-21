package com.blockone.test.studentapi.controller;

import com.blockone.test.studentapi.exception.ResourceNotFoundException;
import com.blockone.test.studentapi.model.Student;
import com.blockone.test.studentapi.repository.StudentRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(StudentController.class)
class StudentControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepo repo;

    @Autowired
    ObjectMapper mapper;

    Student student1 = new Student(1L, "John", "Loh", "3 A", "Singapore");
    Student student2 = new Student(2L, "Mahesh", "G", "1 C", "India");
    Student student3 = new Student(3L, "Rob", "Rey", "1 C", "UK");

    @Test
    void testFetchStudentsById_success() throws Exception {
        Mockito.when(repo.findById(student1.getId())).thenReturn(java.util.Optional.of(student1));

        MvcResult mvcResult = mockMvc.perform(get("/fetchStudents?id=1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        assertEquals(student1.getId(), ((Number) JsonPath.read(response, "$.id")).longValue());
        assertEquals(student1.getFirstName(), JsonPath.read(response, "$.firstName"));
        assertEquals(student1.getLastName(), JsonPath.read(response, "$.lastName"));
        assertEquals(student1.getClazz(), JsonPath.read(response, "$.clazz"));
        assertEquals(student1.getNationality(), JsonPath.read(response, "$.nationality"));
    }

    @Test
    void testFetchStudentsById_resourceNotFound() throws Exception {
        mockMvc.perform(get("/fetchStudents?id=99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("Student not found with id : '99'", result.getResolvedException().getMessage()));
    }

    @Test
    void testFetchStudentsByClazz_success() throws Exception {
        List<Student> mockResult = Arrays.asList(student2, student3);
        Mockito.when(repo.getStudentsByClazz(student2.getClazz())).thenReturn(mockResult);

        MvcResult mvcResult = mockMvc.perform(get("/fetchStudents?clazz=1 C")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        assertEquals(student2.getClazz(), JsonPath.read(response, "$[0].clazz"));
        assertEquals(student3.getClazz(), JsonPath.read(response, "$[1].clazz"));
    }

    @Test
    void testFetchStudentsByClazz_noContentFound() throws Exception {
        mockMvc.perform(get("/fetchStudents?clazz=99 C")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }


    @Test
    void testAddStudent_success() throws Exception {
        Mockito.when(repo.findById(student1.getId()))
                .thenReturn(Optional.empty());

        Mockito.when(repo.save(student1)).thenReturn(student1);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/addStudent")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(student1));

        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated());

        Mockito.verify(repo, Mockito.times(1)).save(student1);

    }

    @Test
    void testAddStudent_idAlreadyAvailable() throws Exception {
        Mockito.when(repo.findById(student1.getId()))
                .thenReturn(java.util.Optional.of(student1));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/addStudent")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(student1));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());

        Mockito.verify(repo, Mockito.times(0)).save(student1);
    }

    @Test
    void testAddStudent_nullId() throws Exception {
        Student nullStudent = new Student(null, "John", "Loh", "3 A", "Singapore");

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/addStudent")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(nullStudent));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest());

        Mockito.verify(repo, Mockito.times(0)).save(student1);
    }

    @Test
    void testUpdateStudent_success() throws Exception {
        Mockito.when(repo.findById(student1.getId()))
                .thenReturn(java.util.Optional.of(student1));

        //trying to update only clazz, remaining details unchanged
        Student updated = new Student(student1.getId(), null, null,
                "3 B", null);

        student1.setClazz("3 B");
        Mockito.when(repo.save(student1)).thenReturn(student1);

        MockHttpServletRequestBuilder mockRequest = put("/updateStudent")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(updated));

        MvcResult mvcResult = mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        assertEquals(student1.getId(), ((Number) JsonPath.read(response, "$.id")).longValue());
        assertEquals(student1.getFirstName(), JsonPath.read(response, "$.firstName"));
        assertEquals(student1.getLastName(), JsonPath.read(response, "$.lastName"));
        assertEquals(student1.getClazz(), JsonPath.read(response, "$.clazz"));
        assertEquals(student1.getNationality(), JsonPath.read(response, "$.nationality"));
    }

    @Test
    void testUpdateStudent_resourceNotFound() throws Exception {
        Mockito.when(repo.findById(student1.getId()))
                .thenReturn(null);

        Student updated = new Student(99L, null, null,
                "3 B", null);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/updateStudent")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(updated));

        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("Student not found with id : '99'", result.getResolvedException().getMessage()));

    }

    @Test
    void testUpdateStudent_null() throws Exception {
        Student updated = new Student(null, null, null,
                "3 B", null);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/updateStudent")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(updated));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest());
    }


    @Test
    void testDeleteStudent_success() throws Exception {
        Mockito.when(repo.findById(student1.getId()))
                .thenReturn(java.util.Optional.of(student1));

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/deleteStudent?id=1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteStudent_resourceNotFound() throws Exception {
        Mockito.when(repo.findById(student1.getId()))
                .thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/deleteStudent?id=1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("Student not found with id : '1'",
                        result.getResolvedException().getMessage()));
    }


}
