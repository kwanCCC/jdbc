package org.doraemon.treasure.grammer.mid.model.operands.binary;

import org.doraemon.treasure.grammer.mid.model.Operand;

/**
 * quotient division behaves like regular floating point division <br>
 * {@link http://druid.io/docs/0.8.0/querying/post-aggregations.html}
 * 
 * @author zhxiaog
 *
 */
public class QuotientOperand extends BinaryOperand {

    public QuotientOperand(Operand left, Operand right) {
        super(left, right, "quotient");
    }

}
