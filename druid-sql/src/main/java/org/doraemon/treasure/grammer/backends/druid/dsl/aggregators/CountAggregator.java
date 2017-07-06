package org.doraemon.treasure.grammer.backends.druid.dsl.aggregators;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"name"}, callSuper = false)
public class CountAggregator extends AbstractAggregator {
    private final String type = "count";
    private String       name;

    @Override
    public String toString() {
        return type;
    }
}
