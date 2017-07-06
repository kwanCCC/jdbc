package org.doraemon.treasure.grammer.backends.metricstore.dsl.limits;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SimpleLimit implements Limit {
    private static final String TYPE = "simple";

    private int offset;
    private int n;
    
    public SimpleLimit(int n) {
        this.n = n;
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
