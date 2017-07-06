package org.doraemon.treasure.grammer.parser;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.commons.lang3.tuple.Pair;

import org.doraemon.treasure.grammer.mid.model.IBooleanExpr;
import org.doraemon.treasure.grammer.mid.model.Granularity;
import org.doraemon.treasure.grammer.mid.model.LimitOperand;
import org.doraemon.treasure.grammer.mid.model.Operand;
import org.doraemon.treasure.grammer.mid.model.OrderByOperand;

@Data
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class Query
{
  private final Boolean              isUnique;
  private final String               table;
  private final List<Operand>        columns;
  private final IBooleanExpr         whereClause;
  private final List<OrderByOperand> orderBy;
  private final List<Operand>        groupBys;
  private final Pair<Long, Long>     timestamps;
  private final LimitOperand         limit;
  private final Granularity          granularity;
}
