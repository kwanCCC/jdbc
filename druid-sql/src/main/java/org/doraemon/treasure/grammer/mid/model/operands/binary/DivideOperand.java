package org.doraemon.treasure.grammer.mid.model.operands.binary;

import org.doraemon.treasure.grammer.mid.model.Operand;

/**
 * / division always returns 0 if dividing by0, regardless of the numerator.<br>
 * {@link http://druid.io/docs/0.8.0/querying/post-aggregations.html}
 * 
 * @author zhxiaog
 *
 */
public class DivideOperand extends BinaryOperand {

    public DivideOperand(Operand left, Operand right) {
        super(left, right, "/");
    }
}
