package org.doraemon.treasure.grammer.backends.druid.dsl.aggregators;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class HyperUniqueAggregator extends AbstractAggregator
{
  private final String type = "hyperUnique";

  private String name;
  private String fieldName;

}
