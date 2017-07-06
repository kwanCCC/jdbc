package org.doraemon.treasure.grammer.backends.druid.dsl;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @see http://druid.io/docs/0.8.0/querying/limitspec.html
 * @author zhxiaog
 *
 */
@Data
@Builder
@EqualsAndHashCode
public class DefaultLimitSpec {
    private final String                  type = "default";
    private final int                     limit;
    private final List<OrderByColumnSpec> columns;
}
