package org.doraemon.treasure.grammer.backends.metricstore.dsl.queries;

import org.doraemon.treasure.grammer.backends.metricstore.dsl.limits.Limit;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.Column;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.filters.Filter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.joda.time.Interval;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TopNQuery implements Query {
    private String type = "topn";
    private String       dataSource;
    private Interval     interval;
    private List<Column> columns;
    private Filter       filter;
    private Limit        limit;
    private List<String> groupBys;
}
