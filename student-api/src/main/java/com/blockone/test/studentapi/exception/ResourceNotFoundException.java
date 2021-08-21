package com.blockone.test.studentapi.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class to capture Custom Exception ResourceNotFoung.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceNotFoundException.class);

    /**
     * Instantiates a new Resource not found exception.
     *
     * @param resourceName the resource name
     * @param fieldName    the field name
     * @param fieldValue   the field value
     */
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
        LOGGER.error("{} not found with {} : '{}'", resourceName, fieldName, fieldValue);
    }

}
