package org.doraemon.treasure.grammer.backends.druid.dsl.postAggregators;

import org.doraemon.treasure.grammer.JSONAssert;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class FieldAccessPostAggregatorTest {

    @Test
    public void test() {
        FieldAccessPostAggregator agg = new FieldAccessPostAggregator("size");
        JSONObject jo = JSON.parseObject(JSON.toJSONString(agg));
        JSONAssert.eq(jo, "type", "fieldAccess");
        JSONAssert.eq(jo, "fieldName", "size");
    }

}
