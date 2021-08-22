package com.blockone.test.studentapi.utils;

import com.cfg.SpringConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfig.class)
public class DateTimeUtilsIT {

    @Autowired
    private DateTimeUtils dateTimeUtils;

    @Test
    public void testGetTimeStamp() {
        final String timeStamp = dateTimeUtils.getTimeStamp("HmsS");
        Assert.assertNotNull(timeStamp);
    }

}
