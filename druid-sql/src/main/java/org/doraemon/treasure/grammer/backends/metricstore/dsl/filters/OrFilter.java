package org.doraemon.treasure.grammer.backends.metricstore.dsl.filters;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OrFilter implements Filter {
    private String type = "or";
    private List<Filter> filters;
    
    public OrFilter(List<Filter> filters) {
        this.filters = filters;
    }
    
}
