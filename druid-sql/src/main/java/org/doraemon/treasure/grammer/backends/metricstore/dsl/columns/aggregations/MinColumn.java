package org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.aggregations;

import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.AggregationColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MinColumn implements AggregationColumn {
    private String type = "min";
    private Column innerColumn;
    
    public MinColumn(Column innerColumn) {
        this.innerColumn = innerColumn;
    }
}
