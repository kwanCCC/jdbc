package org.doraemon.treasure.grammer.backends.metricstore.compilers;

import org.doraemon.treasure.grammer.ObjectMapperFactory;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.queries.TimeSeriesQuery;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.Column;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.aggregations.SumColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.primitives.DoubleColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.primitives.LongColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.filters.EqualFilter;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprEq;
import org.doraemon.treasure.grammer.mid.model.granularities.SimpleGranularity;
import org.doraemon.treasure.grammer.mid.model.operands.NameOperand;
import org.doraemon.treasure.grammer.mid.model.Operand;
import org.doraemon.treasure.grammer.mid.model.OrderByOperand;
import org.doraemon.treasure.grammer.mid.model.operands.math.SumOperand;
import org.doraemon.treasure.grammer.mid.model.operands.primitive.IntPrimitiveOperand;
import org.doraemon.treasure.grammer.parser.Query;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.Interval;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class TimeSeriesQueryCompilerTest
{
  ObjectMapper mapper = ObjectMapperFactory.getMapper();
  IMetricStoreQueryCompiler timeSeriesQueryCompiler = new TimeSeriesQueryCompiler();

  @Test
  public void test_should_not_compile_when_found_orderBys() throws SQLException
  {
    Query query = Query.builder()
                       .columns(Arrays.<Operand>asList(new SumOperand("doubleSum", new NameOperand(null, "num1"))))
                       .orderBy(Arrays.asList(new OrderByOperand(new NameOperand("t1", "c1"), true)))
                       .build();

    assertFalse(timeSeriesQueryCompiler.canCompile(query));
  }

  @Test
  public void test_should_not_compile_when_found_groupBys() throws SQLException
  {
    List<Operand> groupBys = Arrays.<Operand>asList(new NameOperand("t1", "a1"));
    Query query = Query.builder()
                       .columns(Arrays.<Operand>asList(new SumOperand("doubleSum", new NameOperand(null, "num1"))))
                       .groupBys(groupBys)
                       .build();

    assertFalse(timeSeriesQueryCompiler.canCompile(query));
  }

  @Test
  public void test_should_compile_when_found_granularity() throws SQLException
  {
    Query query = Query.builder()
                       .columns(Arrays.<Operand>asList(new SumOperand("doubleSum", new NameOperand(null, "num1"))))
                       .granularity(new SimpleGranularity("PT1M"))
                       .build();

    assertTrue(timeSeriesQueryCompiler.canCompile(query));
  }

  @Test
  public void compile_simple_query() throws IOException, SQLException
  {
    long start = System.currentTimeMillis() / 60000 * 60000;
    long end = start + 30 * 60000;

    List<Operand> columns = Arrays.<Operand>asList(new SumOperand("doubleSum", new NameOperand(null, "num1")));

    Query query = Query.builder().timestamps(Pair.of(start, end)).columns(columns).table("mock")
                       .whereClause(new BooleanExprEq(new NameOperand(null, "uid"), new IntPrimitiveOperand("1")))
                       .granularity(new SimpleGranularity("PT1M")).build();
    String tsQueryJson = timeSeriesQueryCompiler.compile(query);

    TimeSeriesQuery actual = mapper.readValue(tsQueryJson, TimeSeriesQuery.class);
    TimeSeriesQuery expected =
        TimeSeriesQuery.builder().points(30).dataSource("mock").type("timeseries").interval(new Interval(start, end))
                       .columns(Arrays.<Column>asList(new SumColumn(new DoubleColumn("num1"))))
                       .filter(new EqualFilter(new LongColumn("uid"), 1)).build();
    assertEquals(expected, actual);
  }
}
