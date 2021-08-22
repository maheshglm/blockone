package com.blockone.test.studentapi.utils;

import com.cfg.SpringConfig;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfig.class)
public class WorkspaceUtilsIT {

    @Autowired
    private WorkspaceUtils workspaceUtils;

    @After
    public void clear() {
        System.clearProperty("framework.basedir");
    }

    @Test
    public void testGetBaseDir_default() {
        Assert.assertEquals(System.getProperty("user.dir"), workspaceUtils.getBaseDir());
    }

    @Test
    public void testGetBaseDir_overrideWithSystemProp() {
        System.setProperty("framework.basedir", "/test");
        WorkspaceUtils wu = new WorkspaceUtils();
        Assert.assertEquals("/test", wu.getBaseDir());
    }


    @Test
    public void testGetEnvDir() {
        System.setProperty("framework.basedir", "/test");
        WorkspaceUtils wu = new WorkspaceUtils();
        Assert.assertEquals("/test/config", wu.getEnvDir());
    }

    @Test
    public void testgetFeaturesDir() {
        System.setProperty("framework.basedir", "/test");
        WorkspaceUtils wu = new WorkspaceUtils();
        Assert.assertEquals("/test/tests/features", wu.getFeaturesDir());
    }

}
