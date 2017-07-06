package org.doraemon.treasure.grammer.mid.model.operands.binary;

import org.doraemon.treasure.grammer.mid.model.Operand;

public class MinusOperand extends BinaryOperand {

    public MinusOperand(Operand left, Operand right) {
        super(left, right, "-");
    }

}
