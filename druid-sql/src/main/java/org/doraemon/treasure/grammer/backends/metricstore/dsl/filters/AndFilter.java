package org.doraemon.treasure.grammer.backends.metricstore.dsl.filters;

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
public class AndFilter implements Filter {
    String type = "and";
    List<Filter> filters;
    
    public AndFilter(List<Filter> filters) {
        this.filters = filters;
    }
}
