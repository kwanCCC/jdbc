package org.doraemon.treasure.grammer.mid.model.booleanExprs;

import org.doraemon.treasure.grammer.mid.model.IBooleanExpr;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@AllArgsConstructor
public class BooleanExprNot implements IBooleanExpr
{
  private final IBooleanExpr inner;
}
