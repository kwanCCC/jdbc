package org.doraemon.treasure.grammer.backends.druid.dsl.filters;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class NotFilter implements Filter {
    private final String type = "not";
    private final Filter field;
}
