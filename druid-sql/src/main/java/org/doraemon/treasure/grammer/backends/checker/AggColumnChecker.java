package org.doraemon.treasure.grammer.backends.checker;

import org.doraemon.treasure.grammer.backends.druid.format.aggregators.AggregatorFormat;
import org.doraemon.treasure.grammer.mid.model.Operand;
import org.doraemon.treasure.grammer.parser.Query;

import java.sql.SQLException;


public class AggColumnChecker {

  private AggColumnChecker() {}

  private static final AggregatorFormat aggregatorFormat = new AggregatorFormat();

  protected static Boolean isIncludeAggregation(Query query) throws SQLException
  {

    for (Operand operand : query.getColumns()) {
      if (aggregatorFormat.format(operand) != null) {
        return true;
      }
    }
    return false;
  }
}
