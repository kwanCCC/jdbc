package org.doraemon.treasure.grammer.backends.druid.dsl.queries;

import org.doraemon.treasure.grammer.backends.druid.dsl.DefaultLimitSpec;
import org.doraemon.treasure.grammer.backends.druid.dsl.EnumQueryType;
import org.doraemon.treasure.grammer.backends.druid.dsl.aggregators.IAggregator;
import org.doraemon.treasure.grammer.backends.druid.dsl.dimensions.IDimensionSpec;
import org.doraemon.treasure.grammer.backends.druid.dsl.filters.Filter;
import org.doraemon.treasure.grammer.mid.model.Granularity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.joda.time.Interval;

import java.util.Collection;
import java.util.List;

@Data
@EqualsAndHashCode
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupByQuery implements IQueryObject {
    private final EnumQueryType queryType = EnumQueryType.groupBy;
    @NonNull
    private final String               dataSource;
    @NonNull
    private final List<IDimensionSpec> dimensions;
    private final DefaultLimitSpec     limitSpec;

    private final Filter                  filter;
    @NonNull
    private final Granularity             granularity;
    @NonNull
    private final Collection<IAggregator> aggregations;
    private final Collection<IAggregator> postAggregations;
    @NonNull
    private       Interval                intervals;

    // TODO: having

}
