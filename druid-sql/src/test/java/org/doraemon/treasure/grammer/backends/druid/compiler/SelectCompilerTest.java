package org.doraemon.treasure.grammer.backends.druid.compiler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.doraemon.treasure.grammer.JSONAssert;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprAnd;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprEq;
import org.doraemon.treasure.grammer.mid.model.IBooleanExpr;
import org.doraemon.treasure.grammer.mid.model.granularities.SimpleGranularity;
import org.doraemon.treasure.grammer.mid.model.operands.NameOperand;
import org.doraemon.treasure.grammer.mid.model.Operand;
import org.doraemon.treasure.grammer.mid.model.operands.primitive.IntPrimitiveOperand;
import org.doraemon.treasure.grammer.mid.model.operands.primitive.StringPrimitiveOperand;
import org.doraemon.treasure.grammer.parser.Query;
import junit.framework.Assert;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class SelectCompilerTest
{

  private SelectCompiler compiler;
  ;

  @Before
  public void setUp()
  {
    compiler = new SelectCompiler();
  }

  @Test
  public void test_should_not_compile_when_query_include_granularity_clause()
  {
    Query query = Query.builder().granularity(new SimpleGranularity("all")).build();
    Assert.assertFalse(compiler.canCompile(query));
  }


  @Test
  public void test_should_not_compile_when_query_include_groupBy_clause()
  {
    Query query = Query.builder()
                       .groupBys(Arrays.<Operand>asList(new NameOperand("t1", "a1")))
                       .build();
    Assert.assertFalse(compiler.canCompile(query));
  }

  @Test
  @Ignore
  public void testCompile() throws SQLException
  {
    // timestamps:
    Calendar cal = Calendar.getInstance();
    cal.set(2015, 1, 20, 20, 20, 20);
    long start = cal.getTimeInMillis();
    cal.set(2015, 2, 20, 20, 20, 20);
    long end = cal.getTimeInMillis();

    // selected columns:
    List<Operand> columns = new ArrayList<Operand>();
    columns.add(new NameOperand(null, "num1"));

    IBooleanExpr whereClause = new BooleanExprAnd(
        new BooleanExprEq(new NameOperand(null, "name"), new StringPrimitiveOperand("oneapm")),
        new BooleanExprEq(new NameOperand(null, "type"), new IntPrimitiveOperand("10"))
    );

    Query query = Query.builder()
                       .columns(columns)
                       .table("metric_table")
                       .timestamps(Pair.of(start, end))
                       .whereClause(whereClause)
                       .build();
    Assert.assertTrue(compiler.canCompile(query));
    String json = compiler.compile(query);
    System.out.println("json======" + json);
    JSONObject jo = JSON.parseObject(json);
    JSONAssert.eq(jo, "queryType", "select");
    JSONAssert.eq(jo, "dataSource", "metric_table");
    JSONAssert.exists(jo, "filter");
    JSONAssert.exists(jo, "intervals");
    JSONAssert.exists(jo, "pagingSpec");
  }
}
