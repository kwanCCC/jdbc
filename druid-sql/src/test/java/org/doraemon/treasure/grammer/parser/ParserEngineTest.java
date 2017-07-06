package org.doraemon.treasure.grammer.parser;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.doraemon.treasure.grammer.mid.model.IBooleanExpr;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprNot;
import org.doraemon.treasure.grammer.parser.exceptions.DruidSQLException;
import org.doraemon.treasure.grammer.parser.exceptions.LexicalErrorException;
import org.doraemon.treasure.grammer.parser.exceptions.SyntaxErrorException;
import junit.framework.Assert;

import org.doraemon.treasure.grammer.mid.model.OrderByOperand;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprAnd;
import org.doraemon.treasure.grammer.mid.model.operands.binary.MultiplyOperand;
import org.doraemon.treasure.grammer.mid.model.operands.primitive.IntPrimitiveOperand;
import org.junit.Rule;
import org.junit.Test;

import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprEq;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprOr;
import org.doraemon.treasure.grammer.mid.model.operands.AliasOperand;
import org.doraemon.treasure.grammer.mid.model.LimitOperand;
import org.doraemon.treasure.grammer.mid.model.operands.NameOperand;
import org.doraemon.treasure.grammer.mid.model.Operand;
import org.doraemon.treasure.grammer.mid.model.operands.binary.AddOperand;
import org.doraemon.treasure.grammer.mid.model.operands.binary.DivideOperand;
import org.doraemon.treasure.grammer.mid.model.operands.binary.MinusOperand;
import org.doraemon.treasure.grammer.mid.model.operands.math.SumOperand;
import org.doraemon.treasure.grammer.mid.model.operands.primitive.FloatPrimitiveOperand;
import org.doraemon.treasure.grammer.mid.model.operands.primitive.StringPrimitiveOperand;
import org.junit.rules.ExpectedException;

import java.util.Arrays;

public class ParserEngineTest {
    
    private long now = System.currentTimeMillis();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * 测试基础的select语句
     */
    @Test
    public void baseSelectTest() {
        String sql =
                "select aaa,sss.bbb,abc as abcd,longSum(abcd) AS ss,longSum(abc) as uh,( doubleSum(abcd) / 3.6 ) as hj,(doubleSum(aaa)+3.6*(4.5/abc)-def) as iu from table_name";
        Query query = ParserEngine.parse(sql);
        assertNotNull(query);
        assertEquals(query.getTable(), "table_name");
        assertArrayEquals(
                query.getColumns()
                        .toArray(),
                new Operand[] {new NameOperand("table_name", "aaa"),
                               new NameOperand("sss",
                                               "bbb"),
                               new AliasOperand(new NameOperand("table_name", "abc"),
                                                "abcd"),
                               new AliasOperand(new SumOperand("longSum",
                                                               new NameOperand("table_name",
                                                                               "abcd")),
                                                "ss"),
                               new AliasOperand(new SumOperand("longSum", new NameOperand("table_name", "abc")),
                                                "uh"),
                               new AliasOperand(
                                        new DivideOperand(new SumOperand("doubleSum",
                                                                         new NameOperand("table_name",
                                                                                        "abcd")),
                                                          new FloatPrimitiveOperand("3.6")) {},
                                        "hj"),
                               new AliasOperand(new MinusOperand(
                                        new AddOperand(new SumOperand("doubleSum", new NameOperand("table_name", "aaa")),
                                                       new MultiplyOperand(new FloatPrimitiveOperand("3.6"),
                                                                           new DivideOperand(new FloatPrimitiveOperand("4.5"), new NameOperand("table_name", "abc")))),
                                        new NameOperand("table_name", "def")), "iu")});

    }

