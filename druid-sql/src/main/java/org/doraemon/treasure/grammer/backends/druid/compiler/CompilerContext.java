package org.doraemon.treasure.grammer.backends.druid.compiler;

import org.doraemon.treasure.grammer.backends.druid.dsl.aggregators.IAggregator;
import org.doraemon.treasure.grammer.backends.druid.dsl.filters.Filter;
import org.doraemon.treasure.grammer.mid.model.Granularity;
import org.doraemon.treasure.grammer.mid.model.Operand;
import org.doraemon.treasure.grammer.parser.Query;
import lombok.Builder;
import lombok.Data;
import org.joda.time.Interval;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class CompilerContext {
    private Query                    query;
    private String                   dataSource;
    private Filter                   filter;
    private Granularity              granularity;
    private Map<IAggregator, String> aggregators;
    private Map<IAggregator, String> postAggregators;
    private List<Operand>            dimensionColumns;
    private Interval                 interval;
    
    private final Map<String, Object> session = new HashMap<String, Object>();
}
