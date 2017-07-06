package org.doraemon.treasure.grammer.backends.druid.compiler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.doraemon.treasure.grammer.mid.model.OrderByOperand;
import org.doraemon.treasure.grammer.mid.model.granularities.SimpleGranularity;
import org.doraemon.treasure.grammer.mid.model.operands.primitive.IntPrimitiveOperand;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.doraemon.treasure.grammer.JSONAssert;
import org.doraemon.treasure.grammer.mid.model.IBooleanExpr;
import org.doraemon.treasure.grammer.mid.model.LimitOperand;
import org.doraemon.treasure.grammer.mid.model.Operand;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprAnd;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprEq;
import org.doraemon.treasure.grammer.mid.model.operands.AliasOperand;
import org.doraemon.treasure.grammer.mid.model.operands.NameOperand;
import org.doraemon.treasure.grammer.mid.model.operands.binary.DivideOperand;
import org.doraemon.treasure.grammer.mid.model.operands.math.SumOperand;
import org.doraemon.treasure.grammer.mid.model.operands.primitive.StringPrimitiveOperand;
import org.doraemon.treasure.grammer.parser.Query;

import junit.framework.Assert;

public class TopNCompilerTest
{

  private TopNCompiler compiler;

  @Before
  public void setUp()
  {
    compiler = new TopNCompiler();
  }

  @Test
  public void testCanCompile() throws SQLException
  {

    // timestamps:
    Calendar cal = Calendar.getInstance();
    cal.set(2015, 1, 20, 20, 20, 20);
    long start = cal.getTimeInMillis();
    cal.set(2015, 2, 20, 20, 20, 20);
    long end = cal.getTimeInMillis();

    Query query = Query.builder()
                       .orderBy(Arrays.asList(Mockito.mock(OrderByOperand.class)))
                       .limit(new LimitOperand(0, 1))
                       .columns(createColumns())
                       .timestamps(Pair.of(start, end))
                       .granularity(new SimpleGranularity("all"))
                       .groupBys(Arrays.asList(Mockito.mock(Operand.class)))
                       .build();
    Assert.assertTrue(compiler.canCompile(query));
  }

  @Test
  public void testCompile() throws SQLException
  {
    Query query = buildQuery();

    Assert.assertTrue(compiler.canCompile(query));
    String json = compiler.compile(query);
    JSONObject jo = JSON.parseObject(json);
    JSONAssert.eq(jo, "queryType", "topN");
    JSONAssert.eq(jo, "dataSource", "metric_table");
    JSONAssert.eq(jo, "threshold", 11);
    JSONAssert.eq(jo, "metric", "avg");
    JSONAssert.eq(jo, "granularity", "all");
    JSONAssert.exists(jo, "dimension");
    JSONAssert.exists(jo, "filter");
    JSONAssert.exists(jo, "aggregations");
    JSONAssert.exists(jo, "postAggregations");
    JSONAssert.exists(jo, "intervals");

  }

  @Test
  public void testCompile_invertedTopN() throws SQLException
  {
    Query query = buildQuery();

    OrderByOperand orderBy = query.getOrderBy().get(0);
    OrderByOperand orderByAsc = new OrderByOperand(orderBy.getOperand(), false);

    Query invertedTopNQuery = Query.builder()
         .orderBy(Collections.singletonList(orderByAsc))
         .limit(query.getLimit())
         .groupBys(query.getGroupBys())
         .granularity(query.getGranularity())
         .columns(query.getColumns())
         .table(query.getTable())
         .timestamps(query.getTimestamps())
         .whereClause(query.getWhereClause())
         .build();

    Assert.assertTrue(compiler.canCompile(invertedTopNQuery));
    String json = compiler.compile(invertedTopNQuery);
    JSONObject jo = JSON.parseObject(json);
    JSONAssert.eq(jo, "queryType", "topN");
    JSONAssert.eq(jo, "dataSource", "metric_table");
    JSONAssert.eq(jo, "threshold", 11);

    JSONObject metricSpec = new JSONObject();
    metricSpec.put("metric", "avg");
    metricSpec.put("type", "inverted");
    JSONAssert.eq(jo, "metric", metricSpec);

    JSONAssert.eq(jo, "granularity", "all");
    JSONAssert.exists(jo, "dimension");
    JSONAssert.exists(jo, "filter");
    JSONAssert.exists(jo, "aggregations");
    JSONAssert.exists(jo, "postAggregations");
    JSONAssert.exists(jo, "intervals");

  }

  private Query buildQuery() {// timestamps:
    Calendar cal = Calendar.getInstance();
    cal.set(2015, 1, 20, 20, 20, 20);
    long start = cal.getTimeInMillis();
    cal.set(2015, 2, 20, 20, 20, 20);
    long end = cal.getTimeInMillis();

    Operand avgOperand = new DivideOperand(
        new SumOperand("longSum", new NameOperand(null, "num1")),
        new SumOperand("longSum", new NameOperand(null, "num2"))
    );
    // selected columns:
    List<Operand> columns = new ArrayList<Operand>();
    columns.add(new AliasOperand(new SumOperand("longSum", new NameOperand(null, "num1")), "num1"));
    columns.add(new AliasOperand(avgOperand, "avg"));

    // order by
    OrderByOperand orderBy = new OrderByOperand(avgOperand, true);
    IBooleanExpr whereClause =
        new BooleanExprAnd(
            new BooleanExprEq(new NameOperand(null, "name"), new StringPrimitiveOperand("oneapm")),
            new BooleanExprEq(
                new NameOperand(null, "type"),
                new IntPrimitiveOperand("10")
            )
        );
    return Query.builder()
         .orderBy(Arrays.asList(orderBy))
         .limit(new LimitOperand(1, 10))
         .groupBys(Arrays.asList((Operand) new NameOperand(null, "id")))
         .granularity(new SimpleGranularity("all"))
         .columns(columns)
         .table("metric_table")
         .timestamps(Pair.of(start, end))
         .whereClause(whereClause)
         .build();
  }

  private List<Operand> createColumns()
  {
    List<Operand> columns = new ArrayList<Operand>();
    columns.add(new AliasOperand(new SumOperand("longSum", new NameOperand(null, "num1")), "num1"));
    return columns;
  }
}
