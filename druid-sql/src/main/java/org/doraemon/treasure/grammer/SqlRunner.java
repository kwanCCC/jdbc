package org.doraemon.treasure.grammer;

import org.doraemon.treasure.grammer.backends.IJSONCompiler;
import org.doraemon.treasure.grammer.backends.metricstore.compilers.GroupByQueryCompiler;
import org.doraemon.treasure.grammer.backends.metricstore.compilers.SelectQueryCompiler;
import org.doraemon.treasure.grammer.backends.metricstore.compilers.TimeSeriesQueryCompiler;
import org.doraemon.treasure.grammer.backends.metricstore.compilers.TopNQueryCompiler;
import org.doraemon.treasure.grammer.parser.ParserEngine;
import org.doraemon.treasure.grammer.parser.Query;
import org.doraemon.treasure.grammer.parser.exceptions.DruidSQLException;
import org.apache.commons.lang3.StringUtils;
import org.doraemon.treasure.grammer.backends.druid.compiler.GroupByCompiler;
import org.doraemon.treasure.grammer.backends.druid.compiler.SelectCompiler;
import org.doraemon.treasure.grammer.backends.druid.compiler.TimeSeriesCompiler;
import org.doraemon.treasure.grammer.backends.druid.compiler.TopNCompiler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * druid查询封装,可以注入到spring中
 *
 */
public class SqlRunner {
    private Logger logger = LoggerFactory.getLogger(SqlRunner.class);

    private String protocol;
    private List<IJSONCompiler> compilers = new ArrayList<>();

    /**
     * 初始化一个Sql执行容器
     *
     * @param properties <see>QueryProperties</see>
     */
    public SqlRunner(Properties properties) {
        this.protocol = properties.getProperty("minor_protocol", "");
        if (logger.isDebugEnabled()) {
            logger.debug("using protocol: " + this.protocol);
        }

        switch (this.protocol) {
            case "METRIC_STORE":
                this.compilers.add(new TimeSeriesQueryCompiler());
                this.compilers.add(new TopNQueryCompiler());
                this.compilers.add(new GroupByQueryCompiler());
                this.compilers.add(new SelectQueryCompiler()); //注意顺序
                break;
            default: // for druid
                this.compilers.add(new TimeSeriesCompiler());
                this.compilers.add(new TopNCompiler());
                this.compilers.add(new GroupByCompiler());
                this.compilers.add(new SelectCompiler());
        }
    }

    public String parseSql(String sql) throws SQLException {
        Query query = ParserEngine.parse(sql);
        if(query == null) {
            throw new SQLException("Found a null query object parsed from sql: " + sql);
        }
        String json = null;
        for (IJSONCompiler compiler : this.compilers) {
            if (compiler.canCompile(query)) {
                json = compiler.compile(query);
                break;
            }
        }

        if(StringUtils.isBlank(json)) {
            throw new DruidSQLException("query object can't be serialized to a nonempty json string, "
                                        + "it's probably that the sql is not a supported query type of TimeSeries, TopN, GroupBy and Select. sql: " + sql);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("sql parser json, json:" + json);
        }
        return json;
    }
}
