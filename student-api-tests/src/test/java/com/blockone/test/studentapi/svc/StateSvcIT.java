package com.blockone.test.studentapi.svc;

import com.blockone.test.studentapi.CustomException;
import com.blockone.test.studentapi.utils.WorkspaceUtils;
import com.cfg.SpringConfig;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfig.class)
public class StateSvcIT {

    @Autowired
    private StateSvc stateSvc;

    @Autowired
    private WorkspaceUtils workspaceUtils;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testLoadEnvProperties_success() {
        WorkspaceUtils wu = new WorkspaceUtils();
        Throwable exceptionThrown = null;
        try {
            stateSvc.loadEnvProperties("test");
        } catch (Throwable t) {
            exceptionThrown = t;
        }
        Assert.assertNull(exceptionThrown);
    }

    @Test
    public void testLoadEnvProperties_envNotDefined() {
        thrown.expect(CustomException.class);
        thrown.expectMessage("failed when loading named environment [dummy] config from environment properties file");
        stateSvc.loadEnvProperties("dummy");
    }

    @Test
    public void testGetStringVar_getFromEnv() {
        workspaceUtils.setBaseDir("target/test-classes/test-files");
        stateSvc.loadEnvProperties("test");
        Assert.assertEquals("root",
                stateSvc.getStringVar("student.db.jdbc.user"));
    }

    @Test
    public void testGetStringVar_getFromGlobal() {
        workspaceUtils.setBaseDir("target/test-classes/test-files");
        Assert.assertEquals("value1",
                stateSvc.getStringVar("var1"));
    }

    @Test
    public void testGetStringVar_getStringMap() {
        stateSvc.setStringVar("testVar", "testVal");
        Assert.assertEquals("testVal",
                stateSvc.getStringVar("testVar"));
    }

    @Test
    public void testSetStringVar() {
        workspaceUtils.setBaseDir("target/test-classes/test-files");
        stateSvc.loadEnvProperties("test");
        stateSvc.setStringVar("api.base.url", "http://localhost:9090");
        Assert.assertEquals("http://localhost:9090",
                stateSvc.getStringVar("api.base.url"));
    }
}
