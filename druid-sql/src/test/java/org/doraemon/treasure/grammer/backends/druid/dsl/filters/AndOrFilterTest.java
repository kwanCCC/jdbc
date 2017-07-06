package org.doraemon.treasure.grammer.backends.druid.dsl.filters;

import java.util.Arrays;

import org.junit.Test;
import org.mockito.Mockito;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.doraemon.treasure.grammer.JSONAssert;

public class AndOrFilterTest {

    @Test
    public void test() {
        AndOrFilter filter = new AndOrFilter("and", Arrays.asList(Mockito.mock(Filter.class), Mockito.mock(Filter.class)));
        String json = JSON.toJSONString(filter);
        JSONObject jo = JSON.parseObject(json);
        JSONAssert.eq(jo, "type", "and");
        JSONAssert.exists(jo, "fields");
    }

}
