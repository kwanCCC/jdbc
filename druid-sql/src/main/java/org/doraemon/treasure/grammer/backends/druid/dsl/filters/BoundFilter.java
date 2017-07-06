package org.doraemon.treasure.grammer.backends.druid.dsl.filters;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class BoundFilter implements Filter
{
  private final String type = "bound";
  private final String dimension;
  private final String lower;
  private final Boolean lowerStrict;
  private final String upper;
  private final Boolean upperStrict;
  private final Boolean alphaNumeric;
}
