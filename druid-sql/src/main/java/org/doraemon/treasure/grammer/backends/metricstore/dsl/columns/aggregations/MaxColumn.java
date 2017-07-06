package org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.aggregations;

import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.AggregationColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class MaxColumn implements AggregationColumn {
    private String type = "max";
    private Column innerColumn;
    
    public MaxColumn(Column innerColumn) {
        this.innerColumn = innerColumn;
    }
}
