package org.doraemon.treasure.grammer;

import static org.doraemon.treasure.grammer.JSONAssert.*;
import static junit.framework.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.doraemon.treasure.grammer.parser.exceptions.DruidSQLException;

public class SqlRunnerTest {


    private SqlRunner metricstoreSqlRunNer;
    private SqlRunner sqlRunner;

    @Before
    public void setUp() throws Exception {
        metricstoreSqlRunNer = createMetricStoreSqlRunner();
        sqlRunner = createDruidSqlRunner();
    }

    @Test
    public void test_parse_select_sql_metricstore() throws Exception {
        String sql = "select num1, uid from table1 where timestamps between 1 and 2 and id <> 1";
        final String json = metricstoreSqlRunNer.parseSql(sql);
        assertNotNull(json);
    }

    @Test
    public void test_return_success_when_parse_groupby_sql_with_metricstore() throws Exception {
        String sql = "select longSum(uid) from table1 where timestamps between 1 and 2 group by uid order by uid limit 5 break by 1000";
        final String json = metricstoreSqlRunNer.parseSql(sql);
        assertNotNull(json);
    }

    @Test
    public void test_return_success_when_parse_groupby_sql_without_limit() throws Exception {
        String sql = "select longSum(uid) from table1 where timestamps between 1 and 2 group by uid break by 1000";
        final String json = metricstoreSqlRunNer.parseSql(sql);

        JSONObject jo = JSON.parseObject(json);
        nonExist(jo, "limit");
    }

    @Test
    public void test_return_success_when_parse_groupby_sql_without_limit_use_druid() throws Exception {
        String sql = "select longSum(uid) from table1 where timestamps between 1 and 2 group by uid break by 1000";
        final String json = sqlRunner.parseSql(sql);

        JSONObject jo = JSON.parseObject(json);
        nonExist(jo, "limitSpec");
    }

    @Test
    public void test_parse_select_sql_for_druid() throws Exception {

        String sql = "select num1 as num, uid from table1 where timestamps between 1 and 2 and id <> 1";
        final String json = sqlRunner.parseSql(sql);
        assertNotNull(json);
    }

    @Test
    public void test_parse_select_sql_with_in() throws Exception {
        String sql = "select num1 as num, uid from table1 where timestamps between 1 and 2 and id in 1";
        final String json = metricstoreSqlRunNer.parseSql(sql);
        assertNotNull(json);
    }

    @Test
    public void test_parse_select_sql_with_in_multi_value() throws Exception {
        String sql = "select num1 as num, uid from table1 where timestamps between 1 and 2 and id = 1 and id in (1, 3, 2)";
        final String json = metricstoreSqlRunNer.parseSql(sql);
        assertNotNull(json);
    }

    @Test
    public void test_parse_select_sql_with_not_in() throws Exception {
        String sql = "select num1 as num, uid from table1 where timestamps between 1 and 2 and id = 1 and id not in (1, 3, 2)";
        final String json = metricstoreSqlRunNer.parseSql(sql);
        assertNotNull(json);
    }

    @Test
    public void test_parse_select_sql_with_distinct() throws Exception {

        String sql = "select distinct num1 as num, uid from table1 where timestamps between 1 and 2 and id = 1 and id not in (1, 3, 2)";
        final String json = metricstoreSqlRunNer.parseSql(sql);
        assertNotNull(json);
    }

    @Test
    public void test_parse_select_sql_with_distinct_by_druid() throws Exception {

        String sql = "select distinct num1 as num, uid from table1 where timestamps between 1 and 2 and id = 1 and id not in (1, 3, 2)";
        final String json = sqlRunner.parseSql(sql);
        final JSONObject groupByQuery = JSON.parseObject(json);
        assertEquals("groupBy", groupByQuery.getString("queryType"));
    }

