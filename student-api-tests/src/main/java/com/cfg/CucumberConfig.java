package com.cfg;

import com.google.common.base.Strings;

/**
 * Cucumber config.
 */
public class CucumberConfig {

    /**
     * The constant features.
     */
    public static String features = "tests/features";
    /**
     * The constant reports.
     */
    public static String reports = "./testout/reports";

    /**
     * Get cucumber options string [ ].
     *
     * @return the string [ ]
     */
    public static String[] getCucumberOptions() {

        String featuresProperty = System.getProperty("features.path");
        String reportsPathProperty = System.getProperty("reports.path");

        if (!Strings.isNullOrEmpty(featuresProperty)) {
            features = featuresProperty;
        }

        if (!Strings.isNullOrEmpty(reportsPathProperty)) {
            reports = reportsPathProperty;
        }

        return new String[]{
                "--strict",
                "--monochrome",
                "--glue",
                "glue",
                "--plugin",
                "pretty",
                "--plugin",
                "html:" + reports,
                features
        };
    }
}
