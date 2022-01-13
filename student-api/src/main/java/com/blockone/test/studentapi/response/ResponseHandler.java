package com.blockone.test.studentapi.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;


public class ResponseHandler {

    private static final String MESSAGE = "message";
    private static final String STATUS = "status";
    private static final String DATA = "data";

    public static ResponseEntity<?> generateResponse(String message, HttpStatus status, Object responseObj) {
        Map<String, Object> map = new HashMap<>();
        map.put(MESSAGE, message);
        map.put(STATUS, status.value());
        map.put(DATA, responseObj);
        return new ResponseEntity<>(map, status);
    }
}
