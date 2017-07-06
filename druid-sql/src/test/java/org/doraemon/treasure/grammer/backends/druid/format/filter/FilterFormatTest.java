package org.doraemon.treasure.grammer.backends.druid.format.filter;

import org.doraemon.treasure.grammer.backends.druid.dsl.filters.AndOrFilter;
import org.doraemon.treasure.grammer.backends.druid.dsl.filters.BoundFilter;
import org.doraemon.treasure.grammer.backends.druid.dsl.filters.Filter;
import org.doraemon.treasure.grammer.backends.druid.dsl.filters.NotFilter;
import org.doraemon.treasure.grammer.backends.druid.dsl.filters.SelectorFilter;
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

import static org.junit.Assert.*;

public class FilterFormatTest
{

  private final FilterFormat format = new FilterFormat();

  @Test
  public void test_eq_expr_to_selector_filter() throws SQLException
  {
    NameOperand testNameOperand = new NameOperand("test_table", "test_column");
    PrimitiveOperand testValueOperand = new FloatPrimitiveOperand("0.1");
    BooleanExprEq eqExpr = new BooleanExprEq(testNameOperand, testValueOperand);
    Assert.assertEquals(format.format(eqExpr), new SelectorFilter("test_column", "0.1"));
  }

  @Test(expected = SQLException.class)
  public void test_eq_expr_throw_sql_exception_when_sub_expr_some_name_operand() throws SQLException
  {
    NameOperand testNameOperand = new NameOperand("test_table", "test_column");
    BooleanExprEq eqExpr = new BooleanExprEq(testNameOperand, testNameOperand);
    format.format(eqExpr);
  }

  @Test
  public void test_greater_expr_to_bound_filter() throws SQLException
  {
    NameOperand testNameOperand = new NameOperand("test_table", "test_column");
    PrimitiveOperand testValueOperand = new FloatPrimitiveOperand("0.1");
    BooleanExprGt eqExpr = new BooleanExprGt(testNameOperand, testValueOperand, false);
    Assert.assertEquals(format.format(eqExpr), new BoundFilter("test_column", "0.1", false, null, null, true));
  }

  @Test
  public void test_greater_and_eq_expr_to_bound_filter() throws SQLException
  {
    NameOperand testNameOperand = new NameOperand("test_table", "test_column");
    PrimitiveOperand testValueOperand = new FloatPrimitiveOperand("0.1");
    BooleanExprGt eqExpr = new BooleanExprGt(testNameOperand, testValueOperand, true);
    assertEquals(format.format(eqExpr), new BoundFilter("test_column", "0.1", true, null, null, true));
  }

  @Test
  public void test_less_expr_to_bound_filter() throws SQLException
  {
    NameOperand testNameOperand = new NameOperand("test_table", "test_column");
    PrimitiveOperand testValueOperand = new FloatPrimitiveOperand("0.1");
    BooleanExprLt eqExpr = new BooleanExprLt(testNameOperand, testValueOperand, false);
    assertEquals(format.format(eqExpr), new BoundFilter("test_column", null, null, "0.1", false, true));
  }

  @Test
  public void test_less_and_eq_expr_to_bound_filter() throws SQLException
  {
    NameOperand testNameOperand = new NameOperand("test_table", "test_column");
    PrimitiveOperand testValueOperand = new FloatPrimitiveOperand("0.1");
    BooleanExprLt eqExpr = new BooleanExprLt(testNameOperand, testValueOperand, true);
    assertEquals(format.format(eqExpr), new BoundFilter("test_column", null, null, "0.1", true, true));
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
    AndOrFilter expect = new AndOrFilter("and", Arrays.<Filter>asList(
        new SelectorFilter("test_column", "0.1"),
        new SelectorFilter("test_column", "0.1")
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
    AndOrFilter expect = new AndOrFilter("or", Arrays.<Filter>asList(
        new SelectorFilter("test_column", "0.1"),
        new SelectorFilter("test_column", "0.1")
    ));
    assertEquals(format.format(orExpr), expect);
  }

  @Test
  public void test_not_expr_to_not_filter() throws SQLException
  {
    NameOperand testNameOperand = new NameOperand("test_table", "test_column");
    PrimitiveOperand testValueOperand = new FloatPrimitiveOperand("0.1");
    BooleanExprNot notExpr = new BooleanExprNot(new BooleanExprEq(testNameOperand, testValueOperand));
    assertEquals(format.format(notExpr), new NotFilter(new SelectorFilter("test_column", "0.1")));
  }


}
