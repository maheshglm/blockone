package com.blockone.test.studentapi.svc;

import com.blockone.test.studentapi.utils.WorkspaceUtils;
import com.cfg.SpringConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfig.class)
public class StatePropertiesSvcIT {

    @Autowired
    private StatePropertiesSvc statePropertiesSvc;

    @Autowired
    private WorkspaceUtils workspaceUtils;

    @Test
    public void testPopulateGlobalPropsMap() {
        workspaceUtils.setBaseDir("target/test-classes/test-files");
        statePropertiesSvc.populateGlobalPropsMap();
        Assert.assertEquals("value2",
                statePropertiesSvc.getGlobalPropsMap("var2"));
    }

}
