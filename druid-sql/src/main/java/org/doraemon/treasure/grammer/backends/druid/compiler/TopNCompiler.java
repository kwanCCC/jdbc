package org.doraemon.treasure.grammer.backends.druid.compiler;

import java.sql.SQLException;
import java.util.List;

import org.doraemon.treasure.grammer.backends.checker.TopNChecker;
import org.doraemon.treasure.grammer.backends.druid.dsl.dimensions.IDimensionSpec;
import org.doraemon.treasure.grammer.backends.druid.dsl.queries.IQueryObject;
import org.doraemon.treasure.grammer.backends.druid.dsl.queries.TopNQuery;
import org.doraemon.treasure.grammer.backends.druid.util.CompilerUtils;
import org.doraemon.treasure.grammer.parser.Query;

public class TopNCompiler extends IDruidQueryCompiler
{
  @Override
  public boolean canCompile(Query query) throws SQLException
  {
    return TopNChecker.isConditionMet(query);
  }

  @Override
  protected void beforeCompile(CompilerContext context) throws SQLException
  {
    // get order by
    final String orderByMetric = CompilerUtils.getOrderBy4TopN(context);
    final boolean desc = context.getQuery().getOrderBy().get(0).isDesc();
    context.getSession().put("metric", orderByMetric);
    context.getSession().put("isOrderByDesc", desc);
  }

  @Override
  protected IQueryObject compile(CompilerContext context)
  {
    // get group by dimension
    List<IDimensionSpec> groupBys = CompilerUtils.getGroupBys(context);
    final IDimensionSpec groupDim = groupBys.get(0);
    final int limit = context.getQuery().getLimit().getMaxSize();
    TopNQuery.TopNMetricSpec metricSpec = new TopNQuery.TopNMetricSpec();
    String metric = context.getSession().get("metric").toString();
    metricSpec.setMetric(metric);
    Object desc = context.getSession().get("isOrderByDesc");
    if(desc == null || ((desc instanceof Boolean) && !(boolean) desc)) {
        metricSpec.setType(TopNQuery.TopNMetricSpecType.inverted);
    }
    return TopNQuery.builder()
             .dataSource(context.getDataSource())
             .dimension(groupDim)
             .threshold(limit)
             .metric(metricSpec)
             .filter(context.getFilter())
             .aggregations(context.getAggregators().keySet())
             .postAggregations(context.getPostAggregators().keySet())
             .granularity(context.getGranularity())
             .intervals(context.getInterval())
             .build();
  }

}