    @Test
    public void test_parse_sql_limit_with_ref_column() throws Exception {
        String sql = "\n" +
                "select app_id, LONGSUM(total_num) as total\n" +
                "from httptransaction\n" +
                "where timestamp BETWEEN 1 and 2\n" +
                "and app_id = 1\n" +
                "group by app_id\n" +
                "order by total\n" +
                "limit 10\n" +
                "BREAK BY 3600000\n";
        final String json = metricstoreSqlRunNer.parseSql(sql);
        assertNotNull(json);
    }

    @Test
    public void test_parse_sql_limit_with_ref_column_refer_agg_column() throws Exception {
        String sql = "\n" +
                "select app_id, LONGSUM(total_num) / LONGSUM(app_id)  as total\n" +
                "from httptransaction\n" +
                "where timestamp BETWEEN 1 and 2\n" +
                "and app_id = 1\n" +
                "group by app_id\n" +
                "order by total\n" +
                "limit 10\n" +
                "BREAK BY 3600000\n";
        final String json = metricstoreSqlRunNer.parseSql(sql);
        assertNotNull(json);
    }

    @Test
    public void test_parse_sql_to_topN_with_break_by_is_eq_interval() throws Exception {
        String sql = "\n" +
                "select app_id, LONGSUM(total_num) / LONGSUM(app_id)  as total\n" +
                "from httptransaction\n" +
                "where timestamp BETWEEN 1 and 2\n" +
                "and app_id = 1\n" +
                "group by app_id\n" +
                "order by total\n" +
                "limit 10\n" +
                "BREAK BY 1\n";
        final String json = metricstoreSqlRunNer.parseSql(sql);
        JSONObject jo = JSON.parseObject(json);
        JSONAssert.eq(jo, "type", "topn");
    }

    @Test
    public void test_parse_sql_to_groupBy_with_break_by_is_not_eq_interval() throws Exception {
        String sql = "\n" +
                "select app_id, LONGSUM(total_num) / LONGSUM(app_id)  as total\n" +
                "from httptransaction\n" +
                "where timestamp BETWEEN 1 and 2\n" +
                "and app_id = 1\n" +
                "group by app_id\n" +
                "order by total\n" +
                "limit 10\n" +
                "BREAK BY 4000\n";
        final String json = metricstoreSqlRunNer.parseSql(sql);
        JSONObject jo = JSON.parseObject(json);
        JSONAssert.eq(jo, "type", "groupby");
    }

  @Test
    public void test_parse_sql_to_topN_with_break_by_is_all() throws Exception {
        String sql = "\n" +
                "select app_id, LONGSUM(total_num) / LONGSUM(app_id)  as total\n" +
                "from httptransaction\n" +
                "where timestamp BETWEEN 1 and 2\n" +
                "and app_id = 1\n" +
                "group by app_id\n" +
                "order by total\n" +
                "limit 10\n" +
                "BREAK BY all\n";
        final String json = metricstoreSqlRunNer.parseSql(sql);
        JSONObject jo = JSON.parseObject(json);
        JSONAssert.eq(jo, "type", "topn");
    }

    @Test
    public void test_parse_sql_to_timeseries_without_break_by() throws Exception {
        String sql = "select count(1) as errorCount from extra_data where timestamp between 1 and 2";
        String json = metricstoreSqlRunNer.parseSql(sql);
        JSONObject jo = JSON.parseObject(json);
        JSONAssert.eq(jo, "type", "timeseries");
    }

    @Test
    public void test_parse_sql_to_groupby_with_break_by_is_day() throws Exception {
        String sql = "\n" +
                "select app_id, LONGSUM(total_num) / LONGSUM(app_id)  as total\n" +
                "from httptransaction\n" +
                "where timestamp BETWEEN 1 and 2\n" +
                "and app_id = 1\n" +
                "group by app_id\n" +
                "order by total\n" +
                "limit 10\n" +
                "BREAK BY DAY\n";
        final String json = metricstoreSqlRunNer.parseSql(sql);
        JSONObject jo = JSON.parseObject(json);
        JSONAssert.eq(jo, "type", "groupby");
    }

