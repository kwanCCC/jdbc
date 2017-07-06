package org.doraemon.treasure.grammer.backends.metricstore.dsl.queries;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.doraemon.treasure.grammer.ObjectMapperFactory;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.Column;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.aggregations.SumColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.primitives.DoubleColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.primitives.IntColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.primitives.StringColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.filters.AndFilter;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.filters.EqualFilter;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.filters.Filter;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.limits.Limit;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.limits.OrderBy;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.limits.OrderByLimit;
import org.joda.time.Interval;
import org.junit.Test;

public class GroupByQueryTest {

    private final String json = "{\n" + "  \"type\": \"groupby\",\n"
                    + "  \"dataSource\": \"ds\",\n"
                    + "  \"interval\": \"2016-06-01T15:26:12.116+08:00/2016-06-01T15:56:12.116+08:00\",\n"
                    + "  \"limit\": {\n" + "    \"offset\": 1,\n" + "    \"n\": 20,\n" + "    \"orderBys\": [\n" + "      {\n"
                    + "        \"column\": {\"innerColumn\": {\"field\": \"num4\", \"type\": \"double\"}, \"type\": \"sum\"}, \"desc\": true\n"
                    + "      }\n" + "    ],\n" + "    \"type\": \"orderby\"\n" + "  },\n"
                    + "  \"points\": 30,\n" + "  \"filter\": {\n" + "    \"type\": \"and\",\n"
                    + "    \"filters\": [\n" + "      {\n" + "        \"type\": \"equal\",\n"
                    + "        \"column\": {\"field\": \"uid\", \"type\": \"int\"},\n"
                    + "        \"value\": 1\n" + "      },\n" + "      {\n"
                    + "        \"type\": \"equal\",\n"
                    + "        \"column\": {\"field\": \"name\", \"type\": \"string\"},\n"
                    + "        \"value\": \"name1\"\n" + "      }\n" + "    ]\n" + "  },\n"
                    + "  \"columns\": [{\"innerColumn\": {\"field\": \"num4\", \"type\": \"double\"}, \"type\": \"sum\"}],\n"
                    + "  \"groupBys\": [\"name\"]\n" + "}\n";

    @Test
    public void test_deserialize() throws IOException {
        GroupByQuery expectedQuery = getGroupByQuery();
        
        assertEquals(expectedQuery, ObjectMapperFactory.getMapper().readValue(json, Query.class));
        
    }

    @Test
    public void test_serialize() throws Exception {
        ObjectMapper mapper = ObjectMapperFactory.getMapper();

        String actual = mapper.writeValueAsString(getGroupByQuery());

        assertEquals(mapper.readTree(json), mapper.readTree(actual));

    }

    private GroupByQuery getGroupByQuery() {
        Filter equalFilter   = new EqualFilter(new IntColumn("uid"), 1);
        Filter       equalFilter2  = new EqualFilter(new StringColumn("name"), "name1");
        AndFilter filter        = new AndFilter(Arrays.<Filter>asList(equalFilter, equalFilter2));
        List<Column> columns       = Arrays.<Column>asList(new SumColumn(new DoubleColumn("num4")));
        Interval     interval      = Interval.parse("2016-06-01T15:26:12.116+08:00/2016-06-01T15:56:12.116+08:00");
        Column       orderByColumn = new SumColumn(new DoubleColumn("num4"));
        Limit limit         = new OrderByLimit(1, 20, Arrays.asList(new OrderBy(orderByColumn)));
        
        return GroupByQuery.builder().type("groupby").dataSource("ds").interval(interval).columns(columns).filter(filter)
                       .groupBys(Arrays.asList("name")).limit(limit).points(30).build();
    }
}
