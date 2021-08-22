package com.blockone.test.studentapi;

import com.blockone.test.studentapi.svc.JdbcSvc;
import com.cfg.SpringConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfig.class)
public class BootstrapRunIT {

    @Test
    public void testLifecycle() {
        Bootstrap.init();
        Bootstrap.getBean(JdbcSvc.class);
        Bootstrap.done();
    }

}
