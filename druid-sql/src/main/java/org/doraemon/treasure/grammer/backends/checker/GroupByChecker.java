package org.doraemon.treasure.grammer.backends.checker;


import org.doraemon.treasure.grammer.parser.Query;
import org.apache.commons.collections.CollectionUtils;

import java.sql.SQLException;

import static org.doraemon.treasure.grammer.backends.checker.AggColumnChecker.isIncludeAggregation;

public class GroupByChecker {

    private  GroupByChecker() {}

    public static boolean isConditionMet(Query query) throws SQLException {
        return isIncludeAggregation(query) &&
                dimensionRequired(query);
    }

    private static boolean dimensionRequired(Query query) {
        return CollectionUtils.isNotEmpty(query.getGroupBys());
    }

}
