package org.doraemon.treasure.grammer.joda;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.joda.time.Interval;

import java.io.IOException;

public class IntervalSerializer extends StdSerializer<Interval> {
    protected IntervalSerializer() {
        super(Interval.class);
    }
    
    @Override
    public void serialize(Interval interval, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException, JsonGenerationException {
        jsonGenerator.writeString(interval.toString());
    }
}
