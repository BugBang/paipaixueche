package com.session.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 该类主要用于activity之间传递复杂数据或非相邻的activity之间传递数据
 *
 * @author YJ Liang
 *         2016  上午9:26:16
 */
public class ExtraMap {
    private Map<String, Object> map = new HashMap<String, Object>();

    public void putExtra(String key, boolean value) {
        map.put(key, value);
    }

    public boolean getExtra(String key, boolean defaultValue) {
        Object obj = map.get(key);
        if (obj == null) {
            return defaultValue;
        }
        if (obj instanceof Boolean) {
            map.remove(key);
            return (Boolean) obj;
        } else {
            return defaultValue;
        }

    }
}
