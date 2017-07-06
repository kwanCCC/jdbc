package org.doraemon.treasure.grammer.backends.druid.compiler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.lang3.tuple.Pair;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprAnd;
import org.doraemon.treasure.grammer.mid.model.granularities.SimpleGranularity;
import org.doraemon.treasure.grammer.mid.model.operands.math.SumOperand;
import org.doraemon.treasure.grammer.mid.model.operands.primitive.IntPrimitiveOperand;
import org.doraemon.treasure.grammer.parser.Query;
import org.junit.Before;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.doraemon.treasure.grammer.JSONAssert;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprEq;
import org.doraemon.treasure.grammer.mid.model.IBooleanExpr;
import org.doraemon.treasure.grammer.mid.model.operands.AliasOperand;
import org.doraemon.treasure.grammer.mid.model.operands.NameOperand;
import org.doraemon.treasure.grammer.mid.model.Operand;
import org.doraemon.treasure.grammer.mid.model.operands.binary.DivideOperand;
import org.doraemon.treasure.grammer.mid.model.operands.primitive.StringPrimitiveOperand;

public class TimeSeriesCompilerTest
{
  TimeSeriesCompiler compiler;

  @Before
  public void setUp()
  {
    compiler = new TimeSeriesCompiler();
  }

  @Test
  public void testCanCompile() throws SQLException
  {
    Query query = Query.builder()
                       .columns(createColumns())
                       .granularity(new SimpleGranularity("all"))
                       .build();
    Assert.assertTrue(compiler.canCompile(query));
  }

  @Test
  public void testCompile() throws SQLException
  {
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

    IBooleanExpr whereClause =
        new BooleanExprAnd(
            new BooleanExprEq(new NameOperand(null, "name"), new StringPrimitiveOperand("oneapm")),
            new BooleanExprEq(
                new NameOperand(null, "type"),
                new IntPrimitiveOperand("10")
            )
        );
    Query query =
        Query.builder()
             .granularity(new SimpleGranularity("PT1M"))
             .columns(columns)
             .table("metric_table")
             .timestamps(Pair.of(start, end))
             .whereClause(whereClause)
             .build();
    Assert.assertTrue(compiler.canCompile(query));
    String json = compiler.compile(query);
    JSONObject jo = JSON.parseObject(json);
    JSONAssert.eq(jo, "queryType", "timeseries");
    JSONAssert.eq(jo, "dataSource", "metric_table");
    JSONAssert.eq(jo, "granularity", "PT1M");
    JSONAssert.exists(jo, "filter");
    JSONAssert.exists(jo, "aggregations");
    JSONAssert.exists(jo, "postAggregations");
    JSONAssert.exists(jo, "intervals");
  }

  private List<Operand> createColumns()
  {
    List<Operand> columns = new ArrayList<Operand>();
    columns.add(new AliasOperand(new SumOperand("longSum", new NameOperand(null, "num1")), "num1"));
    return columns;
  }

}
