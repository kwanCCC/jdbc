package org.doraemon.treasure.grammer.backends.druid.dsl.postAggregators;


import org.doraemon.treasure.grammer.backends.druid.dsl.aggregators.IAggregator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class HyperUniquePostAggregator extends AbstractPostAggregator
{
  private final String type = "hyperUniqueCardinality";

  private String name;
  private String fieldName;

  @Override
  public Set<IAggregator> getAggregators()
  {
    return new HashSet<IAggregator>();
  }
}
