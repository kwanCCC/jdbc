package org.doraemon.treasure.grammer.backends.druid.dsl.dimensions;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@JsonSerialize()
public class BasicDimensionSpec
{
  private final String name;
}
