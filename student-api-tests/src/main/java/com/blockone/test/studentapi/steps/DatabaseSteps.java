package com.blockone.test.studentapi.steps;

import com.blockone.test.studentapi.CustomException;
import com.blockone.test.studentapi.CustomExceptionType;
import com.blockone.test.studentapi.svc.DatabaseSvc;
import com.blockone.test.studentapi.svc.JdbcSvc;
import com.blockone.test.studentapi.svc.StateSvc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Database steps.
 */
@Component
public class DatabaseSteps {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseSteps.class);

    /**
     * The constant COLUMN_VALUE_VERIFICATION_FAILED_EXPECTED_ACTUAL.
     */
    public static final String COLUMN_VALUE_VERIFICATION_FAILED_EXPECTED_ACTUAL = "Column value verification failed, expected [{}], actual [{}]";

    @Autowired
    private DatabaseSvc databaseSvc;

    @Autowired
    private JdbcSvc jdbcSvc;

    @Autowired
    private StateSvc stateSvc;

    /**
     * Sets database connection to config.
     *
     * @param dbConfigPrefix the db config prefix
     */
    public void setDatabaseConnectionToConfig(final String dbConfigPrefix) {
        databaseSvc.setDatabaseConnectionToConfig(dbConfigPrefix);
    }

    /**
     * Execute query.
     *
     * @param sql the sql
     */
    public void executeQuery(final String sql) {
        final String effectiveSql = stateSvc.expandExpression(sql);
        databaseSvc.executeQuery(effectiveSql);
    }

    /**
     * Expect value of column should match.
     *
     * @param columnName    the column name
     * @param expectedValue the expected value
     * @param sqlQuery      the sql query
     */
    public void iExpectValueOfColumnShouldMatch(final String columnName, final String expectedValue, final String sqlQuery) {
        final String sqlQueryToExecute = stateSvc.expandExpression(sqlQuery);
        final String expandExpectedValue = stateSvc.expandExpression(expectedValue);
        LOGGER.debug("Column Value to verify is [{}]: Expected value is [{}]", columnName, expandExpectedValue);
        final String actualValue = databaseSvc.getColumnValue(columnName, sqlQueryToExecute);

        if (!expandExpectedValue.equals(actualValue)) {
            LOGGER.error(COLUMN_VALUE_VERIFICATION_FAILED_EXPECTED_ACTUAL, expandExpectedValue, actualValue);
            throw new CustomException(CustomExceptionType.VERIFICATION_FAILED,
                    COLUMN_VALUE_VERIFICATION_FAILED_EXPECTED_ACTUAL, expandExpectedValue, actualValue);
        }
    }

}
