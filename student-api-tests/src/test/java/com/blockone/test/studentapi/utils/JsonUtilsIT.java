package com.blockone.test.studentapi.utils;

import com.blockone.test.studentapi.mdl.Student;
import com.cfg.SpringConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfig.class)
public class JsonUtilsIT {

    @Autowired
    private JsonUtils jsonUtils;

    @Test
    public void testGetJsonFromPojo() {
        Student student = Student.builder()
                .id(1L)
                .firstName("Mahesh")
                .lastName("G")
                .clazz("1 A")
                .nationality("IND")
                .build();

        String jsonFromPojo = jsonUtils.getJsonStringFromPojo(student);
        Assert.assertNotNull(jsonFromPojo);
        Assert.assertTrue(jsonFromPojo.contains("\"id\" : 1"));
        Assert.assertTrue(jsonFromPojo.contains("\"nationality\" : \"IND\""));
    }

    @Test
    public void testGetPojoFromJsonFile() {
        String filename = "target/test-classes/student_sample.json";
        Student student = jsonUtils.getPojoFromJsonFile(new File(filename),
                Student.class);

        Assert.assertNotNull(student);
        Assert.assertEquals(Long.valueOf("1"), student.getId());
        Assert.assertEquals("Mahesh", student.getFirstName());

    }


}
