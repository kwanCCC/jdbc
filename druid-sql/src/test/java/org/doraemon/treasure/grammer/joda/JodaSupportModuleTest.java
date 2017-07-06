package org.doraemon.treasure.grammer.joda;

import org.doraemon.treasure.grammer.ObjectMapperFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.joda.time.Interval;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class JodaSupportModuleTest {
    private final String   intervalStr = "2015-02-20T20:20:20.955+08:00/2015-03-20T20:20:20.955+08:00";
    private final Interval interval    = Interval.parse(intervalStr);
    String intervalJson = "\"" + intervalStr + "\"";
    
    
    @Test
    public void test_serialization() throws JsonProcessingException {
        assertEquals(intervalJson, ObjectMapperFactory.getMapper().writeValueAsString(interval));
    }
    
    @Test
    public void test_deserialization() throws IOException {
        Interval actual = ObjectMapperFactory.getMapper().readValue(intervalJson, Interval.class);
        assertEquals(interval, actual);
    }
}