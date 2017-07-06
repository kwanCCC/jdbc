package org.doraemon.treasure.grammer.backends.druid.dsl.filters;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.doraemon.treasure.grammer.JSONAssert;

public class SelectorFilterTest {

    @Test
    public void test() {

        SelectorFilter filter = new SelectorFilter("metric", "value");
        String json = JSON.toJSONString(filter);
        JSONObject jsonObj = JSON.parseObject(json);
        JSONAssert.eq(jsonObj, "dimension", "metric");
        JSONAssert.eq(jsonObj, "value", "value");
        JSONAssert.eq(jsonObj, "type", "selector");
    }

}
