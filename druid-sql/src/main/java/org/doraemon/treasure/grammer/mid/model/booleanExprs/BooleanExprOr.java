package org.doraemon.treasure.grammer.mid.model.booleanExprs;

import org.doraemon.treasure.grammer.mid.model.IBooleanExpr;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class BooleanExprOr implements IBooleanExpr
{
  private final List<IBooleanExpr> innerExpr;
}
