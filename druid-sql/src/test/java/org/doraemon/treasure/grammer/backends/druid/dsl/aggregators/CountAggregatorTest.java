package org.doraemon.treasure.grammer.backends.druid.dsl.aggregators;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.doraemon.treasure.grammer.JSONAssert;

public class CountAggregatorTest {

    @Test
    public void test() {
        CountAggregator agg = new CountAggregator();
        JSONObject jo = JSON.parseObject(JSON.toJSONString(agg));
        JSONAssert.eq(jo, "type", "count");
    }

}
