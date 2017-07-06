package org.doraemon.treasure.grammer.backends.druid.dsl.filters;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class SelectorFilter implements Filter {
    private final String type = "selector";
    private final String dimension;
    private final String value;
}
