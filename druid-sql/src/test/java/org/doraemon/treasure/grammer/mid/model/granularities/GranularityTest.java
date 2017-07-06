package org.doraemon.treasure.grammer.mid.model.granularities;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.doraemon.treasure.grammer.JSONAssert;
import org.doraemon.treasure.grammer.ObjectMapperFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;

import java.io.IOException;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;

//TODO: add deserialization tests
public class GranularityTest
{

  @Test
  public void serialize_simple_granularity() throws JsonProcessingException
  {
    SimpleGranularity sg = new SimpleGranularity("all");
    assertEquals("\"all\"", ObjectMapperFactory.getMapper().writeValueAsString(sg));
  }

  @Test
  public void serialize_period_granularity() throws IOException
  {
    Calendar cal = Calendar.getInstance();
    cal.set(2015, 1, 20, 20, 20, 20);
    long start = cal.getTimeInMillis();
    PeriodGranularity pg = new PeriodGranularity("PT30M", "America/Los_Angeles", start);
    String json = ObjectMapperFactory.getMapper().writeValueAsString(pg);

    JSONObject jo = JSON.parseObject(json);

    JSONAssert.eq(jo, "type", "period");
    JSONAssert.eq(jo, "period", "PT30M");
    JSONAssert.eq(jo, "timeZone", "America/Los_Angeles");
    JSONAssert.eq(jo, "origin", "2015-02-20T12:20:20");
  }


  @Test
  public void serialize_duration_granularity() throws JsonProcessingException
  {
    Calendar cal = Calendar.getInstance();
    cal.set(2015, 1, 20, 20, 20, 20);
    long start = cal.getTimeInMillis();
    DurationGranularity durationGranularity = new DurationGranularity(10L, start);

    String json = ObjectMapperFactory.getMapper().writeValueAsString(durationGranularity);

    JSONObject jo = JSON.parseObject(json);
    JSONAssert.eq(jo, "type", "duration");
    JSONAssert.eq(jo, "duration", 10);
    JSONAssert.eq(jo, "origin", "2015-02-20T12:20:20");
  }
}
