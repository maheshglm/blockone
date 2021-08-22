package com.blockone.test.studentapi.utils;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class WorkspaceUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkspaceUtils.class);

    private String baseDir;

    public WorkspaceUtils() {
        baseDir = System.getProperty("user.dir");
        final String userSpecifiedBaseDir = System.getProperty("framework.basedir");
        if (!Strings.isNullOrEmpty(userSpecifiedBaseDir)) {
            baseDir = userSpecifiedBaseDir;
        }
        LOGGER.debug("Basedir [{}]", baseDir);
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    public String getBaseDir() {
        return baseDir;
    }

    public String getEnvDir() {
        return baseDir + "/config";
    }

    public String getFeaturesDir() {
        return baseDir + "/tests/features";
    }


}
