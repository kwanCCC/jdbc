package org.doraemon.treasure.grammer.query;

import org.doraemon.treasure.grammer.backends.druid.compiler.GroupByCompiler;
import org.doraemon.treasure.grammer.backends.druid.compiler.TimeSeriesCompiler;
import org.doraemon.treasure.grammer.backends.druid.compiler.TopNCompiler;

public interface QueryProperties {
    String             URL          = "url";
    String             CONTENT_TYPE = "content-type";

    TimeSeriesCompiler timeSeries = new TimeSeriesCompiler();
    TopNCompiler       topn       = new TopNCompiler();
    GroupByCompiler    groupBy    = new GroupByCompiler();
}
