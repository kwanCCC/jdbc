package org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.others;

import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@AllArgsConstructor
@EqualsAndHashCode
public class DistinctCountColumn implements Column
{
  private final String type = "distinctCount";
  private final Column innerColumn;

}
