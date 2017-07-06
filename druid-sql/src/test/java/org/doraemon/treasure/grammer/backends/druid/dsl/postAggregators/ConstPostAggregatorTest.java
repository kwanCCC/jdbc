package org.doraemon.treasure.grammer.backends.druid.dsl.postAggregators;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.doraemon.treasure.grammer.JSONAssert;

public class ConstPostAggregatorTest {

    @Test
    public void test() {
        ConstPostAggregator agg = new ConstPostAggregator("size", 1000);
        JSONObject jo = JSON.parseObject(JSON.toJSONString(agg));
        JSONAssert.eq(jo, "name", "size");
        JSONAssert.eq(jo, "type", "constant");
        JSONAssert.eq(jo, "value", 1000);
    }

}
