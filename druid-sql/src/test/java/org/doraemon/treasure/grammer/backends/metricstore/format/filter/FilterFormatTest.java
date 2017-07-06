package org.doraemon.treasure.grammer.backends.metricstore.format.filter;

import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.ConstColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.primitives.DoubleColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.filters.AndFilter;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.filters.CompareFilter;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.filters.EqualFilter;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.filters.Filter;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.filters.NotFilter;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.filters.OrFilter;
import org.doraemon.treasure.grammer.mid.model.IBooleanExpr;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprAnd;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprEq;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprGt;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprLt;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprNot;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprOr;
import org.doraemon.treasure.grammer.mid.model.operands.NameOperand;
import org.doraemon.treasure.grammer.mid.model.operands.primitive.FloatPrimitiveOperand;
import org.doraemon.treasure.grammer.mid.model.operands.primitive.PrimitiveOperand;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class FilterFormatTest
{
  private final FilterFormat format = new FilterFormat();

  @Test
  public void test_eq_expr_to_equal_filter() throws SQLException
  {
    NameOperand testNameOperand = new NameOperand("test_table", "test_column");
    PrimitiveOperand testValueOperand = new FloatPrimitiveOperand("0.1");
    BooleanExprEq eqExpr = new BooleanExprEq(testNameOperand, testValueOperand);
    assertEquals(format.format(eqExpr), new EqualFilter(new DoubleColumn("test_column"), 0.1));
  }

  @Test(expected = SQLException.class)
  public void test_eq_expr_throw_sql_exception_when_sub_expr_some_name_operand() throws SQLException
  {
    NameOperand testNameOperand = new NameOperand("test_table", "test_column");
    BooleanExprEq eqExpr = new BooleanExprEq(testNameOperand, testNameOperand);
    format.format(eqExpr);
  }

  @Test
  public void test_greater_expr_to_compare_filter() throws SQLException
  {
    NameOperand testNameOperand = new NameOperand("test_table", "test_column");
    PrimitiveOperand testValueOperand = new FloatPrimitiveOperand("0.1");
    BooleanExprGt eqExpr = new BooleanExprGt(testNameOperand, testValueOperand, false);
    assertEquals(format.format(eqExpr), new CompareFilter(new DoubleColumn("test_column"), new ConstColumn(0.1), ">"));
  }

  @Test
  public void test_greater_and_eq_expr_to_compare_filter() throws SQLException
  {
    NameOperand testNameOperand = new NameOperand("test_table", "test_column");
    PrimitiveOperand testValueOperand = new FloatPrimitiveOperand("0.1");
    BooleanExprGt eqExpr = new BooleanExprGt(testNameOperand, testValueOperand, true);
    assertEquals(format.format(eqExpr), new CompareFilter(new DoubleColumn("test_column"), new ConstColumn(0.1), ">="));
  }

  @Test
  public void test_less_expr_to_compare_filter() throws SQLException
  {
    NameOperand testNameOperand = new NameOperand("test_table", "test_column");
    PrimitiveOperand testValueOperand = new FloatPrimitiveOperand("0.1");
    BooleanExprLt eqExpr = new BooleanExprLt(testNameOperand, testValueOperand, false);
    assertEquals(format.format(eqExpr), new CompareFilter(new DoubleColumn("test_column"), new ConstColumn(0.1), "<"));
  }

  @Test
  public void test_less_and_eq_expr_to_compare_filter() throws SQLException
  {
    NameOperand testNameOperand = new NameOperand("test_table", "test_column");
    PrimitiveOperand testValueOperand = new FloatPrimitiveOperand("0.1");
    BooleanExprLt eqExpr = new BooleanExprLt(testNameOperand, testValueOperand, true);
    assertEquals(format.format(eqExpr), new CompareFilter(new DoubleColumn("test_column"), new ConstColumn(0.1), "<="));
  }

  @Test
  public void test_and_expr_to_and_filter() throws SQLException
  {
    NameOperand testNameOperand = new NameOperand("test_table", "test_column");
    PrimitiveOperand testValueOperand = new FloatPrimitiveOperand("0.1");
    IBooleanExpr andExpr = new BooleanExprAnd(
        new BooleanExprEq(testNameOperand, testValueOperand),
        new BooleanExprEq(testNameOperand, testValueOperand)
    );
    AndFilter expect = new AndFilter(Arrays.<Filter>asList(
        new EqualFilter(new DoubleColumn("test_column"), 0.1),
        new EqualFilter(new DoubleColumn("test_column"), 0.1)
    ));
    assertEquals(format.format(andExpr), expect);
  }

  @Test
  public void test_or_expr_to_and_filter() throws SQLException
  {
    NameOperand testNameOperand = new NameOperand("test_table", "test_column");
    PrimitiveOperand testValueOperand = new FloatPrimitiveOperand("0.1");
    IBooleanExpr orExpr = new BooleanExprOr(
        Arrays.<IBooleanExpr>asList(
            new BooleanExprEq(testNameOperand, testValueOperand),
            new BooleanExprEq(testNameOperand, testValueOperand)
        )
    );
    OrFilter expect = new OrFilter("or", Arrays.<Filter>asList(
        new EqualFilter(new DoubleColumn("test_column"), 0.1),
        new EqualFilter(new DoubleColumn("test_column"), 0.1)
    ));
    assertEquals(format.format(orExpr), expect);
  }

  @Test
  public void test_not_expr_to_not_filter() throws SQLException
  {
    NameOperand testNameOperand = new NameOperand("test_table", "test_column");
    PrimitiveOperand testValueOperand = new FloatPrimitiveOperand("0.1");
    BooleanExprNot notExpr = new BooleanExprNot(new BooleanExprEq(testNameOperand, testValueOperand));
    Assert.assertEquals(format.format(notExpr), new NotFilter(new EqualFilter(new DoubleColumn("test_column"), 0.1)));
  }

}
