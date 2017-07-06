package org.doraemon.treasure.grammer.backends.druid.dsl.queries;

import java.util.Collection;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import org.doraemon.treasure.grammer.backends.druid.dsl.EnumQueryType;
import org.doraemon.treasure.grammer.backends.druid.dsl.aggregators.IAggregator;
import org.doraemon.treasure.grammer.backends.druid.dsl.filters.Filter;
import org.doraemon.treasure.grammer.mid.model.Granularity;
import org.joda.time.Interval;

@Data
@Builder
@EqualsAndHashCode
public class TimeSeriesQuery implements IQueryObject
{

  private final EnumQueryType queryType = EnumQueryType.timeseries;
  @NonNull
  private final String dataSource;

  private final Filter                  filter;
  @NonNull
  private final Granularity             granularity;
  @NonNull
  private final Collection<IAggregator> aggregations;
  private final Collection<IAggregator> postAggregations;
  @NonNull
  private       Interval                intervals;
}
