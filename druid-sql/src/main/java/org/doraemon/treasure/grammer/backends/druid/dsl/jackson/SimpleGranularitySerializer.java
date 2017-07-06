package org.doraemon.treasure.grammer.backends.druid.dsl.jackson;

import org.doraemon.treasure.grammer.mid.model.granularities.SimpleGranularity;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class SimpleGranularitySerializer extends StdSerializer<SimpleGranularity>
{
  protected SimpleGranularitySerializer()
  {
    super(SimpleGranularity.class);
  }

  @Override
  public void serialize(
      SimpleGranularity simpleGranularity,
      JsonGenerator jsonGenerator,
      SerializerProvider serializerProvider
  ) throws IOException, JsonGenerationException
  {
    jsonGenerator.writeString(simpleGranularity.getName());
  }
}
