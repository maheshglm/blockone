package com.blockone.test.studentapi.svc;


import com.blockone.test.studentapi.CustomException;
import com.blockone.test.studentapi.CustomExceptionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

@Service
public class StatePropertiesSvc {

    private static Logger LOGGER = LoggerFactory.getLogger(StatePropertiesSvc.class);

    @Autowired
    private StateSvc stateSvc;

    private Map<String, String> globalPropsMap = null;

    public synchronized void loadFrameworkProperties() {
        File file = new File(System.getProperty("user.dir") + "/config/framework.properties");
        LOGGER.debug("loading properties from [{}]", file.getAbsolutePath());
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(file.getAbsolutePath()));
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("properties read:[{}]", Objects.toString(props));
            }
            globalPropsMap = new HashMap<>();
            for (String key : props.stringPropertyNames()) {
                globalPropsMap.put(key, props.getProperty(key));
            }
        } catch (IOException e) {
            LOGGER.error("failed while loading properties file [{}]", file.getAbsolutePath(), e);
            throw new CustomException(CustomExceptionType.PROCESSING_FAILED, "failed while loading properties file [{}]", file.getAbsolutePath());
        }
    }

    public void populateGlobalPropsMap() {
        if (globalPropsMap == null) {
            LOGGER.debug("loadproperties(): first time invocation.");
            this.loadFrameworkProperties();
        }
    }

    public String getGlobalPropsMap(String name) {
        String result;
        if (globalPropsMap.containsKey(name)) {
            result = globalPropsMap.get(name);
        } else {
            LOGGER.debug("[{}] not found in GlobalPropsMap", name);
            result = "";
        }
        return result;
    }
}
