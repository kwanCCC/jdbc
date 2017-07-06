package org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.aggregations;

import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.AggregationColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SumColumn implements AggregationColumn {
    private String type = "sum";
    private Column innerColumn;
    
    public SumColumn(Column innerColumn) {
        this.innerColumn = innerColumn;
    }
}
