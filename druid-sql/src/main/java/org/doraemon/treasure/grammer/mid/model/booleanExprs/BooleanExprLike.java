package org.doraemon.treasure.grammer.mid.model.booleanExprs;

import org.doraemon.treasure.grammer.mid.model.IBooleanExpr;
import org.doraemon.treasure.grammer.mid.model.Operand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class BooleanExprLike implements IBooleanExpr {
    private final Operand left;
    private final Operand right;
}
