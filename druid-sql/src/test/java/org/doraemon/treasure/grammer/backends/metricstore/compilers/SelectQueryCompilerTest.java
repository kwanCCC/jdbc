package org.doraemon.treasure.grammer.backends.metricstore.compilers;

import org.doraemon.treasure.grammer.ObjectMapperFactory;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.Column;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.aggregations.SumColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.primitives.DoubleColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.primitives.LongColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.filters.EqualFilter;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.queries.SelectQuery;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprEq;
import org.doraemon.treasure.grammer.mid.model.granularities.SimpleGranularity;
import org.doraemon.treasure.grammer.mid.model.operands.NameOperand;
import org.doraemon.treasure.grammer.mid.model.Operand;
import org.doraemon.treasure.grammer.mid.model.operands.math.SumOperand;
import org.doraemon.treasure.grammer.mid.model.operands.primitive.IntPrimitiveOperand;
import org.doraemon.treasure.grammer.parser.Query;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.Interval;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


public class SelectQueryCompilerTest {

    private ObjectMapper mapper = ObjectMapperFactory.getMapper();
    private SelectQueryCompiler selectQueryCompiler;

    private long start = System.currentTimeMillis() / 60000 * 60000;
    private long end = start + 30 * 60000;

    @Before
    public void setUp() throws Exception {
        selectQueryCompiler = new SelectQueryCompiler();
    }


    @Test
    public void test_should_not_compile_when_query_include_groupBy_clause() throws Exception {
        List<Operand> groupBys = Arrays.<Operand>asList(new NameOperand("t1", "a1"));
        Query query = Query.builder().groupBys(groupBys).build();

        assertFalse(selectQueryCompiler.canCompile(query));
    }

    @Test
    public void test_should_not_compile_when_query_include_granularity_clause() throws Exception {
        Query query = Query.builder().granularity(new SimpleGranularity("PT1M")).build();
        assertFalse(selectQueryCompiler.canCompile(query));
    }

    @Test
    public void test_compile_when_select_query_not_include_limit_clause() throws IOException, SQLException {
        Query query = queryBuilder().build();

        String json = selectQueryCompiler.compile(query);
        SelectQuery expected = selectQueryBuilder()
                .isUnique(false)
                .build();
        assertSelectQuery(json, expected);
    }


    @Test
    public void test_compile_success_when_select_query_not_include_where_clause() throws IOException, SQLException {
        Query query = queryBuilder()
                .whereClause(null)
                .build();

        String json = selectQueryCompiler.compile(query);
        SelectQuery expected = selectQueryBuilder()
                .isUnique(false)
                .filter(null)
                .build();

        assertSelectQuery(json, expected);
    }

    private Query.QueryBuilder queryBuilder() {
        return Query.builder()
                .timestamps(Pair.of(start, end))
                .columns(Arrays.<Operand>asList(new SumOperand("doubleSum", new NameOperand(null, "num1"))))
                .table("mock")
                .whereClause(new BooleanExprEq(new NameOperand(null, "uid"), new IntPrimitiveOperand("1")));
    }

    private SelectQuery.SelectQueryBuilder selectQueryBuilder() {
        return SelectQuery.builder()
                .dataSource("mock")
                .type("select")
                .interval(new Interval(start, end))
                .columns(Arrays.<Column>asList(new SumColumn(new DoubleColumn("num1"))))
                .filter(new EqualFilter(new LongColumn("uid"), 1));
    }


    private void assertSelectQuery(String json, SelectQuery expected) throws IOException {
        assertEquals(expected, mapper.readValue(json, SelectQuery.class));
    }

}
