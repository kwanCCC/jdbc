package org.doraemon.treasure.grammer.backends.druid.dsl.jackson;

import java.io.IOException;

import org.doraemon.treasure.grammer.backends.druid.dsl.queries.TopNQuery;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class TopNMetricSpecSerializer extends StdSerializer<TopNQuery.TopNMetricSpec> {

    protected TopNMetricSpecSerializer() {
        super(TopNQuery.TopNMetricSpec.class);
    }

    @Override
    public void serialize(TopNQuery.TopNMetricSpec topNMetricSpec, JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        if(topNMetricSpec.getType() == null) {
            jsonGenerator.writeString(topNMetricSpec.getMetric());
        } else {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("type", topNMetricSpec.getType().name());
            jsonGenerator.writeStringField("metric", topNMetricSpec.getMetric());
            jsonGenerator.writeEndObject();
        }
    }
}
