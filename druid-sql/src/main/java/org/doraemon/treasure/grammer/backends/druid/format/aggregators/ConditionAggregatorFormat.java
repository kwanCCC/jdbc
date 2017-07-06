package org.doraemon.treasure.grammer.backends.druid.format.aggregators;


import org.doraemon.treasure.grammer.backends.druid.dsl.aggregators.IAggregator;
import org.doraemon.treasure.grammer.backends.druid.dsl.aggregators.JavaScriptAggregation;
import org.doraemon.treasure.grammer.mid.model.IBooleanExpr;
import org.doraemon.treasure.grammer.mid.model.Operand;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprAnd;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprEq;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprGt;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprLt;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprNot;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprOr;
import org.doraemon.treasure.grammer.mid.model.operands.AliasOperand;
import org.doraemon.treasure.grammer.mid.model.operands.CountOperand;
import org.doraemon.treasure.grammer.mid.model.operands.NameOperand;
import org.doraemon.treasure.grammer.mid.model.operands.binary.BinaryOperand;
import org.doraemon.treasure.grammer.mid.model.operands.math.AggregationOperand;
import org.doraemon.treasure.grammer.mid.model.operands.math.ConditionAggregationOperand;
import org.doraemon.treasure.grammer.mid.model.operands.math.MaxOperand;
import org.doraemon.treasure.grammer.mid.model.operands.math.MinOperand;
import org.doraemon.treasure.grammer.mid.model.operands.math.SumOperand;
import org.doraemon.treasure.grammer.mid.model.operands.primitive.PrimitiveOperand;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConditionAggregatorFormat extends AggregatorFormat
{

  @Override
  public IAggregator format(Operand operand) throws SQLException
  {
    //TODO:加个缓存?
    ConditionAggregationOperand conditionOperand = (ConditionAggregationOperand) operand;

    Operand innerAggOperand = conditionOperand.getInnerOperand();

    List<String> allFields = getAggregateOperand(innerAggOperand);
    allFields.addAll(getAggregateOperand(conditionOperand.getCondition()));

    List<String> fields = new ArrayList<String>();
    for (String field : allFields) {
      if (!fields.contains(field)) {
        fields.add(field);
      }
    }

    return new JavaScriptAggregation(
        null,
        fields,
        getRestFunction(),
        getCombineFunction(innerAggOperand),
        getAggregateFunction(fields, innerAggOperand, conditionOperand.getCondition())
    );
  }

  private List<String> getAggregateOperand(Operand operand) throws SQLException
  {
    if (operand instanceof AggregationOperand) {
      return getAggregateOperand(((AggregationOperand) operand).getName());
    } else if (operand instanceof CountOperand) {
      return new ArrayList<String>();
    } else if (operand instanceof PrimitiveOperand) {
      return new ArrayList<String>();
    } else if (operand instanceof BinaryOperand) {
      BinaryOperand binaryOperand = (BinaryOperand) operand;
      List<String> returnValue = new ArrayList<String>();
      returnValue.addAll(getAggregateOperand(binaryOperand.getLeft()));
      returnValue.addAll(getAggregateOperand(binaryOperand.getRight()));
      return returnValue;
    } else if (operand instanceof NameOperand) {
      List<String> returnValue = new ArrayList<String>();
      returnValue.add(((NameOperand) operand).getColumn());
      return returnValue;
    } else if (operand instanceof AliasOperand) {
      throw new SQLException("不支持的语法agg(a alias sb)");
    } else if (operand instanceof ConditionAggregationOperand) {
      throw new SQLException("不支持的嵌套Condition Aggregator操作");
    } else {
      throw new SQLException("Cannot format Operand:" + operand);
    }
  }

  private List<String> getAggregateOperand(IBooleanExpr booleanExpr) throws SQLException
  {
    if (booleanExpr instanceof BooleanExprAnd) {
      List<String> returnValue = new ArrayList<String>();
      returnValue.addAll(getAggregateOperand(((BooleanExprAnd) booleanExpr).getLeft()));
      returnValue.addAll(getAggregateOperand(((BooleanExprAnd) booleanExpr).getRight()));
      return returnValue;
    } else if (booleanExpr instanceof BooleanExprOr) {
      List<String> returnValue = new ArrayList<String>();
      List<IBooleanExpr> innerExpr = ((BooleanExprOr) booleanExpr).getInnerExpr();
      for (IBooleanExpr expr : innerExpr) {
        returnValue.addAll(getAggregateOperand(expr));
      }
      return returnValue;
    } else if (booleanExpr instanceof BooleanExprNot) {
      return getAggregateOperand(((BooleanExprNot) booleanExpr).getInner());
    } else if (booleanExpr instanceof BooleanExprEq) {
      List<String> returnValue = new ArrayList<String>();
      returnValue.addAll(getAggregateOperand(((BooleanExprEq) booleanExpr).getLeft()));
      returnValue.addAll(getAggregateOperand(((BooleanExprEq) booleanExpr).getLeft()));
      return returnValue;
    } else if (booleanExpr instanceof BooleanExprGt) {
      List<String> returnValue = new ArrayList<String>();
      returnValue.addAll(getAggregateOperand(((BooleanExprGt) booleanExpr).getLeft()));
      returnValue.addAll(getAggregateOperand(((BooleanExprGt) booleanExpr).getLeft()));
      return returnValue;
    } else if (booleanExpr instanceof BooleanExprLt) {
      List<String> returnValue = new ArrayList<String>();
      returnValue.addAll(getAggregateOperand(((BooleanExprLt) booleanExpr).getLeft()));
      returnValue.addAll(getAggregateOperand(((BooleanExprLt) booleanExpr).getLeft()));
      return returnValue;
    } else {
      throw new SQLException("暂不支持的判断,请检查Driver的版本");
    }
  }

  private String getRestFunction()
  {
    return "function(){return 0;}";
  }

  private String getCombineFunction(Operand aggOperand) throws SQLException
  {
    if (aggOperand instanceof MaxOperand) {
      return "function(partialA, partialB){ if(partialA > partialB){ return partialA; }else{ return partialB; }}";
    } else if (aggOperand instanceof MinOperand) {
      return "function(partialA, partialB){ if(partialA < partialB){ return partialA; }else{ return partialB; }}";
    } else if (aggOperand instanceof SumOperand) {
      return "function(partialA, partialB){return partialA + partialB;}";
    } else if (aggOperand instanceof CountOperand) {
      return "function(partialA, partialB){return partialA + partialB;}";
    } else {
      throw new SQLException("暂不支持的聚合类型,请检查Driver的版本");
    }
  }

  private String getAggregateFunction(List<String> fields, Operand aggOperand, IBooleanExpr condition)
      throws SQLException
  {
    StringBuilder builder = new StringBuilder();
    for (String field : fields) {
      builder.append(",").append(field);
    }
    String option;
    if (aggOperand instanceof MaxOperand) {
      String columnName = ((MaxOperand) aggOperand).getName().getColumn();
      option = "if( current > " + columnName + " ){ return current; }else{ return " + columnName + " ; }";
    } else if (aggOperand instanceof MinOperand) {
      String columnName = ((MinOperand) aggOperand).getName().getColumn();
      option = "if( current < " + columnName + " ){ return current; }else{ return " + columnName + " ; }";
    } else if (aggOperand instanceof SumOperand) {
      String columnName = ((SumOperand) aggOperand).getName().getColumn();
      option = " return current + " + columnName + "; ";
    } else if (aggOperand instanceof CountOperand) {
      option = " return current + 1 ; ";
    } else {
      throw new SQLException("不支持的语法,必须为agg(<列名>) if");
    }
    return "function(current," + builder.substring(1) + ") {" +
           " if ( " + getBooleanExpr(condition) + "){" +
           option +
           "}" + "else{return current;}" +
           "}";
  }

  private String getBooleanExpr(IBooleanExpr booleanExpr) throws SQLException
  {
    if (booleanExpr instanceof BooleanExprAnd) {
      return "(" + getBooleanExpr(((BooleanExprAnd) booleanExpr).getLeft())
             + " && "
             + getBooleanExpr(((BooleanExprAnd) booleanExpr).getRight()) + ")";
    } else if (booleanExpr instanceof BooleanExprOr) {
      List<IBooleanExpr> innerExpr = ((BooleanExprOr) booleanExpr).getInnerExpr();
      StringBuilder builder = new StringBuilder();
      builder.append("(");
      for (int i = 0; i < innerExpr.size(); i++) {
        if (i > 0) {
          builder.append(" || ");
        }
        builder.append(getBooleanExpr(innerExpr.get(i)));
      }
      return builder.append(")").toString();
    } else if (booleanExpr instanceof BooleanExprNot) {
      return "(!(" + getBooleanExpr(((BooleanExprNot) booleanExpr).getInner()) + "))";
    } else if (booleanExpr instanceof BooleanExprEq) {
      return "( "
             + getOperandExpr(((BooleanExprEq) booleanExpr).getLeft())
             + " = "
             + getOperandExpr(((BooleanExprEq) booleanExpr).getRight())
             + " )";
    } else if (booleanExpr instanceof BooleanExprGt) {
      String option = ((BooleanExprGt) booleanExpr).getIsEquals() ? ">=" : ">";
      return "( "
             + getOperandExpr(((BooleanExprGt) booleanExpr).getLeft())
             + " "
             + option
             + " "
             + getOperandExpr(((BooleanExprGt) booleanExpr).getRight())
             + " )";
    } else if (booleanExpr instanceof BooleanExprLt) {
      String option = ((BooleanExprLt) booleanExpr).getIsEquals() ? "<=" : "<";
      return "( "
             + getOperandExpr(((BooleanExprLt) booleanExpr).getLeft())
             + " "
             + option
             + " "
             + getOperandExpr(((BooleanExprLt) booleanExpr).getRight())
             + " )";
    } else {
      throw new SQLException("暂不支持的判断,请检查Driver的版本");
    }
  }

  private String getOperandExpr(Operand operand) throws SQLException
  {
    if (operand instanceof BinaryOperand) {
      return "( "
             + getOperandExpr(((BinaryOperand) operand).getLeft())
             + " " + ((BinaryOperand) operand).getOperator() + " "
             + getOperandExpr(((BinaryOperand) operand).getRight())
             + " )";
    } else if (operand instanceof NameOperand) {
      return ((NameOperand) operand).getColumn();
    } else if (operand instanceof PrimitiveOperand) {
      String objectValue = ((PrimitiveOperand) operand).getValue();
      if (StringUtils.isNumeric(objectValue)) {
        return objectValue;
      } else {
        return "\"" + objectValue + "\"";
      }
    } else {
      throw new SQLException("Cannot format Operand:" + operand);
    }
  }
}
