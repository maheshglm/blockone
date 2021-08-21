package com.blockone.test.studentapi.utils;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * Date time utils.
 */
@Component
public class DateTimeUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateTimeUtils.class);

    /**
     * Gets time stamp in given format.
     *
     * @param format the format
     * @return the time stamp
     */
    public String getTimeStamp(String format) {
        String timestamp = DateTimeFormat.forPattern(format).print(new LocalDateTime());
        LOGGER.debug("Generated timestamp [{}]", timestamp);
        return timestamp;
    }
}
