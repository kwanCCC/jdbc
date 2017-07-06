package org.doraemon.treasure.grammer.backends.druid.dsl.postAggregators;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import org.doraemon.treasure.grammer.backends.druid.dsl.aggregators.IAggregator;

@Data
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"name"}, callSuper = false)
public class ConstPostAggregator extends AbstractPostAggregator {

    private final String type = "constant";
    private String       name;
    private Object       value;

    @Override
    public Set<IAggregator> getAggregators() {
        return new HashSet<IAggregator>();
    }

    @Override
    public String toString() {
        return "constant-" + value;
    }
}
