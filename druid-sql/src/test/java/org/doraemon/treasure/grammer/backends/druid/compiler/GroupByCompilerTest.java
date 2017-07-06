package org.doraemon.treasure.grammer.backends.druid.compiler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import junit.framework.Assert;

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
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprAnd;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprEq;
import org.doraemon.treasure.grammer.mid.model.IBooleanExpr;
import org.doraemon.treasure.grammer.mid.model.operands.AliasOperand;
import org.doraemon.treasure.grammer.mid.model.LimitOperand;
import org.doraemon.treasure.grammer.mid.model.operands.NameOperand;
import org.doraemon.treasure.grammer.mid.model.Operand;
import org.doraemon.treasure.grammer.mid.model.operands.binary.DivideOperand;
import org.doraemon.treasure.grammer.mid.model.operands.math.SumOperand;
import org.doraemon.treasure.grammer.mid.model.operands.primitive.StringPrimitiveOperand;
import org.doraemon.treasure.grammer.parser.Query;

import static org.doraemon.treasure.grammer.JSONAssert.nonExist;

public class GroupByCompilerTest
{
  private GroupByCompiler compiler;

  @Before
  public void setUp()
  {
    compiler = new GroupByCompiler();
  }

  @Test
  public void testCompileCompilerContext() throws SQLException
  {
    Query query = createQueryBuilder().build();

    Assert.assertTrue(compiler.canCompile(query));
    String json = compiler.compile(query);

    JSONObject jo = JSON.parseObject(json);
    JSONAssert.eq(jo, "queryType", "groupBy");
    JSONAssert.eq(jo, "dataSource", "metric_table");
    JSONAssert.exists(jo, "limitSpec");
    JSONAssert.eq(jo, "granularity", "all");
    JSONAssert.exists(jo, "dimensions");
    JSONAssert.exists(jo, "filter");
    JSONAssert.exists(jo, "aggregations");
    JSONAssert.exists(jo, "postAggregations");
    JSONAssert.exists(jo, "intervals");
  }

  @Test
  public void testCanCompile() throws SQLException
  {
    Query query = Query.builder()
                       .limit(new LimitOperand(0, 1))
                       .columns(createColumns())
                       .groupBys(Arrays.asList(Mockito.mock(Operand.class), Mockito.mock(Operand.class)))
                       .orderBy(Arrays.asList(Mockito.mock(OrderByOperand.class)))
                        .granularity(new SimpleGranularity("day"))
                       .build();
    Assert.assertTrue(compiler.canCompile(query));
    query = Query.builder()
                 .limit(new LimitOperand(0, 1))
                 .columns(createColumns())
                 .groupBys(Arrays.asList(Mockito.mock(Operand.class)))
                  .granularity(new SimpleGranularity("day"))
                 .orderBy(Arrays.asList(Mockito.mock(OrderByOperand.class), Mockito.mock(OrderByOperand.class)))
                 .build();
    Assert.assertTrue(compiler.canCompile(query));
  }

  @Test
  public void test_canCompile_return_true_when_query_include_no_limit() throws Exception {
    Query query = createQueryWithoutLimit();
    Assert.assertTrue(compiler.canCompile(query));
  }

  @Test
  public void test_compile_success_when_query_include_no_limit() throws Exception {
    Query query = createQueryWithoutLimit();
    final String result = compiler.compile(query);

    JSONObject json = JSON.parseObject(result);
    nonExist(json, "limitSpec");
  }

  private Query.QueryBuilder createQueryBuilder() {
    // timestamps:
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
    OrderByOperand orderBy2 = new OrderByOperand(new SumOperand("longSum", new NameOperand(null, "num1")), true);
    IBooleanExpr whereClause =
            new BooleanExprAnd(
                    new BooleanExprEq(new NameOperand(null, "name"), new StringPrimitiveOperand("oneapm")),
                    new BooleanExprEq(
                            new NameOperand(null, "type"),
                            new IntPrimitiveOperand("10")
                    )
            );
    return Query.builder()
            .orderBy(Arrays.asList(orderBy, orderBy2))
            .limit(new LimitOperand(1, 10))
            .groupBys(Arrays.asList((Operand) new NameOperand(null, "id")))
            .granularity(new SimpleGranularity("all"))
            .columns(columns)
            .table("metric_table")
            .timestamps(Pair.of(start, end))
            .whereClause(whereClause);
  }


  private Query createQueryWithoutLimit() {
    return createQueryBuilder().limit(null).build();
  }

  private List<Operand> createColumns()
  {
    List<Operand> columns = new ArrayList<Operand>();
    columns.add(new AliasOperand(new SumOperand("longSum", new NameOperand(null, "num1")), "num1"));
    return columns;
  }

}
