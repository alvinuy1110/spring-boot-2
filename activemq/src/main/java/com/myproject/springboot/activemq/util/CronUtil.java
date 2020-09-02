package com.myproject.springboot.activemq.util;

import java.time.LocalDateTime;
/*
* * * * * *
| | | | | |
| | | | | +-- Year              (range: 1900-3000)
| | | | +---- Day of the Week   (range: 1-7, 1 standing for Monday)
| | | +------ Month of the Year (range: 1-12)
| | +-------- Day of the Month  (range: 1-31)
| +---------- Hour              (range: 0-23)
+------------ Minute            (range: 0-59)
 */
public class CronUtil {


    public static String getCronExpression(LocalDateTime localDateTime) {
        StringBuilder sb = new StringBuilder();

        return sb.toString();
    }
}
