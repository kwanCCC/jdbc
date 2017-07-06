package org.doraemon.treasure.grammer.backends.metricstore.dsl.columns;

import java.util.List;

public interface ArithMetricColumn extends Column {
    List<Column> getInnerColumns();
}
