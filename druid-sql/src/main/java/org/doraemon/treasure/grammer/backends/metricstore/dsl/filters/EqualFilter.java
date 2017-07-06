package org.doraemon.treasure.grammer.backends.metricstore.dsl.filters;

import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class EqualFilter implements Filter {
    private String type = "equal";
    private Column column;
    private Object value;
    
    public EqualFilter(Column column, Object value) {
        this.column = column;
        this.value = value;
    }
}
