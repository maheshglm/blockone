package com.blockone.test.studentapi.steps;

import com.blockone.test.studentapi.svc.StateSvc;
import com.blockone.test.studentapi.utils.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Config steps.
 */
@Component
public class ConfigSteps {

    @Autowired
    private StateSvc stateSvc;

    @Autowired
    private DateTimeUtils dateTimeUtils;

    /**
     * Assign value to var.
     *
     * @param value    the value
     * @param variable the variable
     */
    public void assignValueToVar(final String value, final String variable) {
        final String expandValue = stateSvc.expandExpression(value);
        stateSvc.setStringVar(variable, expandValue);
    }

    /**
     * Generate random integer string.
     *
     * @return the string
     */
    public String generateRandomInteger() {
        final String random = dateTimeUtils.getTimeStamp("HmsS");
        //This is to truncate leading zeros.
        return random.replaceFirst("^0+(?!$)", "");
    }


}
