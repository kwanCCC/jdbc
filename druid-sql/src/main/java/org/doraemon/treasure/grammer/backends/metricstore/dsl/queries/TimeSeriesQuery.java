package org.doraemon.treasure.grammer.backends.metricstore.dsl.queries;

import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.Column;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.filters.Filter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.Interval;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeSeriesQuery implements Query {
    private String type = "timeseries";
    private String       dataSource;
    private Interval     interval;
    private List<Column> columns;
    private Filter       filter;
    private int          points;
}
