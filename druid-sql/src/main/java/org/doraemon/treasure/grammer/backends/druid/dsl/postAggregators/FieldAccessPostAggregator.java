package org.doraemon.treasure.grammer.backends.druid.dsl.postAggregators;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import org.doraemon.treasure.grammer.backends.druid.dsl.aggregators.IAggregator;

@Data
@EqualsAndHashCode(exclude = {"name"}, callSuper = false)
public class FieldAccessPostAggregator extends AbstractPostAggregator {
    private final String type = "fieldAccess";
    @NonNull
    private final String fieldName;
    private String       name = null;

    public FieldAccessPostAggregator(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public Set<IAggregator> getAggregators() {
        return new HashSet<IAggregator>();
    }

    @Override
    public String toString() {
        return fieldName;
    }
}
