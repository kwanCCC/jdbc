package org.doraemon.treasure.grammer.backends.metricstore.dsl.columns;

import org.doraemon.treasure.grammer.ObjectMapperFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConstColumnTest {
    ObjectMapper mapper = ObjectMapperFactory.getMapper();
    @Test
    public void test_serialization() throws JsonProcessingException {
        
        assertEquals("1", mapper.writeValueAsString((Column)new ConstColumn(1)));
        assertEquals("1.0", mapper.writeValueAsString(new ConstColumn(1.0)));
        assertEquals("\"1\"", mapper.writeValueAsString(new ConstColumn("1")));
    }
}