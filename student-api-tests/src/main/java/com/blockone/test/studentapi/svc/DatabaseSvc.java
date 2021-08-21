package com.blockone.test.studentapi.svc;

import com.blockone.test.studentapi.CustomException;
import com.blockone.test.studentapi.CustomExceptionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Database svc.
 */
@Service
public class DatabaseSvc {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseSvc.class);

    private static final String UNABLE_TO_RETRIEVE_RECORDS_WITH_QUERY = "Unable to retrieve records with Query [{}]";
    private static final String QUERY_DOES_NOT_HAVE_COLUMN = "Query does not have column [{}]";

    @Autowired
    private JdbcSvc jdbcSvc;

    @Autowired
    private StateSvc stateSvc;

    private String currentConfigPrefix = "<undefined>";

    /**
     * Gets current config prefix.
     *
     * @return the current config prefix
     */
    public String getCurrentConfigPrefix() {
        return currentConfigPrefix;
    }

    /**
     * Sets database connection to config.
     *
     * @param dbConfigPrefix the db config prefix
     */
    public void setDatabaseConnectionToConfig(final String dbConfigPrefix) {
        currentConfigPrefix = dbConfigPrefix;
        jdbcSvc.createNamedConnection(currentConfigPrefix);
    }

    /**
     * Execute query.
     *
     * @param sqlQuery the sql query
     */
    public void executeQuery(final String sqlQuery) {
        LOGGER.debug("Executing Query: [{}]", sqlQuery);
        jdbcSvc.executeQueryOnNamedConnection(getCurrentConfigPrefix(), sqlQuery);
    }

    /**
     * Gets column value.
     *
     * @param columnName the column name
     * @param sqlQuery   the sql query
     * @return the column value
     */
    public String getColumnValue(final String columnName, final String sqlQuery) {
        if (!sqlQuery.contains(columnName)) {
            LOGGER.error(QUERY_DOES_NOT_HAVE_COLUMN, columnName);
            throw new CustomException(CustomExceptionType.INVALID_INVOCATION_PARAMS, QUERY_DOES_NOT_HAVE_COLUMN, columnName);
        }

        Map<String, String> queryResultMap = jdbcSvc.executeSingleRowQueryOnNamedConnection(currentConfigPrefix, sqlQuery);
        if (queryResultMap.size() <= 0) {
            LOGGER.error(UNABLE_TO_RETRIEVE_RECORDS_WITH_QUERY, sqlQuery);
            throw new CustomException(CustomExceptionType.PROCESSING_FAILED, UNABLE_TO_RETRIEVE_RECORDS_WITH_QUERY, sqlQuery);
        }
        return queryResultMap.get(columnName);
    }


}
