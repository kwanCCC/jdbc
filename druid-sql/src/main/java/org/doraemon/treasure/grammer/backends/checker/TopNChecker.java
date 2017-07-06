package org.doraemon.treasure.grammer.backends.checker;

import org.doraemon.treasure.grammer.mid.model.Granularity;
import org.doraemon.treasure.grammer.mid.model.granularities.SimpleGranularity;
import org.doraemon.treasure.grammer.parser.Query;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.sql.SQLException;

public class TopNChecker {

  private TopNChecker() {}

  public static boolean isConditionMet(Query query) throws SQLException {
    return  AggColumnChecker.isIncludeAggregation(query) &&
            limitRequired(query) &&
            onlyOneDimensionRequired(query) &&
            orderByRequired(query) &&
            isAllGranularity(query);
  }

  private static boolean limitRequired(Query query) {
    return query.getLimit() != null &&
           query.getLimit().getOffset() >= 0 &&
            query.getLimit().getResultCount() > 0;
  }

  private static boolean onlyOneDimensionRequired(Query query) {
    return CollectionUtils.isNotEmpty(query.getGroupBys()) &&
            query.getGroupBys().size() == 1;
  }

  /**
   * 业务要求
   * @param query
   * @return
     */
  private static boolean isAllGranularity(Query query) {

    final Granularity granularity = query.getGranularity();
    if (granularity instanceof SimpleGranularity) {
      return ((SimpleGranularity) granularity).getName().equalsIgnoreCase("all");
    }

    final Pair<Long, Long> timestamps = query.getTimestamps();
    return (timestamps.getRight() - timestamps.getLeft()) == granularity.getValue();
  }

  private static boolean orderByRequired(Query query) {
    return CollectionUtils.isNotEmpty(query.getOrderBy());
  }

}
