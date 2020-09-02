package com.cmmr.permission.utils;

import org.apache.commons.lang3.StringUtils;

public class LevelUtil {

    public static final String SEPARATOR = ".";

    public static final String ROOT = "0";

    // 0
    // 0.1
    // 0.1.2
    public static String calculateLevel(String parentLevel, Integer parentId) {
        if(StringUtils.isBlank(parentLevel)) {
            return ROOT;
        }
        return StringUtils.join(parentLevel, SEPARATOR, parentId);
    }
}
