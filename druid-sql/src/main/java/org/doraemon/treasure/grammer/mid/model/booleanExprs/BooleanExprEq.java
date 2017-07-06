package org.doraemon.treasure.grammer.mid.model.booleanExprs;

import org.doraemon.treasure.grammer.mid.model.IBooleanExpr;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import org.doraemon.treasure.grammer.mid.model.Operand;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class BooleanExprEq implements IBooleanExpr
{
  private final Operand left;
  private final Operand right;
}