    @Test
    public void whereTest() {
        long startTime = System.currentTimeMillis();
        long endTime = System.currentTimeMillis();
        String sql = "select aaa from bbb wherE timestamps between " + startTime + " and " + endTime + " and (aaa=\'aaaa\' or (longsum(bbb)=2015 and doubleSum(ddd)=2015.54))";
        Query query = ParserEngine.parse(sql);
        assertNotNull(query);
        assertEquals(query.getTable(), "bbb");
        assertEquals(query.getWhereClause(), new BooleanExprOr(
            Arrays.asList(
                new BooleanExprEq(new NameOperand("bbb", "aaa"), new StringPrimitiveOperand("aaaa")),
                new BooleanExprAnd(
                    new BooleanExprEq(
                            new SumOperand("longSum", new NameOperand("bbb", "bbb")), new IntPrimitiveOperand("2015")
                    ),
                    new BooleanExprEq(
                            new SumOperand("doubleSum", new NameOperand("bbb", "ddd")), new FloatPrimitiveOperand("2015.54")
                    )
                )
            ))
        );
    }

    @Test
    public void test_support_parse_groupBy_sql() {
        String sql = "select aaa from bbb group by ccc,longsum(ddd),longsum(eee)/doublesum(aaa),longsum(ddd)/sss*(longsum(www)+doublesum(sss))";

        Query query = ParserEngine.parse(sql);
        assertNotNull(query);
        assertEquals(query.getTable(), "bbb");
        assertArrayEquals(query.getGroupBys().toArray(),
                new Operand[] {new NameOperand("bbb", "ccc"), new SumOperand("longSum", new NameOperand("bbb", "ddd")),
                               new DivideOperand(new SumOperand("longSum", new NameOperand("bbb", "eee")), new SumOperand("doubleSum", new NameOperand("bbb", "aaa"))),
                               new MultiplyOperand(new DivideOperand(new SumOperand("longSum", new NameOperand("bbb", "ddd")), new NameOperand("bbb", "sss")),
                                                   new AddOperand(new SumOperand("longSum", new NameOperand("bbb", "www")), new SumOperand("doubleSum", new NameOperand("bbb", "sss"))))});

    }

    @Test
    public void test_support_parse_orderBy_in_sql() {
        String sql = "select aaa from bbb order by aaa desc,longsum(aaa) ,longsum(aaa)/5 asc";
        Query query = ParserEngine.parse(sql);
        assertNotNull(query);
        assertArrayEquals(query.getOrderBy().toArray(),
                new OrderByOperand[] {new OrderByOperand(new NameOperand("bbb", "aaa"), true), new OrderByOperand(new SumOperand("longSum", new NameOperand("bbb", "aaa")), false),
                                      new OrderByOperand(new DivideOperand(new SumOperand("longSum", new NameOperand("bbb", "aaa")), new IntPrimitiveOperand("5")), false)});
    }

    @Test
    public void test_support_parse_limit_in_sql_0() {
        String sql  = "select aaa from bbb where  timestamps between " + now + " and " + now + " limit 1 5";
        Query query = ParserEngine.parse(sql);
        assertNotNull(query);
        LimitOperand limitOperand = query.getLimit();
        assertEquals(limitOperand.getOffset(), 1);
        assertEquals(limitOperand.getMaxSize(), 6);
    }

    @Test
    public void test_support_parse_limit_with_comma_in_sql_0() {
        String sql  = "select aaa from bbb where  timestamps between " + now + " and " + now + " limit 1, 5";
        Query query = ParserEngine.parse(sql);
        assertNotNull(query);
        LimitOperand limitOperand = query.getLimit();
        assertEquals(limitOperand.getOffset(), 1);
        assertEquals(limitOperand.getMaxSize(), 6);
    }

