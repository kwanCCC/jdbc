package org.doraemon.treasure.grammer.backends.metricstore.dsl.limits;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OrderByLimit implements Limit {
    private static final String TYPE = "orderby";

    private int offset = 0;
    private int n = 10;
    private List<OrderBy> orderBys;

    public OrderByLimit(int n, List<OrderBy> orderBys) {
        this.n = n;
        this.orderBys = orderBys;
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
