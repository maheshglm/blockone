package com.blockone.test.studentapi.svc;


import com.blockone.test.studentapi.CustomException;
import com.cfg.SpringConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfig.class)
public class JdbcSvcTest {

    private static final String CONNECTION_NAME = "studentdb";
    private static final String JDBC_URL = ".jdbc.url";
    private static final String JDBC_CLASS = ".jdbc.class";
    private static final String JDBC_USER = ".jdbc.user";
    private static final String JDBC_PASS = ".jdbc.pass";
    private static final String JDBC_DESCRIPTION = ".jdbc.description";

    @InjectMocks
    private JdbcSvc jdbcSvc;

    @Mock
    private StateSvc stateSvc;

    @Before
    public void initMocks() {
        when(stateSvc.getStringVar(CONNECTION_NAME + JDBC_URL)).thenReturn("jdbc:h2:mem:");
        when(stateSvc.getStringVar(CONNECTION_NAME + JDBC_CLASS)).thenReturn("org.h2.Driver");
        when(stateSvc.getStringVar(CONNECTION_NAME + JDBC_USER)).thenReturn("sa");
        when(stateSvc.getStringVar(CONNECTION_NAME + JDBC_PASS)).thenReturn("password");
        when(stateSvc.getStringVar(CONNECTION_NAME + JDBC_DESCRIPTION)).thenReturn("H2 Database Connection");
    }

    @Test
    public void testCreateNamedConnection_success() {
        Throwable exceptionThrown = null;
        try {
            jdbcSvc.createNamedConnection(CONNECTION_NAME);
        } catch (Throwable t) {
            exceptionThrown = t;
        }
        Assert.assertNull(exceptionThrown);
    }

    @Test
    public void testCreateNamedConnection_driverClassNotFound() {
        when(stateSvc.getStringVar(CONNECTION_NAME + JDBC_URL)).thenReturn("jdbc:h2:mem:");
        when(stateSvc.getStringVar(CONNECTION_NAME + JDBC_CLASS)).thenReturn("org.h2.RandomWrongDriver");
        when(stateSvc.getStringVar(CONNECTION_NAME + JDBC_USER)).thenReturn("sa");
        when(stateSvc.getStringVar(CONNECTION_NAME + JDBC_PASS)).thenReturn("password");
        when(stateSvc.getStringVar(CONNECTION_NAME + JDBC_DESCRIPTION)).thenReturn("H2 Database Connection");
        Throwable exceptionThrown = null;
        try {
            jdbcSvc.createNamedConnection(CONNECTION_NAME);
        } catch (Throwable t) {
            exceptionThrown = t;
        }
        Assert.assertNotNull(exceptionThrown);
        Assert.assertEquals(CustomException.class, exceptionThrown.getClass());
        Assert.assertEquals("jdbc driver class [org.h2.RandomWrongDriver] not found in classpath; " +
                        "either provide the correct driver class name or provide the driver in the classpath",
                exceptionThrown.getMessage());
    }

    @Test
    public void testCreateNamedConnection_failedToCreateConnection() {
        when(stateSvc.getStringVar(CONNECTION_NAME + JDBC_URL)).thenReturn("jdbc:h2:3mem:");
        when(stateSvc.getStringVar(CONNECTION_NAME + JDBC_CLASS)).thenReturn("org.h2.Driver");
        when(stateSvc.getStringVar(CONNECTION_NAME + JDBC_USER)).thenReturn("sa");
        when(stateSvc.getStringVar(CONNECTION_NAME + JDBC_PASS)).thenReturn("password");
        when(stateSvc.getStringVar(CONNECTION_NAME + JDBC_DESCRIPTION)).thenReturn("H2 Database Connection");

        Throwable exceptionThrown = null;
        try {
            jdbcSvc.createNamedConnection(CONNECTION_NAME);
        } catch (Throwable t) {
            exceptionThrown = t;
        }
        Assert.assertNotNull(exceptionThrown);
        Assert.assertEquals(CustomException.class, exceptionThrown.getClass());
        Assert.assertEquals("failed to create connection", exceptionThrown.getMessage());
    }

    @Test
    public void testExecuteOnNamedConnection_success() {
        Throwable exceptionThrown = null;
        try {
            jdbcSvc.createNamedConnection(CONNECTION_NAME);
            jdbcSvc.executeQueryOnNamedConnection(CONNECTION_NAME, "CREATE TABLE abc (name varchar(20))");
        } catch (Throwable t) {
            t.printStackTrace();
            exceptionThrown = t;
        }
        Assert.assertNull(exceptionThrown);
    }

    @Test
    public void testExecuteQueryOnNamedConnection_connectionNotCreated() {
        Throwable exceptionThrown = null;
        try {
            jdbcSvc.executeQueryOnNamedConnection(CONNECTION_NAME, "CREATE TABLE abc (name varchar(20))");
        } catch (Throwable t) {
            exceptionThrown = t;
        }
        Assert.assertNotNull(exceptionThrown);
        Assert.assertEquals(CustomException.class, exceptionThrown.getClass());
        Assert.assertEquals("named connection [" + CONNECTION_NAME + "] is null, " +
                "probably it has not been created?", exceptionThrown.getMessage());
    }

    @Test
    public void testExecuteSingleRowQueryOnNamedConnection() {
        Throwable exceptionThrown = null;
        try {
            jdbcSvc.createNamedConnection(CONNECTION_NAME);
            jdbcSvc.executeQueryOnNamedConnection(CONNECTION_NAME, "CREATE TABLE abc (name varchar(20))");
            jdbcSvc.executeQueryOnNamedConnection(CONNECTION_NAME, "INSERT INTO abc VALUES ('Mahesh')");
            jdbcSvc.executeSingleRowQueryOnNamedConnection(CONNECTION_NAME, "SELECT * FROM abc WHERE name='Mahesh'");
        } catch (Throwable t) {
            t.printStackTrace();
            exceptionThrown = t;
        }
        Assert.assertNull(exceptionThrown);
    }


}
