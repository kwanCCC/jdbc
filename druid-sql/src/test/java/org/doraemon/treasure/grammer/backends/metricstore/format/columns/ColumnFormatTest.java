package org.doraemon.treasure.grammer.backends.metricstore.format.columns;

import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.Column;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.ConstColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.aggregations.MaxColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.aggregations.MinColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.aggregations.SumColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.arithmetric.AddColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.arithmetric.DivColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.arithmetric.MinusColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.arithmetric.MultiplyColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.others.AliasColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.others.CountColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.primitives.DoubleColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.primitives.StringColumn;
import org.doraemon.treasure.grammer.mid.model.Operand;
import org.doraemon.treasure.grammer.mid.model.operands.AliasOperand;
import org.doraemon.treasure.grammer.mid.model.operands.CountOperand;
import org.doraemon.treasure.grammer.mid.model.operands.NameOperand;
import org.doraemon.treasure.grammer.mid.model.operands.binary.AddOperand;
import org.doraemon.treasure.grammer.mid.model.operands.binary.DivideOperand;
import org.doraemon.treasure.grammer.mid.model.operands.binary.MinusOperand;
import org.doraemon.treasure.grammer.mid.model.operands.binary.MultiplyOperand;
import org.doraemon.treasure.grammer.mid.model.operands.math.MaxOperand;
import org.doraemon.treasure.grammer.mid.model.operands.math.MinOperand;
import org.doraemon.treasure.grammer.mid.model.operands.math.SumOperand;
import org.doraemon.treasure.grammer.mid.model.operands.primitive.FloatPrimitiveOperand;
import org.doraemon.treasure.grammer.mid.model.operands.primitive.IntPrimitiveOperand;
import org.doraemon.treasure.grammer.mid.model.operands.primitive.StringPrimitiveOperand;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ColumnFormatTest
{

  private final ColumnFormat format = new ColumnFormat();

  @Test
  public void test_count_operand_to_count_column_format() throws SQLException
  {
    CountOperand operand = new CountOperand("count", new NameOperand("test_table", "test_column"));
    assertEquals(format.format(operand), new CountColumn());
  }

  @Test
  public void test_alias_operand_to_alias_column_format() throws SQLException
  {
    AliasOperand aliasOperand = new AliasOperand(new NameOperand("test_table", "test_column"), "test_new_column_name");
    Assert.assertEquals(format.format(aliasOperand), new AliasColumn(new StringColumn("test_column"), "test_new_column_name"));
  }

  @Test
  public void test_binary_operand_to_arith_metric_column_format() throws SQLException
  {
    Operand primitiveOperand = new FloatPrimitiveOperand("0.1");
    List<Column> subColumn = Arrays.<Column>asList(new ConstColumn(0.1), new ConstColumn(0.1));

    Operand add = new AddOperand(primitiveOperand, primitiveOperand);
    Operand div = new DivideOperand(primitiveOperand, primitiveOperand);
    Operand minus = new MinusOperand(primitiveOperand, primitiveOperand);
    Operand mul = new MultiplyOperand(primitiveOperand, primitiveOperand);

    Assert.assertEquals(format.format(add), new AddColumn(subColumn));
    assertEquals(format.format(div), new DivColumn(subColumn));
    assertEquals(format.format(minus), new MinusColumn(subColumn));
    assertEquals(format.format(mul), new MultiplyColumn(subColumn));

  }

  @Test
  public void test_aggregate_operand_to_aggregation_column_format() throws SQLException
  {
    DoubleColumn expectInnerColumn = new DoubleColumn("test_column");
    NameOperand nameOperand = new NameOperand("test_table", "test_column");
    Operand maxOperand = new MaxOperand("doubleMax", nameOperand);
    Operand minOperand = new MinOperand("doubleMin", nameOperand);
    Operand sumOperand = new SumOperand("doubleSum", nameOperand);

    assertEquals(format.format(maxOperand), new MaxColumn(expectInnerColumn));
    Assert.assertEquals(format.format(minOperand), new MinColumn(expectInnerColumn));
    assertEquals(format.format(sumOperand), new SumColumn(expectInnerColumn));
  }

  @Test
  public void test_name_operand_to_string_column_format() throws SQLException
  {
    NameOperand nameOperand = new NameOperand("test_table", "test_column");
    assertEquals(format.format(nameOperand), new StringColumn("test_column"));
  }

  @Test
  public void test_primitive_operand_to_const_column_format() throws SQLException
  {
    Operand intPrimitiveOperand = new IntPrimitiveOperand("1");
    Operand floatPrimitiveOperand = new FloatPrimitiveOperand("0.1");
    Operand stringPrimitiveOperand = new StringPrimitiveOperand("test_string");

    Assert.assertEquals(format.format(intPrimitiveOperand), new ConstColumn(1L));
    Assert.assertEquals(format.format(floatPrimitiveOperand), new ConstColumn(0.1));
    Assert.assertEquals(format.format(stringPrimitiveOperand), new ConstColumn("test_string"));
  }
}
