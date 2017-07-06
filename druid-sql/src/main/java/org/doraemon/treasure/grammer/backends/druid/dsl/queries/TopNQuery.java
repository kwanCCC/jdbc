package org.doraemon.treasure.grammer.backends.druid.dsl.queries;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.doraemon.treasure.grammer.backends.druid.dsl.aggregators.IAggregator;
import org.doraemon.treasure.grammer.backends.druid.dsl.dimensions.IDimensionSpec;
import org.doraemon.treasure.grammer.backends.druid.dsl.filters.Filter;
import org.joda.time.Interval;

import org.doraemon.treasure.grammer.backends.druid.dsl.EnumQueryType;
import org.doraemon.treasure.grammer.mid.model.Granularity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@Builder
@EqualsAndHashCode
public class TopNQuery implements IQueryObject
{
  private final EnumQueryType queryType = EnumQueryType.topN;
  @NonNull
  private final String         dataSource;
  @NonNull
  private final IDimensionSpec dimension;
  private final int            threshold;
  @NonNull
  private final TopNMetricSpec metric;

  private final Filter                  filter;
  @NonNull
  private final Granularity             granularity;
  @NonNull
  private final Collection<IAggregator> aggregations;
  private final Collection<IAggregator> postAggregations;
  @NonNull
  private       Interval                intervals;


  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class TopNMetricSpec {
    @NonNull
    private String metric;
    private TopNMetricSpecType type;

  }

  public enum TopNMetricSpecType {
      numeric, lexicographic, alphaNumeric, inverted;
  }
}
