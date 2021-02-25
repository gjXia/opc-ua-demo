package com.xgj.demo.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gjXia
 * @date 2021/2/23 10:23
 */
public class WsdUtil {

    private final Map<String, Object> wsdInfo = new HashMap<>();

    private static WsdUtil instance;

    private WsdUtil() {
    }

    public static WsdUtil getInstance() {
        if (instance == null) {
            instance = new WsdUtil();
        }
        return instance;
    }

    public void add(String key, Object value) {
        if (key == null || value == null) {
            return;
        }
        wsdInfo.put(key, value);
    }

    public void remove(String key) {
        wsdInfo.remove(key);
    }

    public Object get(String key) {
        return wsdInfo.get(key);
    }

    public Map<String, Object> getWsdInfo() {
        return wsdInfo;
    }
}
