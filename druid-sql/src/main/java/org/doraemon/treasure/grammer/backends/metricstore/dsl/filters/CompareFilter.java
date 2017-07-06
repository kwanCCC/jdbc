package org.doraemon.treasure.grammer.backends.metricstore.dsl.filters;

import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class CompareFilter implements Filter
{
  private final String type = "compare";
  private final Column column;
  private final Column value;
  private final String operator;
}
