package org.doraemon.treasure.grammer.backends.metricstore.dsl.columns;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ConstColumn implements Column {
    private String type = "const";
    
    private Object value;
    
    public ConstColumn(Object value) {
        this.value = value;
    }
}
