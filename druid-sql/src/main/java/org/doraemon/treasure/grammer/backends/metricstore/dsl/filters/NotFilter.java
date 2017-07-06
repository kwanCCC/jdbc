package org.doraemon.treasure.grammer.backends.metricstore.dsl.filters;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class NotFilter implements Filter {
    private String type = "not";
    private Filter filter;
    
    public NotFilter(Filter filter) {
        this.filter = filter;
    }
}
