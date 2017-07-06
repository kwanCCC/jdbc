package org.doraemon.treasure.grammer.backends.druid.dsl.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;

public class DruidDSLModule extends SimpleModule
{
  public DruidDSLModule()
  {
    // TODO: remove these serializers
    this.addSerializer(new DurationGranularitySerializer());
    this.addSerializer(new SimpleGranularitySerializer());
    this.addSerializer(new PeriodGranularitySerializer());

    this.addSerializer(new BasicDimensionSpecSerializer());
    this.addSerializer(new TopNMetricSpecSerializer());
  }
}
