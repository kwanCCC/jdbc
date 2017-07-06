package org.doraemon.treasure.grammer.backends.metricstore.format.filter;


import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.Column;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.ConstColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.filters.Filter;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.filters.LikeFilter;
import org.doraemon.treasure.grammer.backends.metricstore.util.MetricStoreOperandSupport;
import org.doraemon.treasure.grammer.mid.model.IBooleanExpr;
import org.doraemon.treasure.grammer.mid.model.Operand;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprLike;
import org.doraemon.treasure.grammer.mid.model.operands.NameOperand;
import org.doraemon.treasure.grammer.mid.model.operands.primitive.PrimitiveOperand;
import org.doraemon.treasure.grammer.mid.model.operands.primitive.StringPrimitiveOperand;

import java.sql.SQLException;

public class LikeFilterFormat extends FilterFormat {
    @Override
    public Filter format(IBooleanExpr expr) throws SQLException {
        BooleanExprLike likeExpr = (BooleanExprLike) expr;
        Operand         left     = likeExpr.getLeft();
        Operand         right    = likeExpr.getRight();

        if (left instanceof NameOperand && right instanceof StringPrimitiveOperand) {
            Column column                = MetricStoreOperandSupport.buildPrimitiveColumn(((NameOperand) left).getColumn(), ((StringPrimitiveOperand) right).getType());
            Object primitiveOperandValue = MetricStoreOperandSupport.getPrimitiveOperandValue((PrimitiveOperand) right);
            return new LikeFilter(column, new ConstColumn(primitiveOperandValue));
        } else {
            throw new SQLException("目前仅支持对String类型的列进行Like操作");
        }
    }
}
