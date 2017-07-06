package org.doraemon.treasure.grammer.backends.druid.dsl.jackson;

import org.doraemon.treasure.grammer.mid.model.granularities.PeriodGranularity;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.IOException;

public class PeriodGranularitySerializer extends StdSerializer<PeriodGranularity>
{
  protected PeriodGranularitySerializer()
  {
    super(PeriodGranularity.class);
  }

  @Override
  public void serialize(
      PeriodGranularity periodGranularity, JsonGenerator jsonGenerator, SerializerProvider serializerProvider
  ) throws IOException, JsonGenerationException
  {
//    @Override
//    public String toJSONString() {
//        StringBuilder sb = new StringBuilder("{ \"type\":\"period\",");
//        sb.append("\"period\":");
//        sb.append("\"");
//        sb.append(period);
//        sb.append("\"");
//        if (origin > 0) {
//            sb.append(",");
//            sb.append("\"origin\":");
//            sb.append("\"");
//            sb.append(DateFormatUtils.formatUTC(origin, DateFormatUtils.ISO_DATETIME_FORMAT.getPattern()));
//            sb.append("\"");
//        }
//        if (StringUtils.isNotBlank(timeZone)) {
//            sb.append(",");
//            sb.append("\"timeZone\":");
//            sb.append("\"");
//            sb.append(timeZone);
//            sb.append("\"");
//        }
//        sb.append("}");
//        return sb.toString();
//    }

    if (periodGranularity == null) {
      jsonGenerator.writeNull();
    } else {
      jsonGenerator.writeStartObject();
      jsonGenerator.writeFieldName("type");
      jsonGenerator.writeString("period");
      jsonGenerator.writeStringField("period", periodGranularity.getPeriod());
      if (periodGranularity.getOrigin() > 0) {
        String originStr = DateFormatUtils.formatUTC(
            periodGranularity.getOrigin(),
            DateFormatUtils.ISO_DATETIME_FORMAT.getPattern()
        );
        jsonGenerator.writeStringField("origin", originStr);
      }
      if (StringUtils.isNotBlank(periodGranularity.getTimeZone())) {
        jsonGenerator.writeStringField("timeZone", periodGranularity.getTimeZone());
      }
      jsonGenerator.writeEndObject();
    }
  }
}
