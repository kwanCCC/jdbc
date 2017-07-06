package org.doraemon.treasure.grammer.backends.metricstore.util;

import org.doraemon.treasure.grammer.ObjectMapperFactory;
import com.fasterxml.jackson.core.JsonProcessingException;

public class JsonHelper
{

  private JsonHelper() {}

  public static String toJson(Object obj)
  {
    try {
      return ObjectMapperFactory.getMapper().writeValueAsString(obj);
    }
    catch (JsonProcessingException e) {
      String message = "error when serialize query into string: " + e.getLocalizedMessage();
      throw new RuntimeException(message, e);
    }
  }
}
