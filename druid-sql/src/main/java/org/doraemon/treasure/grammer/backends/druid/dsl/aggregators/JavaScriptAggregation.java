package org.doraemon.treasure.grammer.backends.druid.dsl.aggregators;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class JavaScriptAggregation implements IAggregator
{
  private final String type = "javascript";

  @Setter
  private String name;

  private final List<String> fieldNames;
  private final String fnReset;
  private final String fnCombine;
  private final String fnAggregate;

  @Override
  public boolean isPostAggregator()
  {
    return false;
  }
}
