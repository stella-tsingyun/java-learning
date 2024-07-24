package org.tsing.learning.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UUIDUtils {
    public static String buildUUId(String routingKey) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return routingKey + "_" + sdf.format(new Date());
    }
}
