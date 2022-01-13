package com.blockone.test.studentapi.utils;

import com.blockone.test.studentapi.CustomException;
import com.blockone.test.studentapi.CustomExceptionType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class JsonUtils {

    ObjectMapper mapper = new ObjectMapper();

    public String getJsonStringFromPojo(Object o) {
        try {
            return mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new CustomException(CustomExceptionType.PROCESSING_FAILED, "", e.getMessage());
        }
    }

    public <T> T getPojoFromJsonFile(File jsonFile, Class<T> clazz) {
        try {
            return mapper.readValue(jsonFile, clazz);
        } catch (IOException e) {
            throw new CustomException(CustomExceptionType.PROCESSING_FAILED, "", e.getMessage());
        }
    }

}
