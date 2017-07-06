package org.doraemon.treasure.grammer;

import junit.framework.Assert;

import com.alibaba.fastjson.JSONObject;

public class JSONAssert {
    public static void eq(JSONObject json, String field, Object value) {
        Assert.assertTrue(json.containsKey(field));
        Assert.assertEquals(value, json.get(field));
    }

    public static void exists(JSONObject json, String field) {
        Assert.assertTrue(json.containsKey(field));
    }

    public static void nonExist(JSONObject json, String field) {
        Assert.assertFalse(json.containsKey(field));
    }
}
