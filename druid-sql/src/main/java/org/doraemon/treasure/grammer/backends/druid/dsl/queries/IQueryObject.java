package org.doraemon.treasure.grammer.backends.druid.dsl.queries;

import org.doraemon.treasure.grammer.backends.druid.dsl.EnumQueryType;


public interface IQueryObject {

    /**
     * query type, only topn, group by and timeseries supported.
     */
    EnumQueryType getQueryType();

    /**
     * druid data source name
     */
    String getDataSource();

}
