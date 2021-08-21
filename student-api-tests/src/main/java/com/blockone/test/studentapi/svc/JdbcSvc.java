package com.blockone.test.studentapi.svc;

import com.blockone.test.studentapi.CustomException;
import com.blockone.test.studentapi.CustomExceptionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JdbcSvc {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcSvc.class);

    private Map<String, Connection> namedConnections = new HashMap<>();

    private static final String JDBC_DRIVER_CLASS_NOT_FOUND = "jdbc driver class [{}] not found in classpath; either provide the correct driver class name or provide the driver in the classpath";
    private static final String NAMED_CONNECTION_IS_NULL_PROBABLY_IT_HAS_NOT_BEEN_CREATED = "named connection [{}] is null, probably it has not been created?";
    private static final String FAILED_TO_CREATE_CONNECTION = "failed to create connection";
    private static final String FAILED_TO_EXECUTE_QUERY_ON_NAMED_CONNECTION = "failed to execute query [{}] on named connection [{}]";

    @Autowired
    private StateSvc stateSvc;

    private boolean isDBConnectionEstablished(final String connectionName) {
        try {
            Connection conn = namedConnections.get(connectionName);
            if (conn != null && conn.isValid(5)) {
                return true;
            }
        } catch (SQLException e) {
            //ignore
        }
        return false;
    }

    public void createNamedConnection(String connectionName) {
        if (isDBConnectionEstablished(connectionName)) {
            LOGGER.debug("Connection for [{}] is already established...", connectionName);
            return;
        }
        LOGGER.debug("creating Named Connection [{}]", connectionName);

        String jdbcUrl = stateSvc.getStringVar(connectionName + ".jdbc.url");
        String jdbcClass = stateSvc.getStringVar(connectionName + ".jdbc.class");
        String jdbcUser = stateSvc.getStringVar(connectionName + ".jdbc.user");
        String jdbcPass = stateSvc.getStringVar(connectionName + ".jdbc.pass");
        String jdbcDescription = stateSvc.getStringVar(connectionName + ".jdbc.description");

        LOGGER.debug("  jdbcUrl: [{}]", jdbcUrl);
        LOGGER.debug("  jdbcClass: [{}]", jdbcClass);
        LOGGER.debug("  jdbcUser: [{}]", jdbcUser);
        LOGGER.debug("  jdbcPass: ********", jdbcPass);
        LOGGER.debug("  jdbcDescription: [{}]", jdbcDescription);

        try {
            Class.forName(jdbcClass);
        } catch (ClassNotFoundException e) {
            LOGGER.error(JDBC_DRIVER_CLASS_NOT_FOUND, jdbcClass, e);
            throw new CustomException(CustomExceptionType.PROCESSING_FAILED, JDBC_DRIVER_CLASS_NOT_FOUND, jdbcClass);
        }
        try {
            Connection result = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPass);
            namedConnections.put(connectionName, result);
        } catch (SQLException e) {
            LOGGER.error(FAILED_TO_CREATE_CONNECTION, e);
            throw new CustomException(CustomExceptionType.PROCESSING_FAILED, FAILED_TO_CREATE_CONNECTION);
        }
    }

    public boolean executeQueryOnNamedConnection(String connName, String sqlQuery) {
        Connection conn = namedConnections.get(connName);
        if (conn == null) {
            LOGGER.error(NAMED_CONNECTION_IS_NULL_PROBABLY_IT_HAS_NOT_BEEN_CREATED, connName);
            throw new CustomException(CustomExceptionType.PROCESSING_FAILED, NAMED_CONNECTION_IS_NULL_PROBABLY_IT_HAS_NOT_BEEN_CREATED, connName);
        }
        boolean result;
        try (Statement stmt = conn.createStatement()) {
            result = stmt.execute(sqlQuery);
        } catch (SQLException e) {
            LOGGER.error(FAILED_TO_EXECUTE_QUERY_ON_NAMED_CONNECTION, sqlQuery, connName, e);
            throw new CustomException(CustomExceptionType.PROCESSING_FAILED, FAILED_TO_EXECUTE_QUERY_ON_NAMED_CONNECTION, sqlQuery, connName);
        }
        return result;
    }

    public Map<String, String> executeSingleRowQueryOnNamedConnection(String connName, String sqlQuery) {
        Connection conn = namedConnections.get(connName);
        if (conn == null) {
            LOGGER.error(NAMED_CONNECTION_IS_NULL_PROBABLY_IT_HAS_NOT_BEEN_CREATED, connName);
            throw new CustomException(CustomExceptionType.PROCESSING_FAILED, NAMED_CONNECTION_IS_NULL_PROBABLY_IT_HAS_NOT_BEEN_CREATED, connName);
        }
        List<String> columnNames = new ArrayList<>();
        Map<String, String> valueMap = new HashMap<>();

        try (Statement stmt = conn.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(sqlQuery)) {
                while (rs.next()) {
                    ResultSetMetaData rsmd = rs.getMetaData();
                    int columnCount = rsmd.getColumnCount();
                    for (int i = 0; i < columnCount; i++) {
                        columnNames.add(rsmd.getColumnName(i + 1));
                        valueMap.put(columnNames.get(i), rs.getString(i + 1));
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.error(FAILED_TO_EXECUTE_QUERY_ON_NAMED_CONNECTION, sqlQuery, connName, e);
            throw new CustomException(CustomExceptionType.PROCESSING_FAILED, FAILED_TO_EXECUTE_QUERY_ON_NAMED_CONNECTION, sqlQuery, connName);
        }
        return valueMap;
    }
}
