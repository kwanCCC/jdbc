package org.doraemon.treasure.grammer.backends.checker;


import org.doraemon.treasure.grammer.parser.Query;
import org.apache.commons.collections.CollectionUtils;

import java.sql.SQLException;

import static org.doraemon.treasure.grammer.backends.checker.AggColumnChecker.isIncludeAggregation;

public class TimeSeriesChecker {

    private TimeSeriesChecker() {}

    public static Boolean isConditionMet(Query query) throws SQLException {

        return isIncludeAggregation(query) &&
                noLimitNeed(query) &&
                noDimensionNeed(query) &&
                granularityRequired(query);
    }

    private static boolean granularityRequired(Query query) {
        return query.getGranularity() != null;
    }

    private static boolean noDimensionNeed(Query query) {
        return CollectionUtils.isEmpty(query.getGroupBys());
    }

    private static boolean noLimitNeed(Query query) {
        return CollectionUtils.isEmpty(query.getOrderBy()) &&
                query.getLimit() == null;
    }
}
