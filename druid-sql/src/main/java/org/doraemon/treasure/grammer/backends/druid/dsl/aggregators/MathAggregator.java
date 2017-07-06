package org.doraemon.treasure.grammer.backends.druid.dsl.aggregators;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"name"}, callSuper = false)
public class MathAggregator extends AbstractAggregator {
    @NonNull
    private final String type;
    private String       name;
    @NonNull
    private final String fieldName;

    @Override
    public String toString() {
        return String.format("%s_%s", type, fieldName);
    }
}
