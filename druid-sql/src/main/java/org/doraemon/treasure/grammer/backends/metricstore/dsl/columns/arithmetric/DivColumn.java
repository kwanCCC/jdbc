package org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.arithmetric;

import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.ArithMetricColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class DivColumn implements ArithMetricColumn {
    private String type = "div";
    private List<Column> innerColumns;
    
    public DivColumn(List<Column> innerColumns) {
        this.innerColumns = innerColumns;
    }
}
