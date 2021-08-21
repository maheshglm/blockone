package com.blockone.test.studentapi;

import com.blockone.test.studentapi.svc.StatePropertiesSvc;
import com.blockone.test.studentapi.svc.StateSvc;
import com.cfg.SpringConfig;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Bootstrap class which contains init and cleanup methods for spring context.
 */
public class Bootstrap {

    private static final Logger LOGGER = LoggerFactory.getLogger(Bootstrap.class);
    private static final String CURR_ENV_NAME = "curr.env.name";

    private static ConfigurableApplicationContext context;
    private static boolean initialized = false;
    private static Class configClass = SpringConfig.class;

    private static final String DEFAULT_ENV_NAME = "dev";

    private Bootstrap() {
    }

    /**
     * Sets config class.
     *
     * @param configClass the config class
     */
    public static void setConfigClass(Class configClass) {
        Bootstrap.configClass = configClass;
    }

    /**
     * Configure container.
     */
    public static void configureContainer() {
        if (context == null) {
            context = new AnnotationConfigApplicationContext(configClass);
        }
    }

    /**
     * Init.
     * Initialize the spring context
     */
    public static void init() {
        if (!initialized) {
            configureContainer();
            initialized = true;
            LOGGER.info("bootstrap: initialized");
            loadEnvProperties();
            loadFrameworkProperties();
        }
    }

    /**
     * Done.
     * Close spring context
     */
    public static final void done() {
        if (context != null) {
            try {
                context.close();
            } finally {
                context = null;
            }
        }
    }

    private static void loadFrameworkProperties() {
        StatePropertiesSvc statePropertiesSvc = context.getBean(StatePropertiesSvc.class);
        statePropertiesSvc.loadFrameworkProperties();
    }

    private static void loadEnvProperties() {
        StateSvc stateSvc = context.getBean(StateSvc.class);
        var envToUse = System.getProperty(CURR_ENV_NAME);
        if (Strings.isNullOrEmpty(envToUse)) {
            envToUse = DEFAULT_ENV_NAME;
        }
        LOGGER.info("Environment to use: [{}]", envToUse);
        stateSvc.loadEnvProperties(envToUse);
    }

    /**
     * Gets bean.
     *
     * @param class1 the class 1
     * @return the bean
     */
    public static synchronized Object getBean(Class class1) {
        init();
        return context.getBean(class1);
    }
}
