package org.doraemon.treasure.grammer.mid.model.operands.primitive;

import org.doraemon.treasure.grammer.mid.model.Operand;

public interface PrimitiveOperand extends Operand {
    String getValue();
    
    String getType();
}
