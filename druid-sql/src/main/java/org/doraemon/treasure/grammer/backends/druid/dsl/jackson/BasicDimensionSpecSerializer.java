package org.doraemon.treasure.grammer.backends.druid.dsl.jackson;

import org.doraemon.treasure.grammer.backends.druid.dsl.dimensions.BasicDimensionSpec;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class BasicDimensionSpecSerializer extends StdSerializer<BasicDimensionSpec>
{
  protected BasicDimensionSpecSerializer()
  {
    super(BasicDimensionSpec.class);
  }

  @Override
  public void serialize(
      BasicDimensionSpec basicDimensionSpec, JsonGenerator jsonGenerator, SerializerProvider serializerProvider
  ) throws IOException, JsonGenerationException
  {
    if (basicDimensionSpec == null) {
      jsonGenerator.writeNull();
    } else {
      jsonGenerator.writeString(basicDimensionSpec.getName());
    }
  }
}
