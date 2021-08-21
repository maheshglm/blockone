package com.blockone.test.studentapi;

import com.cfg.CucumberConfig;
import com.cfg.SpringConfig;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class BaseRunner {

    static String[] cucumberOptions;

    @BeforeClass
    public static void beforeClass() {
        cucumberOptions = CucumberConfig.getCucumberOptions();
        Bootstrap.setConfigClass(SpringConfig.class);
        Bootstrap.init();
    }

    @AfterClass
    public static void afterClass() {
        Bootstrap.done();
    }
}
