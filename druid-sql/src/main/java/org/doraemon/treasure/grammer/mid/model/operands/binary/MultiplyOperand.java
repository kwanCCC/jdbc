package org.doraemon.treasure.grammer.mid.model.operands.binary;

import org.doraemon.treasure.grammer.mid.model.Operand;

public class MultiplyOperand extends BinaryOperand {

    public MultiplyOperand(Operand left, Operand right) {
        super(left, right, "*");
    }

}
