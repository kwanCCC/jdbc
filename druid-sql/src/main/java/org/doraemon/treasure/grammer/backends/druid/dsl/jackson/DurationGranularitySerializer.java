package org.doraemon.treasure.grammer.backends.druid.dsl.jackson;

import org.doraemon.treasure.grammer.mid.model.granularities.DurationGranularity;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.IOException;

public class DurationGranularitySerializer extends StdSerializer<DurationGranularity>
{
  protected DurationGranularitySerializer()
  {
    super(DurationGranularity.class);
  }

  @Override
  public void serialize(
      DurationGranularity durationGranularity,
      JsonGenerator jsonGenerator,
      SerializerProvider serializerProvider
  ) throws IOException, JsonGenerationException
  {
    if (durationGranularity == null) {
      jsonGenerator.writeNull();
    } else {
      jsonGenerator.writeStartObject();
      jsonGenerator.writeFieldName("type");
      jsonGenerator.writeString("duration");
      jsonGenerator.writeFieldName("duration");
      jsonGenerator.writeNumber(durationGranularity.getDuration());
      if (durationGranularity.getOrigin() > 0) {
        jsonGenerator.writeFieldName("origin");
        String originInStr = DateFormatUtils.formatUTC(
            durationGranularity.getOrigin(),
            DateFormatUtils.ISO_DATETIME_FORMAT.getPattern()
        );
        jsonGenerator.writeString(originInStr);
      }
      jsonGenerator.writeEndObject();
    }
  }
}
