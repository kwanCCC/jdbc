package org.doraemon.treasure.grammer.backends.druid.dsl.aggregators;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.doraemon.treasure.grammer.JSONAssert;

public class MathAggregatorTest {

    @Test
    public void test() {
        MathAggregator agg = new MathAggregator(EnumBinaryAggregator.doubleMax.name(), "avg", "num1");
        JSONObject jo = JSON.parseObject(JSON.toJSONString(agg));
        JSONAssert.eq(jo, "type", "doubleMax");
        JSONAssert.eq(jo, "name", "avg");
        JSONAssert.eq(jo, "fieldName", "num1");
    }

    @Test(expected = NullPointerException.class)
    public void testNPEConstructor() {
        MathAggregator agg = new MathAggregator("", null, null);
    }

}
