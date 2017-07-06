package org.doraemon.treasure.grammer.backends.druid.format.aggregators;

import org.doraemon.treasure.grammer.backends.druid.dsl.aggregators.JavaScriptAggregation;
import org.doraemon.treasure.grammer.mid.model.IBooleanExpr;
import org.doraemon.treasure.grammer.mid.model.Operand;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprGt;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprLt;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprOr;
import org.doraemon.treasure.grammer.mid.model.operands.NameOperand;
import org.doraemon.treasure.grammer.mid.model.operands.math.ConditionAggregationOperand;
import org.doraemon.treasure.grammer.mid.model.operands.math.SumOperand;
import org.doraemon.treasure.grammer.mid.model.operands.primitive.IntPrimitiveOperand;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class ConditionAggregatorFormatTest
{
  private final ConditionAggregatorFormat format = new ConditionAggregatorFormat();

  @Test
  public void test_mi_javascript_format_test() throws Exception
  {
    Operand innerAggOperand = new SumOperand("doubleSum", new NameOperand("test_table", "error_count"));
    IBooleanExpr condition =
        new BooleanExprOr(
            Arrays.asList(
                new BooleanExprGt(new NameOperand("test_table", "status_code"), new IntPrimitiveOperand("10"), true),
                new BooleanExprLt(new NameOperand("test_table", "status_code"), new IntPrimitiveOperand("0"), true)
            )
        );
    ConditionAggregationOperand operand = new ConditionAggregationOperand(innerAggOperand, condition);

    String fnRest = "function(){return 0;}";
    String fnCombine = "function(partialA, partialB){return partialA + partialB;}";
    String fnAggregate = "function(current,error_count,status_code) { if ( (( status_code >= 10 ) || ( status_code <= 0 ))){ return current + error_count; }else{return current;}}";
    JavaScriptAggregation expect = new JavaScriptAggregation(
        null,
        Arrays.asList("error_count", "status_code"),
        fnRest,
        fnCombine,
        fnAggregate
    );

    Assert.assertEquals(format.format(operand), expect);
  }
}
