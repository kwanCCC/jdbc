package org.doraemon.treasure.grammer.backends.druid.format.aggregators;

import org.doraemon.treasure.grammer.backends.druid.dsl.aggregators.CountAggregator;
import org.doraemon.treasure.grammer.backends.druid.dsl.aggregators.IAggregator;
import org.doraemon.treasure.grammer.backends.druid.dsl.aggregators.MathAggregator;
import org.doraemon.treasure.grammer.backends.druid.dsl.postAggregators.ArithMetricPostAggregator;
import org.doraemon.treasure.grammer.backends.druid.dsl.postAggregators.ConstPostAggregator;
import org.doraemon.treasure.grammer.mid.model.Operand;
import org.doraemon.treasure.grammer.mid.model.operands.AliasOperand;
import org.doraemon.treasure.grammer.mid.model.operands.CountOperand;
import org.doraemon.treasure.grammer.mid.model.operands.NameOperand;
import org.doraemon.treasure.grammer.mid.model.operands.binary.AddOperand;
import org.doraemon.treasure.grammer.mid.model.operands.binary.DivideOperand;
import org.doraemon.treasure.grammer.mid.model.operands.binary.MinusOperand;
import org.doraemon.treasure.grammer.mid.model.operands.binary.MultiplyOperand;
import org.doraemon.treasure.grammer.mid.model.operands.math.AggregationOperand;
import org.doraemon.treasure.grammer.mid.model.operands.math.MaxOperand;
import org.doraemon.treasure.grammer.mid.model.operands.math.MinOperand;
import org.doraemon.treasure.grammer.mid.model.operands.math.SumOperand;
import org.doraemon.treasure.grammer.mid.model.operands.primitive.FloatPrimitiveOperand;
import org.doraemon.treasure.grammer.mid.model.operands.primitive.IntPrimitiveOperand;
import org.doraemon.treasure.grammer.mid.model.operands.primitive.StringPrimitiveOperand;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AggregatorFormatTest
{
  private final AggregatorFormat aggregatorFormat = new AggregatorFormat();

  @Test
  public void test_math_aggregation_format() throws SQLException
  {
    NameOperand nameOperand = new NameOperand("test_table", "test_column");
    Operand maxOperand = new MaxOperand("doubleMax", nameOperand);
    Operand minOperand = new MinOperand("doubleMin", nameOperand);
    Operand sumOperand = new SumOperand("doubleSum", nameOperand);
    assertEquals(aggregatorFormat.format(maxOperand), new MathAggregator("doubleMax", null, "test_column"));
    assertEquals(aggregatorFormat.format(minOperand), new MathAggregator("doubleMin", null, "test_column"));
    assertEquals(aggregatorFormat.format(sumOperand), new MathAggregator("doubleSum", null, "test_column"));
  }

  @Test(expected = SQLException.class)
  public void test_other_math_aggregation_format() throws SQLException
  {
    Operand otherOperand = new AggregationOperand()
    {
      @Override
      public String getType()
      {
        return "other_aggregation_type";
      }

      @Override
      public NameOperand getName()
      {
        return new NameOperand("test_table", "test_column");
      }
    };
    aggregatorFormat.format(otherOperand);
  }


  @Test
  public void test_alias_operand_aggregation_format() throws SQLException
  {
    NameOperand nameOperand = new NameOperand("test_table", "test_column");
    Operand aliasOperand = new AliasOperand(new MaxOperand("doubleMax", nameOperand), "test_alias_new_name");
    assertEquals(
        aggregatorFormat.format(aliasOperand),
        new MathAggregator("doubleMax", "test_alias_new_name", "test_column")
    );
  }

  @Test
  public void test_alias_name_operand_aggregation_format() throws SQLException
  {
    Operand aliasOperand = new AliasOperand(new NameOperand("test_table", "test_column"), "test_newName");
    assertEquals(aggregatorFormat.format(aliasOperand), null);
  }

  @Test
  public void test_count_operand_aggregation_format() throws SQLException
  {
    Operand countOperand = new CountOperand("count", null);
    assertEquals(aggregatorFormat.format(countOperand), new CountAggregator());
  }


  @Test
  public void test_primitive_operand_aggregation_format() throws SQLException
  {
    Operand intPrimitiveOperand = new IntPrimitiveOperand("1");
    Operand floatPrimitiveOperand = new FloatPrimitiveOperand("0.1");
    Operand stringPrimitiveOperand = new StringPrimitiveOperand("test_string");
    assertEquals(aggregatorFormat.format(intPrimitiveOperand), new ConstPostAggregator(null, "1"));
    assertEquals(aggregatorFormat.format(floatPrimitiveOperand), new ConstPostAggregator(null, "0.1"));
    assertEquals(aggregatorFormat.format(stringPrimitiveOperand), new ConstPostAggregator(null, "test_string"));
  }


  @Test
  public void test_arithMetric_operand_aggregation_format() throws SQLException
  {
    Operand primitiveOperand = new FloatPrimitiveOperand("0.1");
    List<IAggregator> subAggregator = Arrays.<IAggregator>asList(
        new ConstPostAggregator(null, "0.1"),
        new ConstPostAggregator(null, "0.1")
    );
    Operand add = new AddOperand(primitiveOperand, primitiveOperand);
    Operand div = new DivideOperand(primitiveOperand, primitiveOperand);
    Operand minus = new MinusOperand(primitiveOperand, primitiveOperand);
    Operand mul = new MultiplyOperand(primitiveOperand, primitiveOperand);

    assertEquals(aggregatorFormat.format(add), new ArithMetricPostAggregator(null, "+", subAggregator));
    assertEquals(aggregatorFormat.format(div), new ArithMetricPostAggregator(null, "/", subAggregator));
    assertEquals(aggregatorFormat.format(minus), new ArithMetricPostAggregator(null, "-", subAggregator));
    assertEquals(aggregatorFormat.format(mul), new ArithMetricPostAggregator(null, "*", subAggregator));

  }


  @Test(expected = SQLException.class)
  public void test_null_aggregation_format() throws SQLException
  {
    aggregatorFormat.format(null);
  }

  @Test(expected = SQLException.class)
  public void test_other_aggregation_format() throws SQLException
  {
    Operand otherOperand = new Operand()
    {

    };
    aggregatorFormat.format(otherOperand);
  }
}
