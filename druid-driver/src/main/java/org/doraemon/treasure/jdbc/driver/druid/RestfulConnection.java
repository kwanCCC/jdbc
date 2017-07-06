package org.doraemon.treasure.jdbc.driver.druid;


import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.doraemon.treasure.grammer.SqlRunner;
import org.doraemon.treasure.jdbc.driver.wrap.ConnectionWrap;
import org.doraemon.treasure.jdbc.exception.MetricStoreBadRequestException;
import org.doraemon.treasure.jdbc.exception.MetricStoreInternalServerException;
import org.doraemon.treasure.jdbc.exception.MetricStoreServerException;
import org.doraemon.treasure.jdbc.exception.QueryInterruptedException;
import org.doraemon.treasure.jdbc.misc.HttpUtil;
import org.doraemon.treasure.jdbc.misc.HttpUtilException;
import org.doraemon.treasure.jdbc.misc.JDBCURL;

public class RestfulConnection extends ConnectionWrap {

    private static final String CONNECT_TIMEOUT_KEY = "connectTimeout";
    private static final String SOCKET_TIMEOUT_KEY  = "socketTimeout";

    private static final Logger logger              = LoggerFactory.getLogger(RestfulConnection.class);
    private static final String HEALTH_CHECKING_SQL = "SELECT 1";

    private SqlRunner sqlRunner;
    
    private String url;

    private CloseableHttpAsyncClient client;

    private SQLException ex;

    private int connectTimeout = 5 * 1000;
    private int socketTimeout  = 5 * 1000;

    public RestfulConnection(JDBCURL url) {
        Properties properties = new Properties();
        properties.setProperty("minor_protocol", url.getMinorProtocol());
        this.sqlRunner = new SqlRunner(properties);
        this.url = url.toUrl();
        this.client = HttpUtil.getHttpClient();

        String connectTimeout = url.getQueryProperties().getProperty(CONNECT_TIMEOUT_KEY);
        if(connectTimeout != null && !connectTimeout.isEmpty()) {
            this.connectTimeout = Integer.parseInt(connectTimeout);
        }

        String socketTimeout = url.getQueryProperties().getProperty(SOCKET_TIMEOUT_KEY);
        if(socketTimeout != null && !socketTimeout.isEmpty()) {
            this.socketTimeout = Integer.parseInt(socketTimeout);
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("query address is {}", this.url);
        }
    }
    
    public Object query(String sql) throws SQLException {
        if (logger.isDebugEnabled()) {
            logger.debug("query sql is: {}", sql);
        }
        StringTokenizer token = new StringTokenizer(sql, " ");
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(connectTimeout).setSocketTimeout(socketTimeout).build();
        //for spring boot datasource health checking
        // TODO 支持 "SELECT 1" 这种SQL查询时可移除这段代码
        if(HEALTH_CHECKING_SQL.equals(sql)) {
            return pingServer(requestConfig);
        }
        while (token.hasMoreTokens()) {
            String t = token.nextToken();
            // XXX:测试语句使用的表 DUAL
            if ("DUAL".equals(t)) {
                JSONArray array = (JSONArray) JSON.parse(
                        "[{\"timestamp\":\"2012-01-01T00:00:00.000Z\",\"result\":{\"sample_name1\":\"sample_name1\",\"sample_name2\":\"sample_name1\",\"sample_divide\":\"sample_name1\"}},{\"timestamp\":\"2012-01-02T00:00:00.000Z\",\"result\":{\"sample_name1\":\"sample_name1\",\"sample_name2\":\"sample_name1\",\"sample_divide\":\"sample_name1\"}}]");
                ;
                if (logger.isInfoEnabled()) {
                    logger.info("test sql :" + sql + " \t result:" + array);
                }
                return array;
                
            }
        }
        
        // TODO:call sql parser
        String           params = this.sqlRunner.parseSql(sql);
        Future<HttpResponse> future = HttpUtil.get(url, params, null, client, requestConfig);
        if (future != null) {

            HttpResponse response = null;
            try {
                response = future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new QueryInterruptedException("maybe query thread interrupted or query timeout", e);
            }

            try {
                return HttpUtil.response2JsonObject(response);
            } catch (HttpUtilException hue) {
                throw createSQLException(sql, hue.getStatusCode(), hue.getResponseBody());
            }
        }
        throw new SQLException("unknown exception!!!");
    }
    
    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return new RestfulPreparedStatement(sql, this, null);
    }
    
    @Override
    public Statement createStatement() throws SQLException {
        return new RestfulPreparedStatement(null, this, null);
    }
    
    @Override
    public void close() throws SQLException {
        try {
            client.close();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    private SQLException createSQLException(String sql, int statusCode, String content) {
        switch (statusCode) {
            case HttpStatus.SC_BAD_REQUEST:
                return new MetricStoreBadRequestException(
                        "Metric Store 查询错误, 不合法的查询请求, 请检查SQL及其他请求参数. 请求执行的SQL是 : [" + sql
                        + "], 请求返回的响应内容是: " + content);
            case HttpStatus.SC_INTERNAL_SERVER_ERROR:
                return new MetricStoreInternalServerException(
                        "Metric Store 内部错误. 请求执行的SQL是 : [" + sql + "], 请求返回的响应内容是: " + content);
            default:
                return new MetricStoreServerException("查询错误,返回状态码:" + statusCode + ".请求执行的SQL是 : ["
                                                      + sql + "], 请求返回的响应内容是: " + content);
        }
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        this.socketTimeout = milliseconds;
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return this.socketTimeout;
    }

    @Override
    public DatabaseMetaData getMetaData() {
        return new RestfulDatabaseMetaData();
    }

    private JSONArray pingServer(RequestConfig requestConfig) {
        JSONArray array = new JSONArray();
        try {
            Future<HttpResponse> future = HttpUtil.get(url, "", null, client, requestConfig);
            future.get();
        } catch (SQLException | InterruptedException | ExecutionException e) {
            logger.warn("cant't reach remote server, return an empty JSONArray", e);
            return array;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("column", 1);
        array.add(jsonObject);
        return array;
    }
}
