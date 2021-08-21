package com.blockone.test.studentapi;

import static org.assertj.core.api.Assertions.assertThat;

import com.blockone.test.studentapi.controller.StudentController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StudentApiApplicationTests {

    @Autowired
    private StudentController controller;

    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
    }

}
