package org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.primitives;

import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.PrimitiveColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class StringColumn implements PrimitiveColumn {
    private String type = "string";
    private String field;
    
    public StringColumn(String field) {
        this.field = field;
    }
}