    @Test
    public void test_support_hierarchical_AND_OR_operator_in_where_clause() {
        long startTime = System.currentTimeMillis();
        long endTime = System.currentTimeMillis();
        String sql = "select aaa from bbb where timestamps between " + startTime + " and " + endTime + " and ccc = 1 and aaa = 2 and aa = 3 or c = 4 or c = 5 ";
        Query query = ParserEngine.parse(sql);
        assertNotNull(query);
        assertEquals(query.getWhereClause(),
                     new BooleanExprOr(Arrays.asList(
                         new BooleanExprOr(
                             Arrays.asList(
                                 new BooleanExprAnd(
                                        new BooleanExprAnd(new BooleanExprEq(new NameOperand("bbb", "ccc"), new IntPrimitiveOperand("1")),
                                                new BooleanExprEq(new NameOperand("bbb", "aaa"), new IntPrimitiveOperand("2"))),
                                        new BooleanExprEq(new NameOperand("bbb", "aa"), new IntPrimitiveOperand("3"))),
                                 new BooleanExprEq(new NameOperand("bbb", "c"), new IntPrimitiveOperand("4"))
                             )),
                         new BooleanExprEq(new NameOperand("bbb", "c"), new IntPrimitiveOperand("5"))
                     ))
        );

    }



    @Test
    public void test() {
        String sql =
                "SELECT metricId,DOUBLESUM(num1) as num1,DOUBLESUM(num2) as num2, DOUBLESUM(num3) as num3,DOUBLEMIN(num4) as num4,DOUBLEMAX(num5) as num5, DOUBLESUM(num6) as num6 ,agentRunId as agentRunId FROM druid_metric_1hour WHERE "
                        + "timespamp between " + (now - 3600) + " and " + now + " and applicationId=1 and "
                        + "(metricTypeId=1 OR metricTypeId=2 OR metricTypeId=3 OR metricTypeId=4) and " + "(metricId=1 OR metricId=2 OR metricId=3 OR metricId=4) and agentRunId=1 "
                        + "GROUP BY metricId ,agentRunId order by doublesum(num2)/doublesum(num1) asc LIMIT 1 10 BREAK BY " + now + " to 3600 ";

        Query query = ParserEngine.parse(sql);
        assertNotNull(query);
    }

    @Test
    public void test_support_parse_limit_in_sql() {
        long now = System.currentTimeMillis();
        String sql =
                "SELECT metricId,DOUBLESUM(num1) as num1,DOUBLESUM(num2) as num2, DOUBLESUM(num3) as num3,DOUBLEMIN(num4) as num4,DOUBLEMAX(num5) as num5, DOUBLESUM(num6) as num6 ,agentRunId as agentRunId FROM druid_metric_1hour WHERE "
                        + "timespamp between "
                        + (now - 3600)
                        + " and "
                        + now
                        + " and applicationId=1 and "
                        + "(metricTypeId=1 OR metricTypeId=2 OR metricTypeId=3 OR metricTypeId=4) and "
                        + "(metricId=1 OR metricId=2 OR metricId=3 OR metricId=4) and agentRunId=1 "
                        + "GROUP BY metricId ,agentRunId order by doublesum(num2)/doublesum(num1) asc LIMIT 1 10 BREAK BY " + now + " to 3600 ";

        Query query = ParserEngine.parse(sql);
        assertNotNull(query);
        Assert.assertEquals(11, query.getLimit().getMaxSize());
    }

    @Test
    public void test_support_parse_limit_with_comma_in_sql() {
        long now = System.currentTimeMillis();
        String sql =
                "SELECT metricId,DOUBLESUM(num1) as num1,DOUBLESUM(num2) as num2, DOUBLESUM(num3) as num3,DOUBLEMIN(num4) as num4,DOUBLEMAX(num5) as num5, DOUBLESUM(num6) as num6 ,agentRunId as agentRunId FROM druid_metric_1hour WHERE "
                        + "timespamp between "
                        + (now - 3600)
                        + " and "
                        + now
                        + " and applicationId=1 and "
                        + "(metricTypeId=1 OR metricTypeId=2 OR metricTypeId=3 OR metricTypeId=4) and "
                        + "(metricId=1 OR metricId=2 OR metricId=3 OR metricId=4) and agentRunId=1 "
                        + "GROUP BY metricId ,agentRunId order by doublesum(num2)/doublesum(num1) asc LIMIT 1,10 BREAK BY " + now + " to 3600 ";

        Query query = ParserEngine.parse(sql);
        assertNotNull(query);
        Assert.assertEquals(11, query.getLimit().getMaxSize());
    }
    
