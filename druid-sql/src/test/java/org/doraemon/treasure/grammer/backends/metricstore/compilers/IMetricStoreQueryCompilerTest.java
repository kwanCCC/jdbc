package org.doraemon.treasure.grammer.backends.metricstore.compilers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.lang3.tuple.Pair;
import org.doraemon.treasure.grammer.mid.model.granularities.DurationGranularity;
import org.doraemon.treasure.grammer.mid.model.granularities.Granularities;
import org.doraemon.treasure.grammer.parser.Query;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;


public class IMetricStoreQueryCompilerTest {

    private IMetricStoreQueryCompiler compiler;

    @Before
    public void setUp() {
        compiler = mock(IMetricStoreQueryCompiler.class, CALLS_REAL_METHODS);
    }

    @Test
    public void beforeCompile_granularityAll() {
        Query query = mockQuery();
        when(query.getGranularity()).thenReturn(Granularities.ALL);
        Pair<Long, Long> timestamps = Pair.of(100L, 200L);
        when(query.getTimestamps()).thenReturn(timestamps);
        MatcherAssert.assertThat(compiler.beforeCompile(query).getTimestamps(), is(timestamps));
    }

    @Test
    public void beforeCompile_divisible() {
        Query query = mockQuery();
        when(query.getGranularity()).thenReturn(new DurationGranularity(10, 0));
        Pair<Long, Long> timestamps = Pair.of(100L, 200L);
        when(query.getTimestamps()).thenReturn(timestamps);
        MatcherAssert.assertThat(compiler.beforeCompile(query).getTimestamps(), is(timestamps));
    }

    @Test
    public void beforeCompile_notDivisible() {
        Query query = mockQuery();
        when(query.getGranularity()).thenReturn(new DurationGranularity(30, 0));
        Pair<Long, Long> timestamps = Pair.of(100L, 200L);
        when(query.getTimestamps()).thenReturn(timestamps);
        MatcherAssert.assertThat(compiler.beforeCompile(query).getTimestamps(), is(Pair.of(100L, 220L)));
    }

    @Test
    public void beforeCompile_intervalLessThanGranularity() {
        Query query = mockQuery();
        when(query.getGranularity()).thenReturn(new DurationGranularity(300, 0));
        Pair<Long, Long> timestamps = Pair.of(100L, 200L);
        when(query.getTimestamps()).thenReturn(timestamps);
        MatcherAssert.assertThat(compiler.beforeCompile(query).getTimestamps(), is(Pair.of(100L, 400L)));
    }

    private Query mockQuery() {
        Query query = mock(Query.class);
        when(query.getIsUnique()).thenReturn(false);
        when(query.getTable()).thenReturn("t");
        when(query.getColumns()).thenReturn(null);
        when(query.getWhereClause()).thenReturn(null);
        when(query.getOrderBy()).thenReturn(null);
        when(query.getGroupBys()).thenReturn(null);
        when(query.getTimestamps()).thenReturn(null);
        when(query.getLimit()).thenReturn(null);
        when(query.getGranularity()).thenReturn(null);
        return query;
    }

}
