package com.blockone.test.studentapi.steps;

import cucumber.api.Scenario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Hooks steps.
 */
@Component
public class HooksSteps {

    private static final String API_DB_PREFIX_PARAM = "student.db";
    private static final String API_BASE_URL = "api.base.url";

    @Autowired
    private DatabaseSteps databaseSteps;

    @Autowired
    private RestApiSteps restApiSteps;

    /**
     * Connect to database.
     */
    public void connectToDatabase() {
        databaseSteps.setDatabaseConnectionToConfig(API_DB_PREFIX_PARAM);
    }

    /**
     * Is application running.
     */
    public void isApplicationRunning() {
        restApiSteps.checkApiRunning(API_BASE_URL);
    }
}
