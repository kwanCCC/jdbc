package org.doraemon.treasure.grammer.backends.druid.dsl.jackson;

import org.doraemon.treasure.grammer.backends.druid.dsl.queries.TopNQuery;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;
import java.io.Writer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;


public class TopNMetricSpecSerializerTest {

    private ObjectMapper objectMapper;
    private TopNMetricSpecSerializer serializer;

    @Before
    public void setUp() {
        serializer = new TopNMetricSpecSerializer();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void serialize() throws Exception {
        TopNQuery.TopNMetricSpec metricSpec = new TopNQuery.TopNMetricSpec("metric1", TopNQuery.TopNMetricSpecType.inverted);
        Writer jsonWriter = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(jsonWriter);
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
        serializer.serialize(metricSpec, jsonGenerator, serializerProvider);
        jsonGenerator.flush();
        assertThat(jsonWriter.toString(), is("{\"type\":\"inverted\",\"metric\":\"metric1\"}"));
    }

    @Test
    public void serialize_withoutType() throws Exception {
        TopNQuery.TopNMetricSpec metricSpec = new TopNQuery.TopNMetricSpec();
        metricSpec.setMetric("metric1");
        Writer jsonWriter = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(jsonWriter);
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
        serializer.serialize(metricSpec, jsonGenerator, serializerProvider);
        jsonGenerator.flush();
        assertThat(jsonWriter.toString(), is("\"metric1\""));
    }

}
