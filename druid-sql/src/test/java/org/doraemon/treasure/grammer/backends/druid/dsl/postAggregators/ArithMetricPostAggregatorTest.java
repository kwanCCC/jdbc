package org.doraemon.treasure.grammer.backends.druid.dsl.postAggregators;

import java.util.Arrays;

import org.doraemon.treasure.grammer.backends.druid.dsl.aggregators.IAggregator;
import org.junit.Test;
import org.mockito.Mockito;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.doraemon.treasure.grammer.JSONAssert;

public class ArithMetricPostAggregatorTest {

    @Test
    public void test() {
        ArithMetricPostAggregator agg = new ArithMetricPostAggregator("size", "/", Arrays.asList(Mockito.mock(IAggregator.class), Mockito.mock(IAggregator.class)));
        JSONObject jo = JSON.parseObject(JSON.toJSONString(agg));
        JSONAssert.eq(jo, "type", "arithMetric");
        JSONAssert.eq(jo, "name", "size");
        JSONAssert.exists(jo, "fields");
        // JSONAssert.exists(jo, "ordering");
    }

}
