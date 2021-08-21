package com.blockone.test.studentapi;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CucumberRunner extends BaseRunner {

    private static final String TAGS = "@student-api";

    @Test
    public void testCucumber() {
        List<String> allArgsList = new ArrayList<>();
        allArgsList.addAll(Arrays.asList("--tags", TAGS));
        Collections.addAll(allArgsList, cucumberOptions);
        cucumber.api.cli.Main.run(allArgsList.toArray(new String[0]), Thread.currentThread().getContextClassLoader());
    }
}
