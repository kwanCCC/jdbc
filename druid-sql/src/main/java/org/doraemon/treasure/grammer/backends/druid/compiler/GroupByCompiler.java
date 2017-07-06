package org.doraemon.treasure.grammer.backends.druid.compiler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.doraemon.treasure.grammer.backends.checker.GroupByChecker;
import org.doraemon.treasure.grammer.backends.druid.dsl.aggregators.IAggregator;
import org.doraemon.treasure.grammer.backends.druid.dsl.dimensions.DefaultDimensionSpec;
import org.doraemon.treasure.grammer.backends.druid.dsl.dimensions.IDimensionSpec;
import org.doraemon.treasure.grammer.backends.druid.util.CompilerUtils;
import org.doraemon.treasure.grammer.mid.model.Operand;
import org.doraemon.treasure.grammer.mid.model.granularities.Granularities;

import org.doraemon.treasure.grammer.backends.druid.dsl.DefaultLimitSpec;
import org.doraemon.treasure.grammer.backends.druid.dsl.queries.GroupByQuery;
import org.doraemon.treasure.grammer.backends.druid.dsl.queries.IQueryObject;
import org.doraemon.treasure.grammer.mid.model.operands.AliasOperand;
import org.doraemon.treasure.grammer.mid.model.operands.NameOperand;
import org.doraemon.treasure.grammer.parser.Query;
import com.google.common.collect.Lists;

public class GroupByCompiler extends IDruidQueryCompiler
{

  @Override
  public boolean canCompile(Query query) throws SQLException
  {
    return GroupByChecker.isConditionMet(query) || isDistinctQuery(query);
  }

  private boolean isDistinctQuery(Query query) {
    return query.getIsUnique() != null && query.getIsUnique();
  }

  @Override
  protected void beforeCompile(CompilerContext context) throws SQLException
  {
    DefaultLimitSpec limitSpec = CompilerUtils.getLimitSpec4groupBy(context);
    context.getSession().put("limit", limitSpec);

    // use "BREAK BY ALL" as default granularity
    if(context.getGranularity() == null) {
      context.setGranularity(Granularities.ALL);
    }
  }

  @Override
  protected IQueryObject compile(final CompilerContext context)
  {
    // get group by dimension
    if (isDistinctQuery(context.getQuery())) {
      return buildDistinctQuery(context);
    } else {
      return buildNormalGroupByQuery(context);
    }
  }

  private IQueryObject buildDistinctQuery(CompilerContext context) {
    return GroupByQuery.builder()
            .dataSource(context.getDataSource())
            .dimensions(getDimensions(context))
            .filter(context.getFilter())
            .granularity(Granularities.NONE)
            .aggregations(Lists.<IAggregator>newArrayList())
            .intervals(context.getInterval())
            .build();
  }

  private List<IDimensionSpec> getDimensions(CompilerContext context) {
    final List<Operand> dimensionColumns = context.getDimensionColumns();
    final ArrayList<IDimensionSpec> dims = new ArrayList<>();
    for (Operand operand : dimensionColumns) {
      dims.add(toIDimensionSpec(operand));
    }

    return dims;
  }

  private IDimensionSpec toIDimensionSpec(Operand operand) {
    if (AliasOperand.class.isInstance(operand)) {
      // 存在别名
      AliasOperand alias = (AliasOperand) operand;
      final Operand innerOperand = alias.getOperand();
      if (!NameOperand.class.isInstance(innerOperand)) {
        throw new IllegalArgumentException("select 查询中" + operand + "不合法， 格式如下：select distinct a as b from table ...");
      }
      return new DefaultDimensionSpec(((NameOperand) innerOperand).getColumn(), alias.getAlias());
    } else if (NameOperand.class.isInstance(operand)) {
      NameOperand namedOperand = (NameOperand) operand;
      return new DefaultDimensionSpec(namedOperand.getColumn(), namedOperand.getColumn());
    }
    throw new IllegalArgumentException("select 查询中" + operand + "不合法， 格式如下：select distinct a as b from table ...");
  }

  private IQueryObject buildNormalGroupByQuery(CompilerContext context) {
    DefaultLimitSpec limitSpec = (DefaultLimitSpec) context.getSession().get("limit");
    return GroupByQuery.builder()
            .dataSource(context.getDataSource())
            .dimensions(CompilerUtils.getGroupBys(context))
            .limitSpec(limitSpec)
            .filter(context.getFilter())
            .aggregations(context.getAggregators().keySet())
            .postAggregations(context.getPostAggregators().keySet())
            .granularity(context.getGranularity())
            .intervals(context.getInterval())
            .build();
  }


}