    @Test
    public void test_support_parse_not_operator_in_where_clause() {
        String sql   = "select aaa from bbb where timestamps between 1 and 2 and id <> 1";
        Query  query = ParserEngine.parse(sql);
        assertNotNull(query);
        BooleanExprNot expected =
                new BooleanExprNot(new BooleanExprEq(new NameOperand("bbb", "id"), new IntPrimitiveOperand("1")));
        assertEquals(query.getWhereClause(), expected);
    }

    @Test
    public void testStringLiteral_singleQuoteEscape() throws Exception {
        String sql = "select c1, c2, tag from t where t between 1 and 2 and tag in ('INFORMIX','MySQL')";
        Query  query = ParserEngine.parse(sql);
        assertNotNull(query);
        BooleanExprEq expr1 = new BooleanExprEq(new NameOperand("t", "tag"), new StringPrimitiveOperand("INFORMIX"));
        BooleanExprEq expr2 = new BooleanExprEq(new NameOperand("t", "tag"), new StringPrimitiveOperand("MySQL"));
        BooleanExprOr expected = new BooleanExprOr(Arrays.<IBooleanExpr>asList(expr1, expr2));
        assertEquals(query.getWhereClause(), expected);
    }

    @Test
    public void testEmptySql() {
        thrown.expect(DruidSQLException.class);
        thrown.expectMessage(containsString("blank sql is not allowed"));
        ParserEngine.parse(null);

        thrown.expect(DruidSQLException.class);
        thrown.expectMessage(containsString("blank sql is not allowed"));
        ParserEngine.parse("");
    }

    @Test
    public void testLexicalError() {
        String sql   = "select aaa  $ from  bbb where timestamps xyz between 1 and 2 and id <> 1";

        thrown.expect(LexicalErrorException.class);
        thrown.expectMessage(containsString("\n" + sql));
        thrown.expectMessage(containsString("\n            ^"));
        thrown.expectMessage(containsString("line 1, pos 12 near $ : $"));

        ParserEngine.parse(sql);
    }

    @Test
    public void testLexicalError_MultipleLine() {
        String sql   = "select "
                       + "\naaa  $ from  bbb where timestamps xyz between 1 and 2 and id <> 1";

        thrown.expect(LexicalErrorException.class);
        thrown.expectMessage(containsString("\naaa  $ from  bbb where timestamps xyz between 1 and 2 and id <> 1"));
        thrown.expectMessage(containsString("\n     ^"));
        thrown.expectMessage(containsString("line 2, pos 5 near $ : $"));

        ParserEngine.parse(sql);
    }

    @Test
    public void testSyntaxError() {
        String sql   = "select aaa from  bbb where timestamps xyz between 1 and 2 and id <> 1";

        thrown.expect(SyntaxErrorException.class);
        thrown.expectMessage(containsString("\n" + sql));
        thrown.expectMessage(containsString("\n                                      ^^^"));
        thrown.expectMessage(containsString("line 1, pos 38 near xyz : mismatched input 'xyz' expecting BETWEEN"));

        ParserEngine.parse(sql);
    }

    @Test
    public void testSyntaxError_MultipleLine() {
        String sql   = "select aaa from  bbb where "
                       + "\n \ttimestamps xyz between 1 and 2 and id <> 1";

        thrown.expect(SyntaxErrorException.class);
        thrown.expectMessage(containsString("\n \ttimestamps xyz between 1 and 2 and id <> 1"));
        thrown.expectMessage(containsString("\n \t           ^^^"));
        thrown.expectMessage(containsString("line 2, pos 13 near xyz : mismatched input 'xyz' expecting BETWEEN"));

        ParserEngine.parse(sql);
    }


}
