package org.doraemon.treasure.grammer.backends.druid.dsl.queries;

import org.doraemon.treasure.grammer.backends.druid.dsl.EnumQueryType;
import org.doraemon.treasure.grammer.backends.druid.dsl.dimensions.IDimensionSpec;
import org.doraemon.treasure.grammer.backends.druid.dsl.filters.Filter;
import org.doraemon.treasure.grammer.mid.model.Granularity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.joda.time.Interval;

import java.util.List;

@Data
@Builder
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SelectQuery implements IQueryObject
{

  private final EnumQueryType queryType = EnumQueryType.select;

  @NonNull
  private final String dataSource;

  @NonNull
  private Interval intervals;

  private boolean descending;

  private final Filter filter;

  private final List<IDimensionSpec> dimensions;

  private List<String> metrics;

  @NonNull
  private PagingSpec pagingSpec;

  private final Granularity granularity;
}

