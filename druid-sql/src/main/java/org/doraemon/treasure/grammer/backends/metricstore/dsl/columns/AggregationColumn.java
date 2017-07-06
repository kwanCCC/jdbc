package org.doraemon.treasure.grammer.backends.metricstore.dsl.columns;

public interface AggregationColumn extends Column {
    Column getInnerColumn();
}
