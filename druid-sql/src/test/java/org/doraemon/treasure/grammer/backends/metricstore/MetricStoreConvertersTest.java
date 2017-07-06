//package com.blueocn.druid.backends.metricstore;
//
//import com.blueocn.druid.backends.metricstore.dsl.columns.Column;
//import com.blueocn.druid.backends.metricstore.dsl.columns.aggregations.MaxColumn;
//import com.blueocn.druid.backends.metricstore.dsl.columns.aggregations.MinColumn;
//import com.blueocn.druid.backends.metricstore.dsl.columns.aggregations.SumColumn;
//import com.blueocn.druid.backends.metricstore.dsl.columns.arithmetric.AddColumn;
//import com.blueocn.druid.backends.metricstore.dsl.columns.arithmetric.DivColumn;
//import com.blueocn.druid.backends.metricstore.dsl.columns.arithmetric.MinusColumn;
//import com.blueocn.druid.backends.metricstore.dsl.columns.arithmetric.MultiplyColumn;
//import com.blueocn.druid.backends.metricstore.dsl.columns.primitives.DoubleColumn;
//import com.blueocn.druid.backends.metricstore.dsl.columns.primitives.LongColumn;
//import com.blueocn.druid.backends.metricstore.dsl.columns.primitives.StringColumn;
//import com.blueocn.druid.backends.metricstore.dsl.filters.AndFilter;
//import com.blueocn.druid.backends.metricstore.dsl.filters.EqualFilter;
//import com.blueocn.druid.backends.metricstore.dsl.filters.Filter;
//import com.blueocn.druid.backends.metricstore.dsl.filters.NotFilter;
//import com.blueocn.druid.backends.metricstore.dsl.filters.OrFilter;
//import com.blueocn.druid.mid.model.booleanExprs.BooleanExprAnd;
//import com.blueocn.druid.mid.model.booleanExprs.BooleanExprEq;
//import com.blueocn.druid.mid.model.booleanExprs.BooleanExprNot;
//import com.blueocn.druid.mid.model.booleanExprs.BooleanExprOr;
//import com.blueocn.druid.mid.model.operands.NameOperand;
//import com.blueocn.druid.mid.model.operands.binary.AddOperand;
//import com.blueocn.druid.mid.model.operands.binary.DividOperand;
//import com.blueocn.druid.mid.model.operands.binary.MinusOperand;
//import com.blueocn.druid.mid.model.operands.binary.MultiplyOperand;
//import com.blueocn.druid.mid.model.operands.math.MaxOperand;
//import com.blueocn.druid.mid.model.operands.math.MinOperand;
//import com.blueocn.druid.mid.model.operands.math.SumOperand;
//import com.blueocn.druid.mid.model.operands.primitive.IntPrimitiveOperand;
//import org.junit.Test;
//
//import java.sql.SQLException;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.Assert.assertEquals;
//
//public class MetricStoreConvertersTest {
//
//    @Test
//    public void test_convert_boolean_eq_to_equal_filter() throws SQLException {
//        BooleanExprEq booleanExprEq = new BooleanExprEq(new IntPrimitiveOperand("1"), new NameOperand("t1", "c1"));
//        assertEquals(new EqualFilter(new LongColumn("c1"), 1L), MetricStoreConverters.convert(booleanExprEq));
//    }
//
//    @Test
//    public void test_convert_boolean_and_to_and_filter() throws SQLException {
//        BooleanExprEq  left  = new BooleanExprEq(new IntPrimitiveOperand("1"), new NameOperand("t1", "c1"));
//        BooleanExprEq  right = new BooleanExprEq(new IntPrimitiveOperand("1"), new NameOperand("t1", "c2"));
//        BooleanExprAnd and   = new BooleanExprAnd(left, right);
//
//        Filter    filter1 = new EqualFilter(new LongColumn("c1"), 1L);
//        Filter    filter2 = new EqualFilter(new LongColumn("c2"), 1L);
//        AndFilter filter  = new AndFilter(Arrays.asList(filter1, filter2));
//        assertEquals(filter, MetricStoreConverters.convert(and));
//    }
//
//    @Test
//    public void test_convert_boolean_or_to_or_filter() throws SQLException {
//        BooleanExprEq left  = new BooleanExprEq(new IntPrimitiveOperand("1"), new NameOperand("t1", "c1"));
//        BooleanExprEq right = new BooleanExprEq(new IntPrimitiveOperand("1"), new NameOperand("t1", "c2"));
//        BooleanExprOr and   = new BooleanExprOr(left, right);
//
//        Filter   filter1 = new EqualFilter(new LongColumn("c1"), 1L);
//        Filter   filter2 = new EqualFilter(new LongColumn("c2"), 1L);
//        OrFilter filter  = new OrFilter(Arrays.asList(filter1, filter2));
//        assertEquals(filter, MetricStoreConverters.convert(and));
//    }
//
//    @Test
//    public void test_convert_binary_operand_to_columns() throws SQLException
//    {
//        List<Column> innerColumns = Arrays.<Column>asList(new StringColumn("c1"), new StringColumn("c2"));
//
//        NameOperand leftOperand  = new NameOperand("t1", "c1");
//        NameOperand rightOperand = new NameOperand("t1", "c2");
//
//        AddOperand addOperand = new AddOperand(leftOperand, rightOperand);
//        AddColumn addColumn = new AddColumn(innerColumns);
//        assertEquals(addColumn, MetricStoreConverters.convert(addOperand));
//
//        DividOperand dividOperand = new DividOperand(leftOperand, rightOperand);
//        DivColumn   dividColumn = new DivColumn(innerColumns);
//        assertEquals(dividColumn, MetricStoreConverters.convert(dividOperand));
//
//        MinusOperand minusOperand = new MinusOperand(leftOperand, rightOperand);
//        MinusColumn minusColumn = new MinusColumn(innerColumns);
//        assertEquals(minusColumn, MetricStoreConverters.convert(minusOperand));
//
//        MultiplyOperand multiplyOperand = new MultiplyOperand(leftOperand, rightOperand);
//        MultiplyColumn multiplyColumn = new MultiplyColumn(innerColumns);
//        assertEquals(multiplyColumn, MetricStoreConverters.convert(multiplyOperand));
//    }
//
//    @Test
//    public void test_convert_agg_operand_to_columns() throws SQLException {
//        MaxOperand maxOperand = new MaxOperand("doubleMax", new NameOperand("t1", "c1"));
//        assertEquals(new MaxColumn(new DoubleColumn("c1")), MetricStoreConverters.convert(maxOperand));
//
//        MinOperand minOperand = new MinOperand("doubleMin", new NameOperand("t1", "c1"));
//        assertEquals(new MinColumn(new DoubleColumn("c1")), MetricStoreConverters.convert(minOperand));
//
//        SumOperand sumOperand = new SumOperand("doubleSum", new NameOperand("t1", "c1"));
//        assertEquals(new SumColumn(new DoubleColumn("c1")), MetricStoreConverters.convert(sumOperand));
//    }
//
//    @Test
//    public void test_convert_not_expr() throws SQLException
//    {
//        BooleanExprNot expr =
//                new BooleanExprNot(new BooleanExprEq(new NameOperand("", "test"), new IntPrimitiveOperand("1")));
//        NotFilter expected = new NotFilter(new EqualFilter(new LongColumn("test"), 1L));
//        assertEquals(expected, MetricStoreConverters.convert(expr));
//    }
//}
