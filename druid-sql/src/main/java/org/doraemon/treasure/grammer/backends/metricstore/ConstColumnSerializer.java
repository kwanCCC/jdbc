package org.doraemon.treasure.grammer.backends.metricstore;

import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.ConstColumn;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class ConstColumnSerializer extends StdSerializer<ConstColumn> {
    
    protected ConstColumnSerializer() {
        super(ConstColumn.class);
    }
    
    @Override
    public void serialize(ConstColumn constColumn, JsonGenerator jsonGenerator, SerializerProvider serializerProvider
    ) throws IOException, JsonGenerationException {
        if (constColumn.getValue() instanceof Integer || constColumn.getValue() instanceof Long) {
            Number value = (Number) constColumn.getValue();
            jsonGenerator.writeNumber(value.longValue());
        } else if (constColumn.getValue() instanceof Number) {
            Number value = (Number) constColumn.getValue();
            jsonGenerator.writeNumber(value.doubleValue());
        } else {
            jsonGenerator.writeString(constColumn.getValue().toString());
        }
    }
    
    @Override
    public void serializeWithType(ConstColumn value, JsonGenerator jgen, SerializerProvider provider,
                                         TypeSerializer typeSer
    ) throws IOException, JsonProcessingException {
        //typeSer.writeTypePrefixForObject(value, jgen);
        serialize(value, jgen, provider);
        //typeSer.writeTypeSuffixForObject(value, jgen);
    }
}
