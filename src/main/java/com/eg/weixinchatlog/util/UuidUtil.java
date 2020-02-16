package com.eg.weixinchatlog.util;

import java.util.UUID;

/**
 * @time 2020-01-22 16:44
 */
public class UuidUtil {
    public static String getUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
