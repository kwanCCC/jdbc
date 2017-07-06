package org.doraemon.treasure.grammer.backends.druid.dsl.filters;

import org.junit.Test;
import org.mockito.Mockito;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.doraemon.treasure.grammer.JSONAssert;


public class NotFilterTest {

    @Test
    public void test() {
        NotFilter not = new NotFilter(Mockito.mock(Filter.class));
        String json = JSON.toJSONString(not);
        JSONObject jo = JSON.parseObject(json);
        JSONAssert.eq(jo, "type", "not");
        JSONAssert.exists(jo, "field");
    }

}
