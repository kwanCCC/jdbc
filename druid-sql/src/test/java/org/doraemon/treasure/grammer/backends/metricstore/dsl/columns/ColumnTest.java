package org.doraemon.treasure.grammer.backends.metricstore.dsl.columns;

import org.doraemon.treasure.grammer.ObjectMapperFactory;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.aggregations.MaxColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.aggregations.MinColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.aggregations.SumColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.arithmetric.AddColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.arithmetric.DivColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.arithmetric.MinusColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.arithmetric.MultiplyColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.others.AliasColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.others.RefColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.primitives.DoubleColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.primitives.FloatColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.primitives.IPColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.primitives.IntColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.primitives.LongColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.primitives.StringColumn;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ColumnTest {
    
    private final ObjectMapper mapper      = ObjectMapperFactory.getMapper();
    private final String       inner       = "{\"field\": \"n1\", \"type\": \"int\"}";
    private final IntColumn    innerColumn = new IntColumn("n1");
    
    @Test
    public void deserialize_primitive_columns() throws IOException {
        
        Column stringColumn = mapper.readValue("{\"field\": \"f\", \"type\": \"string\"}", Column.class);
        Assert.assertEquals(new StringColumn("f"), stringColumn);
        
        Column iPColumn = mapper.readValue("{\"field\": \"f\", \"type\": \"ip\"}", Column.class);
        Assert.assertEquals(new IPColumn("f"), iPColumn);
        
        Column intColumn = mapper.readValue("{\"field\": \"f\", \"type\": \"int\"}", Column.class);
        assertEquals(new IntColumn("f"), intColumn);
        
        Column longColumn = mapper.readValue("{\"field\": \"f\", \"type\": \"long\"}", Column.class);
        Assert.assertEquals(new LongColumn("f"), longColumn);
        
        Column floatColumn = mapper.readValue("{\"field\": \"f\", \"type\": \"float\"}", Column.class);
        Assert.assertEquals(new FloatColumn("f"), floatColumn);
        
        Column doubleColumn = mapper.readValue("{\"field\": \"f\", \"type\": \"double\"}", Column.class);
        Assert.assertEquals(new DoubleColumn("f"), doubleColumn);
    }
    
    @Test
    public void serialize_primitive_columns() throws IOException {
        
        Column expectedStringColumn = new StringColumn("f");
        String stringColumnStr      = ObjectMapperFactory.getMapper().writeValueAsString(expectedStringColumn);
        
        assertEquals(expectedStringColumn, ObjectMapperFactory.getMapper().readValue(stringColumnStr, Column.class));
        
        Column expectedIPColumn = new IPColumn("f");
        String iPColumnStr      = ObjectMapperFactory.getMapper().writeValueAsString(expectedIPColumn);
        
        assertEquals(expectedIPColumn, ObjectMapperFactory.getMapper().readValue(iPColumnStr, Column.class));
        
        Column expectedIntColumn = new IntColumn("f");
        String intColumnStr      = ObjectMapperFactory.getMapper().writeValueAsString(expectedIntColumn);
        
        assertEquals(expectedIntColumn, ObjectMapperFactory.getMapper().readValue(intColumnStr, Column.class));
        
        Column expectedLongColumn = new LongColumn("f");
        String longColumnStr      = ObjectMapperFactory.getMapper().writeValueAsString(expectedLongColumn);
        
        assertEquals(expectedLongColumn, ObjectMapperFactory.getMapper().readValue(longColumnStr, Column.class));
        
        Column expectedFloatColumn = new FloatColumn("f");
        String floatColumnStr      = ObjectMapperFactory.getMapper().writeValueAsString(expectedFloatColumn);
        
        assertEquals(expectedFloatColumn, ObjectMapperFactory.getMapper().readValue(floatColumnStr, Column.class));
        
        Column expectedDoubleColumn = new DoubleColumn("f");
        String doubleColumnStr      = ObjectMapperFactory.getMapper().writeValueAsString(expectedDoubleColumn);
        
        assertEquals(expectedDoubleColumn, ObjectMapperFactory.getMapper().readValue(doubleColumnStr, Column.class));
    }
    
    @Test
    public void test_deserialize_aggregation_columns() throws IOException {
        
        IntColumn innerColumn = new IntColumn("n1");
        
        Column expectedSum = new SumColumn(innerColumn);
        Column actualSum   = mapper.readValue("{\"type\": \"sum\", \"innerColumn\": " + inner + "}", Column.class);
        assertEquals(expectedSum, actualSum);
        
        Column expectedMax = new MaxColumn(innerColumn);
        Column actualMax   = mapper.readValue("{\"type\": \"max\", \"innerColumn\": " + inner + "}", Column.class);
        assertEquals(expectedMax, actualMax);
        
        Column expectedMin = new MinColumn(innerColumn);
        Column actualMin   = mapper.readValue("{\"type\": \"min\", \"innerColumn\": " + inner + "}", Column.class);
        assertEquals(expectedMin, actualMin);
    }
    
    @Test
    public void test_serialize_aggregation_columns() throws IOException {
        
        Column expectedSum     = new MaxColumn(innerColumn);
        String expectedSumJson = mapper.writeValueAsString(expectedSum);
        assertEquals(expectedSum, mapper.readValue(expectedSumJson, Column.class));
        
        Column expectedMax     = new MaxColumn(innerColumn);
        String expectedMaxJson = mapper.writeValueAsString(expectedMax);
        assertEquals(expectedMax, mapper.readValue(expectedMaxJson, Column.class));
        
        Column expectedMin     = new MinColumn(innerColumn);
        String expectedMinJson = mapper.writeValueAsString(expectedMin);
        assertEquals(expectedMin, mapper.readValue(expectedMinJson, Column.class));
    }
    
    @Test
    public void test_deserialize_arithMetric_columns() throws IOException {
        
        String       inner        = "[{\"field\": \"n1\", \"type\": \"int\"}, {\"field\": \"n2\", \"type\": \"int\"}]";
        List<Column> innerColumns = Arrays.<Column>asList(new IntColumn("n1"), new IntColumn("n2"));
        
        Column expectedAdd = new AddColumn(innerColumns);
        String addJson     = "{\"innerColumns\": " + inner + ", \"type\":\"add\"}";
        assertEquals(expectedAdd, mapper.readValue(addJson, Column.class));
        Column expectedDiv = new DivColumn(innerColumns);
        String divJson     = "{\"innerColumns\": " + inner + ", \"type\":\"div\"}";
        assertEquals(expectedDiv, mapper.readValue(divJson, Column.class));
        Column expectedMultiply = new MultiplyColumn(innerColumns);
        String multiplyJson     = "{\"innerColumns\": " + inner + ", \"type\":\"multiply\"}";
        assertEquals(expectedMultiply, mapper.readValue(multiplyJson, Column.class));
        Column expectedMinus = new MinusColumn(innerColumns);
        String minusJson     = "{\"innerColumns\": " + inner + ", \"type\":\"minus\"}";
        assertEquals(expectedMinus, mapper.readValue(minusJson, Column.class));
    }
    
    @Test
    public void test_serialization_arithMetric_columns() throws IOException {
        
        List<Column> innerColumns = Arrays.<Column>asList(new IntColumn("n1"), new IntColumn("n2"));
        
        Column expectedAdd = new DivColumn(innerColumns);
        String addJson     = mapper.writeValueAsString(expectedAdd);
        assertEquals(expectedAdd, mapper.readValue(addJson, Column.class));
        
        Column expectedDiv = new DivColumn(innerColumns);
        String divJson     = mapper.writeValueAsString(expectedDiv);
        assertEquals(expectedDiv, mapper.readValue(divJson, Column.class));
        
        Column expectedMultiply = new MultiplyColumn(innerColumns);
        String multiplyJson     = mapper.writeValueAsString(expectedMultiply);
        assertEquals(expectedMultiply, mapper.readValue(multiplyJson, Column.class));
        
        Column expectedMinus = new MinusColumn(innerColumns);
        String minusJson     = mapper.writeValueAsString(expectedMinus);
        assertEquals(expectedMinus, mapper.readValue(minusJson, Column.class));
        
    }
    
    @Test
    public void test_deserialize_other_columns() throws IOException {
        
        String aliasStr      = "{\"alias\": \"alia\", \"type\": \"alias\", \"column\": " + inner + "}";
        Column expectedAlias = new AliasColumn(innerColumn, "alia");
        assertEquals(expectedAlias, mapper.readValue(aliasStr, Column.class));
        
        String refStr      = "{\"type\": \"ref\", \"name\": \"alia\" }";
        Column expectedRef = new RefColumn("alia");
        assertEquals(expectedRef, mapper.readValue(refStr, Column.class));
    }
    
    @Test
    public void test_serialize_other_columns() throws IOException {
        Column expectedAlias = new AliasColumn(innerColumn, "alia");
        String aliasStr      = mapper.writeValueAsString(expectedAlias);
        assertEquals(expectedAlias, mapper.readValue(aliasStr, Column.class));
        
        Column expectedRef = new RefColumn("alia");
        String refStr      = mapper.writeValueAsString(expectedRef);
        assertEquals(expectedRef, mapper.readValue(refStr, Column.class));
    }
}
