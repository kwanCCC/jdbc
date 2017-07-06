package org.doraemon.treasure.grammer.backends.druid.util;

import org.doraemon.treasure.grammer.backends.druid.compiler.CompilerContext;
import org.doraemon.treasure.grammer.backends.druid.dsl.DefaultLimitSpec;
import org.doraemon.treasure.grammer.backends.druid.dsl.EnumOrderByDirection;
import org.doraemon.treasure.grammer.backends.druid.dsl.OrderByColumnSpec;
import org.doraemon.treasure.grammer.backends.druid.dsl.aggregators.IAggregator;
import org.doraemon.treasure.grammer.backends.druid.dsl.dimensions.DefaultDimensionSpec;
import org.doraemon.treasure.grammer.backends.druid.dsl.dimensions.IDimensionSpec;
import org.doraemon.treasure.grammer.backends.druid.dsl.postAggregators.IPostAggregator;
import org.doraemon.treasure.grammer.backends.druid.format.aggregators.AggregatorFormat;
import org.doraemon.treasure.grammer.mid.model.operands.AliasOperand;
import org.doraemon.treasure.grammer.mid.model.LimitOperand;
import org.doraemon.treasure.grammer.mid.model.operands.NameOperand;
import org.doraemon.treasure.grammer.mid.model.Operand;
import org.doraemon.treasure.grammer.mid.model.OrderByOperand;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CompilerUtils
{
  private static final AggregatorFormat aggregatorFormat = new AggregatorFormat();

  /**
   * @param context
   *
   * @return
   */
  public static List<IDimensionSpec> getGroupBys(CompilerContext context)
  {
    List<IDimensionSpec> result = new ArrayList<IDimensionSpec>();
    for (Operand op : context.getQuery().getGroupBys()) {
      if (NameOperand.class.isInstance(op)) {
        NameOperand nop = (NameOperand) op;
        result.add(getGroupBy(nop, context.getDimensionColumns()));
      }
    }
    return result;
  }

  /**
   * @return
   */
  public static List<IDimensionSpec> getSelectDims(CompilerContext context)
  {
    List<IDimensionSpec> result = new ArrayList<IDimensionSpec>();
    for (Operand column : context.getQuery().getColumns()) {
      if (NameOperand.class.isInstance(column)) {
        NameOperand col = (NameOperand) column;
        final String colName = col.getColumn();
        result.add(new DefaultDimensionSpec(colName, colName));
      } else if (AliasOperand.class.isInstance(column)) {
        AliasOperand aliasColumn = (AliasOperand) column;
        NameOperand col = (NameOperand) aliasColumn.getOperand();
        result.add(new DefaultDimensionSpec(col.getColumn(), aliasColumn.getAlias()));
      }
    }
    return result;
  }


  private static IDimensionSpec getGroupBy(NameOperand nameOperand, List<Operand> dimensionColumn)
  {
    for (Operand operand : dimensionColumn) {
      if (isGroupEq(nameOperand, operand)) {
        if (AliasOperand.class.isInstance(operand)) {
          // 存在别名
          AliasOperand alias = (AliasOperand) operand;
          return new DefaultDimensionSpec(nameOperand.getColumn(), alias.getAlias());
        }
        break;
      }
    }
    return new DefaultDimensionSpec(nameOperand.getColumn(), nameOperand.getColumn());
  }

  private static boolean isGroupEq(Operand left, Operand right)
  {
    if (AliasOperand.class.isInstance(left)) {
      return isGroupEq(((AliasOperand) left).getOperand(), right);
    } else if (AliasOperand.class.isInstance(right)) {
      return isGroupEq(left, ((AliasOperand) right).getOperand());
    } else if (NameOperand.class.isInstance(left) && NameOperand.class.isInstance(right)) {
      return ((NameOperand) left).getColumn().equals(((NameOperand) right).getColumn());
    }
    return false;
  }

  public static DefaultLimitSpec getLimitSpec4groupBy(CompilerContext context) throws SQLException
  {
    LimitOperand limit = context.getQuery().getLimit();
    if (limit == null) {
      return null;
    }

    List<OrderByColumnSpec> orderByColumns = new ArrayList<OrderByColumnSpec>();
    List<Pair<String, Boolean>> orderBys = getOrderBys(context);
    for (Pair<String, Boolean> orderBy : orderBys) {
      orderByColumns.add(new OrderByColumnSpec(orderBy.getLeft(), EnumOrderByDirection.get(orderBy.getRight())));
    }
    return DefaultLimitSpec.builder().limit(limit.getMaxSize()).columns(orderByColumns).build();
  }

  private static List<Pair<String, Boolean>> getOrderBys(CompilerContext context) throws SQLException
  {
    List<Pair<String, Boolean>> orderBys = new ArrayList<Pair<String, Boolean>>();
    for (OrderByOperand op : context.getQuery().getOrderBy()) {
      Pair<String, Boolean> orderBy = Pair.of(
          getOrderBy(op, context.getAggregators(), context.getPostAggregators()),
          op.isDesc()
      );
      if (StringUtils.isNotBlank(orderBy.getLeft())) {
        orderBys.add(orderBy);
      }
    }
    return orderBys;
  }

  private static String getOrderBy(
      final OrderByOperand orderByOpr,
      final Map<IAggregator, String> aggsMap,
      final Map<IAggregator, String> postAggsMap
  ) throws SQLException
  {
    String result = null;
    Operand operand = orderByOpr.getOperand();
    if (NameOperand.class.isInstance(operand)) {
      result = ((NameOperand) operand).getColumn();
    } else {
      IAggregator agg = aggregatorFormat.format(operand);
      result = aggsMap.get(agg);
      if (result == null) {
        result = postAggsMap.get(agg);
      }
      if (StringUtils.isBlank(result)) {
        result = agg.toString();
        if (agg.isPostAggregator()) {
          String previous;
          if ((previous = postAggsMap.get(agg)) == null) {
            previous = postAggsMap.put(agg, agg.toString());
          }
          if (previous == null) {
            agg.setName(agg.toString());
            // we have to parse postAggregation see if there is any aggregation need to
            // add to aggregations map
            for (IAggregator a : ((IPostAggregator) agg).getAggregators()) {
              if (StringUtils.isNotBlank(a.getName())) {
                aggsMap.put(a, a.getName());
              } else {
                if (aggsMap.get(a) == null) {
                  aggsMap.put(a, a.toString());
                }

              }
            }
          }
        } else {
          if (aggsMap.get(agg) == null) {
            aggsMap.put(agg, agg.toString());
          }
        }
      }
    }
    return result;
  }

  /**
   * get order by metric name<br>
   * if order by is a nameOperand, in which case can be treated as a field accessor, return
   * nameOperand's column name<br>
   * if order by is not a nameOperand, we need to parse the aggregations and add them to the
   * aggregations map and post aggregation map.
   *
   * @param context
   * @return
   */
  public static String getOrderBy4TopN(CompilerContext context) throws SQLException
  {
    return getOrderBy(context.getQuery().getOrderBy().get(0), context.getAggregators(), context.getPostAggregators());
  }


}
