package org.doraemon.treasure.grammer.backends.druid.dsl.postAggregators;

import org.doraemon.treasure.grammer.backends.druid.dsl.aggregators.IAggregator;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"name"}, callSuper = false)
public class ArithMetricPostAggregator extends AbstractPostAggregator {
    private final String                type   = "arithMetric";
    private String                      name;
    @NonNull
    private final String                fn;

    private final List<IPostAggregator> fields = new ArrayList<IPostAggregator>();
    /**
     * sub aggregators
     */
    @NotNull
    private final List<IAggregator> subAggregators;

    @JsonInclude
    public List<IAggregator> getSubAggregators() {
        return subAggregators;
    }

    @Override
    public Set<IAggregator> getAggregators() {
        Set<IAggregator> result = new HashSet<IAggregator>();
        for (IAggregator agg : subAggregators) {
            if (agg.isPostAggregator()) {
                result.addAll(((IPostAggregator) agg).getAggregators());
            } else {
                result.add(agg);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s_%s_%s", subAggregators.get(0), fn, subAggregators.get(1));
    }
}
