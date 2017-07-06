package org.doraemon.treasure.grammer.backends.metricstore.dsl.queries;

import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.Column;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.filters.Filter;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.limits.Limit;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.joda.time.Interval;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SelectQuery implements Query {
    private String type = "select";
    private String       dataSource;
    private Interval     interval;
    private List<Column> columns;
    private Filter       filter;
    private Limit        limit;
    private Boolean      isUnique;
}
