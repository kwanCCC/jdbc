package org.doraemon.treasure.grammer.backends.druid.dsl.dimensions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@AllArgsConstructor
@EqualsAndHashCode
public class DefaultDimensionSpec implements IDimensionSpec {
    private final String type = "default";
    private final String dimension;
    private final String outputName;
}
