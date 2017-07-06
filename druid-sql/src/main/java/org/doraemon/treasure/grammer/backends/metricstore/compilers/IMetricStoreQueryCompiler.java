package org.doraemon.treasure.grammer.backends.metricstore.compilers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.Column;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.filters.Filter;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.limits.Limit;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.queries.Query;
import org.doraemon.treasure.grammer.backends.metricstore.format.columns.ColumnFormat;
import org.doraemon.treasure.grammer.backends.metricstore.format.filter.FilterFormat;
import org.doraemon.treasure.grammer.mid.model.OrderByOperand;
import org.joda.time.Interval;

import org.doraemon.treasure.grammer.backends.IJSONCompiler;
import org.doraemon.treasure.grammer.backends.druid.format.aggregators.AggregatorFormat;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.limits.OrderBy;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.limits.OrderByLimit;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.limits.SimpleLimit;
import org.doraemon.treasure.grammer.backends.metricstore.util.JsonHelper;
import org.doraemon.treasure.grammer.mid.model.Granularity;
import org.doraemon.treasure.grammer.mid.model.IBooleanExpr;
import org.doraemon.treasure.grammer.mid.model.LimitOperand;
import org.doraemon.treasure.grammer.mid.model.Operand;
import org.doraemon.treasure.grammer.mid.model.granularities.Granularities;
import org.doraemon.treasure.grammer.mid.model.operands.NameOperand;

public abstract class IMetricStoreQueryCompiler<T extends Query>
    implements IJSONCompiler
{

  private final ColumnFormat     columnFormat     = new ColumnFormat();
  private final FilterFormat     filterFormat     = new FilterFormat();
  private final AggregatorFormat aggregatorFormat = new AggregatorFormat();

  @Override
  public String compile(org.doraemon.treasure.grammer.parser.Query query) throws SQLException
  {
    final T value = buildConcreteQuery(beforeCompile(query));
    return JsonHelper.toJson(value);
  }

  // do something before building a metric store query
  protected org.doraemon.treasure.grammer.parser.Query beforeCompile(org.doraemon.treasure.grammer.parser.Query query) {

    // set default granularity when no granularity specified
    org.doraemon.treasure.grammer.parser.Query newQuery = query;
    if(query.getGranularity() == null) {
      newQuery = org.doraemon.treasure.grammer.parser.Query.builder()
                                                           .isUnique(query.getIsUnique())
                                                           .table(query.getTable())
                                                           .columns(query.getColumns())
                                                           .whereClause(query.getWhereClause())
                                                           .orderBy(query.getOrderBy())
                                                           .groupBys(query.getGroupBys())
                                                           .timestamps(query.getTimestamps())
                                                           .limit(query.getLimit())
                                                           .granularity(Granularities.ALL)
                                                           .build();
    }

    return fixTimeInterval(newQuery);
  }

  // extend the time interval when it can't be divided by granularity exactly
  private org.doraemon.treasure.grammer.parser.Query fixTimeInterval(org.doraemon.treasure.grammer.parser.Query query) {
    Granularity granularity = query.getGranularity();
    long startTime = query.getTimestamps().getLeft();
    long endTime = query.getTimestamps().getRight();

    Pair<Long, Long> timestamps;
    if (Granularities.ALL.equals(granularity)) {
      timestamps = Pair.of(startTime, endTime);
    } else {
      long granularityMillis = granularity.getValue();
      boolean divisible = (endTime - startTime) % granularityMillis == 0;
      int points = (int) ((endTime - startTime) / granularityMillis);
      if(divisible) {
        timestamps = Pair.of(startTime, endTime);
      } else {
        timestamps = Pair.of(startTime, startTime + granularityMillis * (points + 1));
      }

    }
    return org.doraemon.treasure.grammer.parser.Query.builder()
                                                     .isUnique(query.getIsUnique())
                                                     .table(query.getTable())
                                                     .columns(query.getColumns())
                                                     .whereClause(query.getWhereClause())
                                                     .orderBy(query.getOrderBy())
                                                     .groupBys(query.getGroupBys())
                                                     .timestamps(timestamps)
                                                     .limit(query.getLimit())
                                                     .granularity(query.getGranularity())
                                                     .build();
  }

  protected abstract T buildConcreteQuery(org.doraemon.treasure.grammer.parser.Query query) throws SQLException;

  protected List<Column> convertOperandsToColumns(List<Operand> columns) throws SQLException
  {
    List<Column> result = new ArrayList<>();
    for (Operand column : columns) {
      result.add(columnFormat.format(column));
    }
    return result;
  }

  protected Filter convertBooleanExprToFilter(IBooleanExpr booleanExpr) throws SQLException
  {
    if (booleanExpr == null) {
      return null;
    }
    return filterFormat.format(booleanExpr);
  }

  protected Limit convertLimit(LimitOperand limit, List<OrderByOperand> orderBy, List<Operand> columns) throws SQLException
  {
    if (limit == null) {
      return null;
    }
    if (CollectionUtils.isEmpty(orderBy)) {
      return new SimpleLimit(limit.getOffset(), limit.getResultCount());
    } else {

      final List<String> aliasNames = columnFormat.getAliasNames(columns);
      List<OrderBy> orderBys = new ArrayList<>();
      for (OrderByOperand orderByOperand : orderBy) {
        Column column = columnFormat.formatWithRefColumn(aliasNames, orderByOperand.getOperand());
        orderBys.add(new OrderBy(column, orderByOperand.isDesc()));
      }
      return new OrderByLimit(limit.getOffset(), limit.getResultCount(), orderBys);
    }
  }

  //TODO: support more operand in groupBys
  protected List<String> convertGroupBys(List<Operand> groupBys)
  {
    List<String> result = new ArrayList<>();
    for (int i = 0; i < groupBys.size(); i++) {
      Operand operand = groupBys.get(i);
      if (operand instanceof NameOperand) {
        result.add(((NameOperand) operand).getColumn());
      } else {
        throw new RuntimeException("unsupported group by operand: " + operand);
      }
    }
    return result;
  }

  protected Boolean isIncludeAggregation(org.doraemon.treasure.grammer.parser.Query query) throws SQLException
  {

    for (Operand operand : query.getColumns()) {
      if (aggregatorFormat.format(operand) != null) {
        return true;
      }
    }
    return false;
  }

  protected int buildPoints(Interval interval, Granularity granularity) {
    if (Granularities.ALL.equals(granularity)) {
      return 1;
    } else {
      return (int) (interval.toDurationMillis() / granularity.getValue());
    }
  }

}
