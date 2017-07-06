//package com.blueocn.druid.mid.model.booleanExprs;
//
//import org.junit.Assert;
//import org.junit.Test;
//
//import com.blueocn.druid.backends.druid.dsl.filters.AndOrFilter;
//import com.blueocn.druid.backends.druid.dsl.filters.Filter;
//import com.blueocn.druid.mid.model.operands.NameOperand;
//import com.blueocn.druid.mid.model.operands.primitive.IntPrimitiveOperand;
//
//public class BooleanExprOrTest {
//
//
//    BooleanExprOr getExprOr() {
//        return new BooleanExprOr(new BooleanExprEq(new NameOperand("test", "t"), new IntPrimitiveOperand("1")), new BooleanExprEq(new NameOperand("test", "t"),
//                new IntPrimitiveOperand("1")));
//    }
//
//    @Test
//    public void testGetFilter() {
//        BooleanExprOr and = getExprOr();
//        Filter f = and.getFilter();
//        Assert.assertTrue(AndOrFilter.class.isInstance(f));
//        AndOrFilter andF = (AndOrFilter) f;
//        Assert.assertEquals(2, andF.getFields().size());
//    }
//
//    @Test
//    public void testGetFilterWithHierichyAnd() {
//        BooleanExprOr and = new BooleanExprOr(getExprOr(), new BooleanExprEq(new NameOperand("test", "t"), new IntPrimitiveOperand("1")));
//        Filter f = and.getFilter();
//        Assert.assertTrue(AndOrFilter.class.isInstance(f));
//        AndOrFilter andF = (AndOrFilter) f;
//        Assert.assertEquals(3, andF.getFields().size());
//    }
//
//}
