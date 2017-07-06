package org.doraemon.treasure.grammer.backends.metricstore.dsl.queries;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.Column;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.filters.EqualFilter;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.filters.Filter;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.limits.Limit;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.limits.OrderBy;
import org.joda.time.Interval;
import org.junit.Test;

import org.doraemon.treasure.grammer.ObjectMapperFactory;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.aggregations.SumColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.primitives.DoubleColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.primitives.IntColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.primitives.StringColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.filters.AndFilter;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.limits.OrderByLimit;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TopNQueryTest {

    private final String json =
            "{\n" + "  \"type\": \"topn\",\n"
            + "  \"dataSource\": \"ds\",\n"
            + "  \"interval\": \"2016-06-01T15:26:12.116+08:00/2016-06-01T15:56:12.116+08:00\",\n"
            + "  \"limit\": {\n" + "    \"offset\": 1,\n" + "    \"n\": 20,\n"
            + "    \"orderBys\": [{\"column\": { \"innerColumn\": {\"field\": \"num4\", \"type\": \"double\"}, \"type\": \"sum\"}, \"desc\": true}],\n"
            + "    \"type\": \"orderby\"\n" + "  },\n" + "  \"filter\": {\n"
            + "    \"type\": \"and\",\n" + "    \"filters\": [\n" + "      {\n"
            + "        \"type\": \"equal\",\n"
            + "        \"column\": {\"field\": \"uid\", \"type\": \"int\"},\n"
            + "        \"value\": 1\n" + "      },\n" + "      {\n"
            + "        \"type\": \"equal\",\n"
            + "        \"column\": {\"field\": \"name\", \"type\": \"string\"},\n"
            + "        \"value\": \"name1\"\n" + "      }\n" + "    ]\n" + "  },\n"
            + "  \"columns\": [{\"innerColumn\": {\"field\": \"num4\", \"type\": \"double\"}, \"type\": \"sum\"}],\n"
            + "  \"groupBys\": [\"name\"]\n" + "}\n";

    @Test
    public void test_deserialization() throws IOException {

        Query actual = ObjectMapperFactory.getMapper().readValue(json, Query.class);
        
        TopNQuery expected = getTopNQuery();
        
        assertEquals(expected, actual);
    }

    @Test
    public void test_serialization() throws Exception {
        ObjectMapper mapper = ObjectMapperFactory.getMapper();

        String actual = mapper.writeValueAsString(getTopNQuery());

        assertEquals(mapper.readTree(json), mapper.readTree(actual));

    }

    private TopNQuery getTopNQuery() {
        Filter equalFilter  = new EqualFilter(new IntColumn("uid"), 1);
        Filter    equalFilter2 = new EqualFilter(new StringColumn("name"), "name1");
        AndFilter filter       = new AndFilter(Arrays.<Filter>asList(equalFilter, equalFilter2));
        List<Column> columns = Arrays.<Column>asList(new SumColumn(new DoubleColumn("num4")));
        Interval interval      = Interval.parse("2016-06-01T15:26:12.116+08:00/2016-06-01T15:56:12.116+08:00");
        Column   orderByColumn = new SumColumn(new DoubleColumn("num4"));
        Limit limit         = new OrderByLimit(1, 20, Arrays.asList(new OrderBy(orderByColumn)));
        
        return TopNQuery.builder().type("topn").dataSource("ds").interval(interval).columns(columns).filter(filter)
                       .groupBys(Arrays.asList("name")).limit(limit).build();
    }
}
