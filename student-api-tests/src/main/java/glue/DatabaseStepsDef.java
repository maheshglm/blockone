package glue;

import com.blockone.test.studentapi.Bootstrap;
import com.blockone.test.studentapi.steps.DatabaseSteps;
import cucumber.api.java.en.Then;

/**
 * Database steps def.
 */
public class DatabaseStepsDef {

    private DatabaseSteps dbSteps = (DatabaseSteps) Bootstrap.getBean(DatabaseSteps.class);

    /**
     * Verify column value.
     *
     * @param columnName     the column name
     * @param expectedResult the expected result
     * @param docString      the doc string
     */
    @Then("I expect value of column {string} in the below query equals to {string}")
    public void verifyColumnValue(String columnName, String expectedResult, String docString) {
        dbSteps.iExpectValueOfColumnShouldMatch(columnName, expectedResult, docString);
    }

    /**
     * Execute query.
     *
     * @param information the information
     * @param docString   the doc string
     */
    @Then("I execute below query to {string}")
    public void executeQuery(final String information, final String docString) {
        dbSteps.executeQuery(docString);
    }

}
