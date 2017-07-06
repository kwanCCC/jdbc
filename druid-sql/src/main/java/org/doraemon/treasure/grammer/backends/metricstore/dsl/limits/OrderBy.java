package org.doraemon.treasure.grammer.backends.metricstore.dsl.limits;

import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class OrderBy {
    private Column column;
    private boolean desc = true;
    
    public OrderBy(Column column) {
        this.column = column;
    }
    
    public OrderBy(Column column, boolean desc) {
        this.column = column;
        this.desc = desc;
    }
}
