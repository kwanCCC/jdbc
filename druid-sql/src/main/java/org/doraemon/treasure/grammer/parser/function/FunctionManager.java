package org.doraemon.treasure.grammer.parser.function;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.doraemon.treasure.grammer.mid.model.operands.CountOperand;
import org.doraemon.treasure.grammer.mid.model.operands.NameOperand;
import org.doraemon.treasure.grammer.mid.model.Operand;
import org.doraemon.treasure.grammer.mid.model.operands.math.MaxOperand;
import org.doraemon.treasure.grammer.mid.model.operands.math.MinOperand;
import org.doraemon.treasure.grammer.mid.model.operands.math.SumOperand;


public class FunctionManager {
    private static final Map<String, Function> funs = new ConcurrentHashMap<String, Function>();


    static {
        funs.put("longsum", new CommonFunction(SumOperand.class, "longSum"));
        funs.put("doublesum", new CommonFunction(SumOperand.class, "doubleSum"));
        funs.put("longmax", new CommonFunction(MaxOperand.class, "longMax"));
        funs.put("doublemax", new CommonFunction(MaxOperand.class, "doubleMax"));
        funs.put("longmin", new CommonFunction(MinOperand.class, "longMin"));
        funs.put("doublemin", new CommonFunction(MinOperand.class, "doubleMin"));
        // funs.put("count", new CommonFunction(CountOperand.class, "count"));
        funs.put("count", new CountFunction());

    }


    public static Function getFunction(String functionName) {
        if (!funs.containsKey(functionName.toLowerCase())) {
            throw new RuntimeException("parse sql error:not found " + functionName + " function");
        }
        return funs.get(functionName.toLowerCase());
    }
}


class CommonFunction implements Function {
    private Constructor<? extends Operand> constructor;
    private String                         type;

    public CommonFunction(Class<? extends Operand> operandClazz, String type) {
        try {
            constructor = operandClazz.getConstructor(String.class, NameOperand.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        this.type = type;
    }

    @Override
    public Object call(Object... args) {
        try {
            return constructor.newInstance(type, (NameOperand) args[0]);
        } catch (Exception e) {

        }
        throw new RuntimeException("parser sql aggregators error");
    }

}


class CountFunction implements Function {

    @Override
    public Object call(Object... args) {
        return new CountOperand("count", (Operand) args[0]);
    }

}
