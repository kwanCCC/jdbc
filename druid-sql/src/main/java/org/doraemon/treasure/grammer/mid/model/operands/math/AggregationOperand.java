package org.doraemon.treasure.grammer.mid.model.operands.math;

import org.doraemon.treasure.grammer.mid.model.operands.NameOperand;
import org.doraemon.treasure.grammer.mid.model.Operand;

public interface AggregationOperand extends Operand {
    
    String getType();
    
    NameOperand getName();
}
