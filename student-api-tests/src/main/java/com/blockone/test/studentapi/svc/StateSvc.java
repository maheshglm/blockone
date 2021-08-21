package com.blockone.test.studentapi.svc;

import com.blockone.test.studentapi.CustomException;
import com.blockone.test.studentapi.CustomExceptionType;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * State svc.
 */
@Service
public class StateSvc {

    public static final String FAILED_WHEN_LOADING_NAMED_ENVIRONMENT_CONFIG_FROM_ENVIRONMENT_PROPERTIES_FILE = "failed when loading named environment [{}] config from environment properties file [{}]";
    @Autowired
    private StatePropertiesSvc statePropertiesSvc;

    private static final Logger LOGGER = LoggerFactory.getLogger(StateSvc.class);

    private Map<String, String> stringMap = new HashMap<>();
    private Map<String, String> envStringMap = new HashMap<>();

    /**
     * Load env properties.
     *
     * @param envName the env name
     */
    public void loadEnvProperties(final String envName) {
        final String expandEnvName = expandExpression(envName);
        final String envPropertiesPath = System.getProperty("user.dir") + "/config/env_" + expandEnvName + ".properties";
        LOGGER.debug("loading environment properties from [{}]", envPropertiesPath);
        envStringMap = new HashMap<>();
        try {
            Properties props = new Properties();
            props.load(new FileInputStream(envPropertiesPath));
            for (String key : props.stringPropertyNames()) {
                LOGGER.debug("  [{}] => [{}]", key, props.getProperty(key));
                envStringMap.put(key, props.getProperty(key));
            }
        } catch (IOException e) {
            LOGGER.error(FAILED_WHEN_LOADING_NAMED_ENVIRONMENT_CONFIG_FROM_ENVIRONMENT_PROPERTIES_FILE, expandEnvName, envPropertiesPath, e);
            throw new CustomException(e, CustomExceptionType.BOOTSTRAP_CONFIG, FAILED_WHEN_LOADING_NAMED_ENVIRONMENT_CONFIG_FROM_ENVIRONMENT_PROPERTIES_FILE, expandEnvName, envPropertiesPath);
        }
    }

    /**
     * Sets string var.
     *
     * @param varName the var name
     * @param value   the value
     */
    public void setStringVar(final String varName, final String value) {
        stringMap.put(varName, value);
        LOGGER.debug("Setting value [{}] in variable [{}]", value, varName);
        envStringMap.remove(varName);
    }

    /**
     * Gets string var.
     *
     * @param name the name
     * @return the string var
     */
    public String getStringVar(final String name) {
        LOGGER.debug("lookup string var [{}]", name);
        String result;

        statePropertiesSvc.populateGlobalPropsMap();
        if (stringMap.containsKey(name)) {
            result = stringMap.get(name);
            LOGGER.debug("fetched from String map [{}] => [{}]", name, result);
            return result;
        }

        if (envStringMap.containsKey(name)) {
            result = envStringMap.get(name);
            LOGGER.debug("fetched from Environment Properties map [{}] => [{}]", name, result);
            return result;
        }
        return statePropertiesSvc.getGlobalPropsMap(name);
    }

    /**
     * Expand expression string.
     * Fetching values from variables by expanding each variable in the expression or string
     * @param expression the expression
     * @return the string
     */
    public String expandExpression(final String expression) {
        if (Strings.isNullOrEmpty(expression)) {
            return null;
        }
        String originalExpression = expression;
        int varStart = expression.indexOf("${");

        if (varStart >= 0) {
            String varName;
            String expanded = "";
            String value;

            while (varStart >= 0) {
                int varEnd = originalExpression.indexOf('}', varStart + 2);
                if (varEnd > varStart + 1) {
                    varName = originalExpression.substring(varStart + 2, varEnd);
                    value = this.getStringVar(varName);
                    expanded = originalExpression.substring(0, varStart) + value + originalExpression.substring(varEnd + 1);
                }
                originalExpression = expanded;
                varStart = originalExpression.indexOf("${");
            }
        }
        return originalExpression;
    }
}