    @Test
    public void test_parse_sql_to_metricStore_groupby_without_break_by() throws Exception {
        String sql = "select app_id, LONGSUM(total_num) / LONGSUM(app_id)  as total\n" +
                     "from httptransaction\n" +
                     "where timestamp BETWEEN 1 and 2\n" +
                     "and app_id = 1\n" +
                     "group by app_id\n";
        // for clickhouse
        String json = metricstoreSqlRunNer.parseSql(sql);
        JSONObject jo = JSON.parseObject(json);
        JSONAssert.eq(jo, "type", "groupby");
        // default is "break by all", and generate only 1 point
        JSONAssert.eq(jo, "points", 1);

        // for druid
        json = sqlRunner.parseSql(sql);
        jo = JSON.parseObject(json);
        JSONAssert.eq(jo, "queryType", "groupBy");
        // default is "break by all", and granularity should be ALL
        JSONAssert.eq(jo, "granularity", "ALL");
    }


    @Test
    public void test_parse_sql_limit_with_ref_column_using_druid() throws Exception {
        String sql = "\n" +
                "select app_id, LONGSUM(total_num) as total\n" +
                "from httptransaction\n" +
                "where timestamp BETWEEN 1 and 2\n" +
                "and app_id = 1\n" +
                "group by app_id\n" +
                "order by total\n" +
                "limit 10\n" +
                "BREAK BY 3600000\n";
        final String json = sqlRunner.parseSql(sql);
        assertNotNull(json);
    }

    @Test
    public void test_parse_select_sql_with_in_with_100_values() throws Exception {
        StringBuilder sql = new StringBuilder("select num1 as num, uid from table1 where timestamps between 1 and 2 and id = 1 and id in (0");
        for(int i = 1; i < 100; i++) {
            sql.append(", ").append(i);
        }
        sql.append(")");

        final String json = sqlRunner.parseSql(sql.toString());
        assertNotNull(json);
    }

    @Test
    public void testTimeSeriesQueryWithLimit() throws Exception {
        String sql = "select longsum(total_time) from t "
                     + "where timestamp between 1481079720000 and 1481083320000 and application_id=1 "
                     + "limit 0 10 "
                     + "break by 1481079720000 to 3600000";

        try {
            sqlRunner.parseSql(sql);
            fail("parsing a time series query with a limit clause should throw DruidSQLException");
        } catch (DruidSQLException e) {
            assertTrue(e.getMessage().startsWith("query object can't be serialized to a nonempty json string, it's probably that the sql is not a supported query type of TimeSeries, TopN, GroupBy and Select"));
        }

    }

    @Test
    public void test_parse_like_sql_with_metric_store() throws Exception {
        String sql = "SELECT longsum(total_time) from t " + "where timestamp between 1481079720000 and 1481083320000 "
                             + "and application_id=1 and metric_name like '%test_metric_name' ";
        String jsonString = metricstoreSqlRunNer.parseSql(sql);
        assertNotNull(jsonString);
        JSONObject jsonObject = JSON.parseObject(jsonString);
        exists(jsonObject, "filter");
        eq(jsonObject, "filter", new HashMap<String, Object>() {{
            put("type", "and");
            put("filters", new ArrayList<Object>() {{
                add(new HashMap<String, Object>() {{
                    put("type", "equal");
                    put("column", new JSONObject(new HashMap<String, Object>() {{
                        put("type", "long");
                        put("field", "application_id");
                    }}));
                    put("value", 1);
                }});
                add(new HashMap<String, Object>() {{
                    put("type", "like");
                    put("column", new JSONObject(new HashMap<String, Object>() {{
                        put("type", "string");
                        put("field", "metric_name");
                    }}));
                    put("value", "%test_metric_name");
                }});
            }});
        }});
    }

    private SqlRunner createMetricStoreSqlRunner() {
        final Properties properties = new Properties();
        properties.setProperty("minor_protocol", "METRIC_STORE");
        return new SqlRunner(properties);
    }

    private SqlRunner createDruidSqlRunner() {
        final Properties properties = new Properties();
        return new SqlRunner(properties);
    }
}
