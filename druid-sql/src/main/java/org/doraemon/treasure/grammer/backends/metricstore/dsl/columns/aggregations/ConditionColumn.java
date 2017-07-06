package org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.aggregations;

import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.AggregationColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.Column;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.filters.Filter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class ConditionColumn implements AggregationColumn
{
  private final String type = "condition";
  private final Filter condition;
  private final Column innerColumn;
}
