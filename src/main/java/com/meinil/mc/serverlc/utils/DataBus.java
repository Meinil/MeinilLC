package com.meinil.mc.serverlc.utils;

import java.util.HashMap;

/**
 * @Author Meinil
 * @Version 1.0
 */
public class DataBus {
    private final static HashMap<String, Object> DATA = new HashMap<>();
    private DataBus() {}
    public static Object getData(String key) {
        return DATA.get(key);
    }
    public static void setData(String key, Object data) {
        DATA.put(key, data);
    }
}
