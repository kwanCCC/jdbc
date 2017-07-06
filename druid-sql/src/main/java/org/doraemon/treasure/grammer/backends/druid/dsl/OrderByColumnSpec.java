package org.doraemon.treasure.grammer.backends.druid.dsl;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @see http://druid.io/docs/0.8.0/querying/limitspec.html
 * @author zhxiaog
 *
 */
@Data
@AllArgsConstructor
public class OrderByColumnSpec {
    private final String               dimension;
    private final EnumOrderByDirection direction;

    public OrderByColumnSpec(String dimension) {
        this.dimension = dimension;
        this.direction = EnumOrderByDirection.ascending;
    }
}
