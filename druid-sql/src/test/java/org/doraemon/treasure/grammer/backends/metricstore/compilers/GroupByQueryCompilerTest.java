package org.doraemon.treasure.grammer.backends.metricstore.compilers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.doraemon.treasure.grammer.mid.model.IBooleanExpr;
import org.doraemon.treasure.grammer.mid.model.LimitOperand;
import org.doraemon.treasure.grammer.mid.model.Operand;
import org.doraemon.treasure.grammer.mid.model.OrderByOperand;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprAnd;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprEq;
import org.doraemon.treasure.grammer.mid.model.granularities.SimpleGranularity;
import org.doraemon.treasure.grammer.mid.model.operands.AliasOperand;
import org.doraemon.treasure.grammer.mid.model.operands.NameOperand;
import org.doraemon.treasure.grammer.mid.model.operands.binary.DivideOperand;
import org.doraemon.treasure.grammer.mid.model.operands.math.SumOperand;
import org.doraemon.treasure.grammer.mid.model.operands.primitive.IntPrimitiveOperand;
import org.doraemon.treasure.grammer.mid.model.operands.primitive.StringPrimitiveOperand;
import org.doraemon.treasure.grammer.parser.Query;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static org.doraemon.treasure.grammer.JSONAssert.nonExist;
import static org.junit.Assert.*;


public class GroupByQueryCompilerTest {

    private GroupByQueryCompiler compile;

    @Before
    public void setUp() throws Exception {
        compile = new GroupByQueryCompiler();
    }

    @Test
    public void test_canCompile_return_true_when_without_limit() throws Exception {
        final Query query = createQueryWithoutLimit();
        assertTrue(compile.canCompile(query));
    }

    @Test
    public void test_compile_return_success_when_without_limit() throws Exception {
        final Query query = createQueryWithoutLimit();
        final String result = this.compile.compile(query);

        JSONObject jo = JSON.parseObject(result);
        nonExist(jo, "limit");
    }

    private Query.QueryBuilder createQueryBuilder() {
        // timestamps:
        Calendar cal = Calendar.getInstance();
        cal.set(2015, 1, 20, 20, 20, 20);
        long start = cal.getTimeInMillis();
        cal.set(2015, 2, 20, 20, 20, 20);
        long end = cal.getTimeInMillis();

        Operand avgOperand = new DivideOperand(
                new SumOperand("longSum", new NameOperand(null, "num1")),
                new SumOperand("longSum", new NameOperand(null, "num2"))
        );
        // selected columns:
        List<Operand> columns = new ArrayList<Operand>();
        columns.add(new AliasOperand(new SumOperand("longSum", new NameOperand(null, "num1")), "num1"));
        columns.add(new AliasOperand(avgOperand, "avg"));

        // order by
        OrderByOperand orderBy = new OrderByOperand(avgOperand, true);
        OrderByOperand orderBy2 = new OrderByOperand(new SumOperand("longSum", new NameOperand(null, "num1")), true);
        IBooleanExpr whereClause =
                new BooleanExprAnd(
                        new BooleanExprEq(new NameOperand(null, "name"), new StringPrimitiveOperand("oneapm")),
                        new BooleanExprEq(
                                new NameOperand(null, "type"),
                                new IntPrimitiveOperand("10")
                        )
                );
        return Query.builder()
                .orderBy(Arrays.asList(orderBy, orderBy2))
                .limit(new LimitOperand(1, 10))
                .groupBys(Arrays.asList((Operand) new NameOperand(null, "id")))
                .granularity(new SimpleGranularity("all"))
                .columns(columns)
                .table("metric_table")
                .timestamps(Pair.of(start, end))
                .whereClause(whereClause);
    }


    private Query createQueryWithoutLimit() {
        return createQueryBuilder().limit(null).build();
    }
}
