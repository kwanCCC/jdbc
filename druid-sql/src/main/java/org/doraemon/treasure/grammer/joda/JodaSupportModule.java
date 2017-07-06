package org.doraemon.treasure.grammer.joda;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.joda.time.Interval;

public class JodaSupportModule extends SimpleModule {
    public JodaSupportModule() {
        super();
        this.addDeserializer(Interval.class, new IntervalDeserializer());
        this.addSerializer(Interval.class, new IntervalSerializer());
    }
}
