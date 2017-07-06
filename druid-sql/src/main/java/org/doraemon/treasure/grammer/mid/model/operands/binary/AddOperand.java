package org.doraemon.treasure.grammer.mid.model.operands.binary;

import org.doraemon.treasure.grammer.mid.model.Operand;

public class AddOperand extends BinaryOperand {

    public AddOperand(Operand left, Operand right) {
        super(left, right, "+");
    }

}
