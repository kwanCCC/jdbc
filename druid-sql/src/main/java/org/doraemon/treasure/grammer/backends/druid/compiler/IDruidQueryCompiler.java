package org.doraemon.treasure.grammer.backends.druid.compiler;

import org.doraemon.treasure.grammer.ObjectMapperFactory;
import org.doraemon.treasure.grammer.backends.IJSONCompiler;
import org.doraemon.treasure.grammer.backends.druid.dsl.aggregators.HyperUniqueAggregator;
import org.doraemon.treasure.grammer.backends.druid.dsl.postAggregators.HyperUniquePostAggregator;
import org.doraemon.treasure.grammer.backends.druid.format.aggregators.AggregatorFormat;
import org.doraemon.treasure.grammer.backends.druid.format.filter.FilterFormat;
import org.doraemon.treasure.grammer.mid.model.IBooleanExpr;
import org.doraemon.treasure.grammer.backends.druid.dsl.aggregators.IAggregator;
import org.doraemon.treasure.grammer.backends.druid.dsl.filters.Filter;
import org.doraemon.treasure.grammer.mid.model.Granularity;
import org.doraemon.treasure.grammer.backends.druid.dsl.postAggregators.ArithMetricPostAggregator;
import org.doraemon.treasure.grammer.backends.druid.dsl.postAggregators.FieldAccessPostAggregator;
import org.doraemon.treasure.grammer.backends.druid.dsl.postAggregators.IPostAggregator;
import org.doraemon.treasure.grammer.backends.druid.dsl.queries.IQueryObject;
import org.doraemon.treasure.grammer.mid.model.operands.AliasOperand;
import org.doraemon.treasure.grammer.mid.model.operands.NameOperand;
import org.doraemon.treasure.grammer.mid.model.Operand;
import org.doraemon.treasure.grammer.parser.Query;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract class IDruidQueryCompiler implements IJSONCompiler
{

  private static final Logger logger = LoggerFactory.getLogger(IDruidQueryCompiler.class);

  private final FilterFormat filterFormat = new FilterFormat();
  private final AggregatorFormat aggregatorFormat = new AggregatorFormat();

  @Override
  public final String compile(final Query query) throws SQLException
  {

    CompilerContext context = buildContext(query);

    beforeCompile(context);

    compileAlias(context);

    final IQueryObject queryObject = compile(context);

    afterCompile(context, queryObject);

    try {
      return ObjectMapperFactory.getMapper().writeValueAsString(queryObject);
    }
    catch (JsonProcessingException e) {
      String message = "error when serialize query into string: " + e.getLocalizedMessage();
      logger.error(message, e);
      throw new RuntimeException(message, e);
    }
  }

  /**
   * override this method if you want to change compiler context before it actually gets compiled
   *
   * @param context
   */
  protected void beforeCompile(final CompilerContext context) throws SQLException {}

  /**
   * you must override this method to build a query object out of compiler context
   *
   * @param context
   *
   * @return
   */
  protected abstract IQueryObject compile(final CompilerContext context) throws SQLException;

  /**
   * this is the last chance where you can change the final query object or get some compiler
   * context information
   *
   * @param context
   * @param queryObject
   */
  protected void afterCompile(final CompilerContext context, final IQueryObject queryObject) throws SQLException {}


  protected Boolean isIncludeAggregation(Query query) throws SQLException
  {

    for (Operand operand : query.getColumns()) {
      if (aggregatorFormat.format(operand) != null) {
        return true;
      }
    }
    return false;
  }

  /**
   * build a compiler context from query instance
   *
   * @param query
   *
   * @return
   */
  private CompilerContext buildContext(Query query) throws SQLException
  {
    // parse where clause
    Filter filter = null;
    IBooleanExpr booleanExpr = query.getWhereClause();
    if (booleanExpr != null) {
      filter = filterFormat.format(query.getWhereClause());
    }


    List<Operand> dimensions = new ArrayList<Operand>();

    // parse selected columns
    List<Operand> operands = query.getColumns();
    Map<IAggregator, String> postAggregators = new HashMap<IAggregator, String>();
    Map<IAggregator, String> aggregators = new HashMap<IAggregator, String>();
    Collection<IAggregator> aggregatorsList = null;
    for (Operand op : operands) {
      if (isDimensionColumn(op)) {
        dimensions.add(op);
        continue;
      }
      IAggregator agg = aggregatorFormat.format(op);
      if (!agg.isPostAggregator()) {
        aggregatorsList = Arrays.asList(agg);
      } else {
        if (StringUtils.isNotBlank(agg.getName())) {
          postAggregators.put(agg, agg.getName());
        } else {
          if (postAggregators.get(agg) == null) {
            postAggregators.put(agg, agg.toString());
          }
        }
        aggregatorsList = ((IPostAggregator) agg).getAggregators();
      }
      if (CollectionUtils.isNotEmpty(aggregatorsList)) {
        for (IAggregator a : aggregatorsList) {
          if (StringUtils.isNotBlank(a.getName())) {
            aggregators.put(a, a.getName());
          } else {
            if (aggregators.get(a) == null) {
              aggregators.put(a, a.toString());
            }
          }
        }
      }
    }


    // get interval and granularity
    Interval interval = new Interval(query.getTimestamps().getLeft(), query.getTimestamps().getRight());
    Granularity granularity = query.getGranularity();

    // build compiler context
    CompilerContext context = CompilerContext.builder()
                                             .query(query)
                                             .dataSource(query.getTable())
                                             .aggregators(aggregators)
                                             .postAggregators(postAggregators)
                                             .interval(interval)
                                             .granularity(granularity)
                                             .filter(filter)
                                             .dimensionColumns(dimensions)
                                             .build();
    return context;
  }

  private boolean isDimensionColumn(Operand operand)
  {
    if (NameOperand.class.isInstance(operand)) {
      return true;
    } else if (AliasOperand.class.isInstance(operand)) {
      return isDimensionColumn(((AliasOperand) operand).getOperand());
    }
    return false;
  }

  /**
   * this code does: <br>
   * 1. replace aggregations in postAggregations with fieldAccessPostAggregation <br>
   * 2. set aggregation alias if not set before
   *
   * @param context
   */
  private void compileAlias(CompilerContext context)
  {
    for (IAggregator agg : context.getPostAggregators().keySet()) {
      if (agg.isPostAggregator()) {
        replaceAggregatorWithFieldAccess((IPostAggregator) agg, context.getAggregators());
        addHyperUniqueAggregatorWhenHyperPostAgg((IPostAggregator) agg, context.getAggregators());
      }
    }

    for (Entry<IAggregator, String> entry : context.getAggregators().entrySet()) {
      entry.getKey().setName(entry.getValue());
    }
  }

  /**
   * replace aggregations in postAggregations with fieldAccessPostAggregation <br>
   *
   * @param postAgg
   * @param aggMap
   */
  private void replaceAggregatorWithFieldAccess(IPostAggregator postAgg, final Map<IAggregator, String> aggMap)
  {
    if (ArithMetricPostAggregator.class.isInstance(postAgg)) {
      ArithMetricPostAggregator aagg = (ArithMetricPostAggregator) postAgg;
      Collection<IPostAggregator> postAggs = new ArrayList<IPostAggregator>();
      for (IAggregator agg : aagg.getSubAggregators()) {
        if (agg.isPostAggregator()) {
          replaceAggregatorWithFieldAccess((IPostAggregator) agg, aggMap);
          postAggs.add((IPostAggregator) agg);
        } else {
          postAggs.add(new FieldAccessPostAggregator(aggMap.get(agg)));
        }
      }
      aagg.getFields().addAll(postAggs);
    }
  }

  private void addHyperUniqueAggregatorWhenHyperPostAgg(IPostAggregator postAgg, final Map<IAggregator, String> aggMap)
  {
    if (HyperUniquePostAggregator.class.isInstance(postAgg)) {
      HyperUniquePostAggregator hyperPostAgg = (HyperUniquePostAggregator) postAgg;
      String randomName = "hyper_unique_" + hyperPostAgg.getFieldName();
      aggMap.put(new HyperUniqueAggregator(randomName, hyperPostAgg.getFieldName()), randomName);
      hyperPostAgg.setFieldName(randomName);
    }
  }
}
