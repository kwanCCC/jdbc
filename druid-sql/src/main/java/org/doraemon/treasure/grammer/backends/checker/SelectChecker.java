package org.doraemon.treasure.grammer.backends.checker;


import org.doraemon.treasure.grammer.mid.model.granularities.Granularities;
import org.doraemon.treasure.grammer.parser.Query;
import org.apache.commons.collections.CollectionUtils;

public class SelectChecker {

    private SelectChecker() {
    }

    public static boolean isConditionMet(Query query) {
        return CollectionUtils.isEmpty(query.getGroupBys()) &&
                query.getGranularity() == Granularities.ALL;
    }
}
