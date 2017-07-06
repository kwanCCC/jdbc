package org.doraemon.treasure.grammer.mid.model.booleanExprs;

import org.doraemon.treasure.grammer.mid.model.IBooleanExpr;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class BooleanExprAnd implements IBooleanExpr
{
  private final IBooleanExpr left;
  private final IBooleanExpr right;
}
