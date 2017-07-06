package org.doraemon.treasure.grammer;

import org.doraemon.treasure.grammer.backends.metricstore.MetricStoreModule;
import org.doraemon.treasure.grammer.backends.druid.dsl.jackson.DruidDSLModule;
import org.doraemon.treasure.grammer.joda.JodaSupportModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

public class ObjectMapperFactory {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    static {
        objectMapper.registerModule(new DruidDSLModule());
        objectMapper.registerModule(new JodaModule());
        objectMapper.registerModule(new JodaSupportModule());
        objectMapper.registerModule(new MetricStoreModule());
    }
    
    public static ObjectMapper getMapper() {
        return objectMapper;
    }
}
