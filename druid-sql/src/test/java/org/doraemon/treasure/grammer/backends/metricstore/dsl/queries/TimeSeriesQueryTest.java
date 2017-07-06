package org.doraemon.treasure.grammer.backends.metricstore.dsl.queries;

import org.doraemon.treasure.grammer.ObjectMapperFactory;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.Column;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.aggregations.SumColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.primitives.DoubleColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.primitives.IntColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.filters.AndFilter;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.filters.EqualFilter;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.filters.Filter;
import org.joda.time.Interval;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TimeSeriesQueryTest {
    
    @Test
    public void test_deserialization() throws IOException {
        String json = "{\n" + "  \"type\": \"timeseries\",\n"
                              + "  \"interval\": \"2016-06-01T15:26:12.116+08:00/2016-06-01T15:56:12.116+08:00\",\n"
                              + "  \"points\": 30,\n"
                              + "  \"columns\": [ {\"innerColumn\": {\"field\": \"num1\", \"type\": \"double\"}, \"type\": \"sum\"}],\n"
                              + "  \"filter\": {\n" + "    \"type\": \"and\",\n" + "    \"filters\": [\n" + "      {\n"
                              + "        \"type\": \"equal\",\n"
                              + "        \"column\": {\"field\": \"uid\", \"type\": \"int\"},\n"
                              + "        \"value\": 1\n" + "      },\n" + "      {\n" + "        \"type\": \"equal\",\n"
                              + "        \"column\": {\"field\": \"uid\", \"type\": \"int\"},\n"
                              + "        \"value\": 1\n" + "      }\n" + "    ]\n" + "  }\n" + "}";
        
        TimeSeriesQuery expected = getTimeSeriesQuery();
        
        assertEquals(expected, ObjectMapperFactory.getMapper().readValue(json, TimeSeriesQuery.class));
    }
    
    @Test
    public void test_serialization() throws IOException {
        TimeSeriesQuery expected = getTimeSeriesQuery();
        String          json     = ObjectMapperFactory.getMapper().writeValueAsString(expected);
        
        assertEquals(expected, ObjectMapperFactory.getMapper().readValue(json, TimeSeriesQuery.class));
    }
    
    private TimeSeriesQuery getTimeSeriesQuery() {
        Filter filter    = new EqualFilter(new IntColumn("uid"), 1);
        AndFilter    andFilter = new AndFilter(Arrays.<Filter>asList(filter, filter));
        List<Column> columns   = Arrays.<Column>asList(new SumColumn(new DoubleColumn("num1")));
        Interval     interval  = Interval.parse("2016-06-01T15:26:12.116+08:00/2016-06-01T15:56:12.116+08:00");
        
        return TimeSeriesQuery.builder().type("timeseries").interval(interval).points(30).columns(columns)
                       .filter(andFilter).build();
    }
    
}