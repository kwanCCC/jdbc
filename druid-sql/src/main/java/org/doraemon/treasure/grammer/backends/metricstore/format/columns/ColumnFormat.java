package org.doraemon.treasure.grammer.backends.metricstore.format.columns;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.Column;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.others.RefColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.primitives.StringColumn;
import org.doraemon.treasure.grammer.backends.metricstore.format.filter.FilterFormat;
import org.doraemon.treasure.grammer.mid.model.Operand;
import org.doraemon.treasure.grammer.mid.model.operands.AliasOperand;
import org.doraemon.treasure.grammer.mid.model.operands.CountOperand;
import org.doraemon.treasure.grammer.mid.model.operands.NameOperand;
import org.doraemon.treasure.grammer.mid.model.operands.binary.BinaryOperand;
import org.doraemon.treasure.grammer.mid.model.operands.math.AggregationOperand;
import org.doraemon.treasure.grammer.mid.model.operands.math.ConditionAggregationOperand;
import org.doraemon.treasure.grammer.mid.model.operands.primitive.PrimitiveOperand;

public class ColumnFormat
{
  private static final ConstColumnFormat       constColumnFormat       = new ConstColumnFormat();
  private static final CountColumnFormat       countColumnFormat       = new CountColumnFormat();
  private static final AliasColumnFormat       aliasColumnFormat       = new AliasColumnFormat();
  private static final ArithMetricColumnFormat arithMetricColumnFormat = new ArithMetricColumnFormat();
  private static final AggregationColumnFormat aggregationColumnFormat = new AggregationColumnFormat();
  private static final ConditionColumnFormat   conditionColumnFormat   = new ConditionColumnFormat();

  private static final FilterFormat filterFormat = new FilterFormat();


    public List<String> getAliasNames(List<? extends Operand> columns) throws SQLException {
        List<String> result = new ArrayList<>();
        for (Operand column : columns) {
            getAliasName(result, column);
        }
        return result;
    }

    private void getAliasName(List<String> names, Operand operand) {

        if (operand == null) {
            return;
        }

        if (operand instanceof CountOperand) {
            getAliasName(names, ((CountOperand) operand).getName());
        } else if (operand instanceof AliasOperand) {
            final String alias = ((AliasOperand) operand).getAlias();
            names.add(alias);
        } else if (operand instanceof BinaryOperand) {
            final BinaryOperand b = (BinaryOperand) operand;
            getAliasName(names, b.getLeft());
            getAliasName(names, b.getRight());
        } else if (operand instanceof AggregationOperand) {
            final AggregationOperand a = (AggregationOperand) operand;
            getAliasName(names, a.getName());
        } else if (operand instanceof NameOperand) {

        } else if (operand instanceof PrimitiveOperand) {

        } else if (operand instanceof ConditionAggregationOperand) {
            final ConditionAggregationOperand c = (ConditionAggregationOperand) operand;
            getAliasName(names, c.getInnerOperand());
        }

        return;
    }

    public Column formatWithRefColumn(List<String> aliasNames, Operand operand) throws SQLException {
        if (operand == null) {
            throw new SQLException("不能解析的Operand:NULL");
        }

        if (operand instanceof CountOperand) {
            return countColumnFormat.formatWithRefColumn(aliasNames, operand);
        } else if (operand instanceof AliasOperand) {
            return aliasColumnFormat.formatWithRefColumn(aliasNames, operand);
        } else if (operand instanceof BinaryOperand) {
            return arithMetricColumnFormat.formatWithRefColumn(aliasNames, operand);
        } else if (operand instanceof AggregationOperand) {
            return aggregationColumnFormat.formatWithRefColumn(aliasNames, operand);
        } else if (operand instanceof NameOperand) {

            final NameOperand named = (NameOperand) operand;
            final String column = named.getColumn();
            final boolean isRefColumn = aliasNames.contains(column);
            if (isRefColumn) {
                return new RefColumn(column);
            }
            return new StringColumn(column);
        } else if (operand instanceof PrimitiveOperand) {
            return constColumnFormat.format(operand);
        } else if (operand instanceof ConditionAggregationOperand) {
            return conditionColumnFormat.formatWithRefColumn(aliasNames, operand);
        } else {
            throw new SQLException("Cannot format Operand:" + operand);
        }

    }

    public Column format(Operand operand) throws SQLException {
        if (operand == null) {
            throw new SQLException("不能解析的Operand:NULL");
        }

        if (operand instanceof CountOperand) {
            return countColumnFormat.format(operand);
        } else if (operand instanceof AliasOperand) {
            return aliasColumnFormat.format(operand);
        } else if (operand instanceof BinaryOperand) {
            return arithMetricColumnFormat.format(operand);
        } else if (operand instanceof AggregationOperand) {
            return aggregationColumnFormat.format(operand);
        } else if (operand instanceof NameOperand) {
            return new StringColumn(((NameOperand) operand).getColumn());
        } else if (operand instanceof PrimitiveOperand) {
            return constColumnFormat.format(operand);
        } else if (operand instanceof ConditionAggregationOperand) {
            return conditionColumnFormat.format(operand);
        } else {
            throw new SQLException("Cannot format Operand:" + operand);
        }
  }
}
