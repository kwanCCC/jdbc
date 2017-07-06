package org.doraemon.treasure.grammer.joda;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.joda.time.Interval;

import java.io.IOException;

public class IntervalDeserializer extends StdDeserializer<Interval> {
    protected IntervalDeserializer() {
        super(Interval.class);
    }
    
    @Override
    public Interval deserialize(JsonParser jsonParser, DeserializationContext deserializationContext
    ) throws IOException, JsonProcessingException {
        if (jsonParser.getCurrentToken() == JsonToken.VALUE_STRING) {
            String v = jsonParser.getText();
            return Interval.parse(v);
        } else {
            throw deserializationContext.mappingException("expected JSON String");
        }
    }
}
