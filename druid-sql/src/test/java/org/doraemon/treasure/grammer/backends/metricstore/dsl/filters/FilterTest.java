package org.doraemon.treasure.grammer.backends.metricstore.dsl.filters;

import org.doraemon.treasure.grammer.ObjectMapperFactory;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.primitives.IntColumn;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class FilterTest {
    ObjectMapper mapper = ObjectMapperFactory.getMapper();
    
    @Test
    public void test_equal_filter_deserialization() throws IOException {
        String equalStr = "{\"type\": \"equal\", \"column\": {\"type\": \"int\", \"field\": \"id\"}, \"value\": 1}";
        
        EqualFilter expected = new EqualFilter(new IntColumn("id"), 1);
        assertEquals(expected, mapper.readValue(equalStr, Filter.class));
    }
    
    @Test
    public void test_equal_filter_ser_der() throws IOException {
        EqualFilter expected = new EqualFilter(new IntColumn("id"), 1);
        String      equalStr = mapper.writeValueAsString(expected);
        
        assertEquals(expected, mapper.readValue(equalStr, Filter.class));
    }
    
    @Test
    public void test_and_or_filter_deserialization() throws IOException {
        String inner       = "{\"type\": \"equal\", \"column\": {\"type\": \"int\", \"field\": \"id\"}, \"value\": 1}";
        Filter innerFilter = new EqualFilter(new IntColumn("id"), 1);
        
        String and         = String.format("{\"type\": \"and\", \"filters\": [%s,%s] }", inner, inner);
        Filter expectedAnd = new AndFilter(Arrays.asList(innerFilter, innerFilter));
        assertEquals(expectedAnd, mapper.readValue(and, Filter.class));
        
        String or         = String.format("{\"type\": \"or\", \"filters\": [%s,%s] }", inner, inner);
        Filter expectedOr = new OrFilter(Arrays.asList(innerFilter, innerFilter));
        assertEquals(expectedOr, mapper.readValue(or, Filter.class));
    }
    
    @Test
    public void test_and_or_filter_ser_der() throws IOException {
        Filter innerFilter = new EqualFilter(new IntColumn("id"), 1);
        
        Filter expectedAnd = new AndFilter(Arrays.asList(innerFilter, innerFilter));
        String and         = mapper.writeValueAsString(expectedAnd);
        assertEquals(expectedAnd, mapper.readValue(and, Filter.class));
        
        Filter expectedOr = new OrFilter(Arrays.asList(innerFilter, innerFilter));
        String or         = mapper.writeValueAsString(expectedOr);
        assertEquals(expectedOr, mapper.readValue(or, Filter.class));
    }
    
    
}