package org.doraemon.treasure.jdbc.driver.druid;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.doraemon.treasure.jdbc.exception.MetricStoreInternalServerException;
import org.doraemon.treasure.jdbc.exception.QueryInterruptedException;
import org.doraemon.treasure.jdbc.misc.HttpUtil;
import org.doraemon.treasure.jdbc.misc.HttpUtilException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.doraemon.treasure.jdbc.exception.MetricStoreBadRequestException;
import org.doraemon.treasure.jdbc.exception.MetricStoreServerException;
import org.doraemon.treasure.jdbc.misc.JDBCURL;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpUtil.class})
public class RestfulConnectionTest {

    // the sql must have where clause with timestamp conditions
    private static final String defaultSql = "select a, b from t where timestamp between 1 and 2";

    private JDBCURL defaultUrl;

//    private RestfulConnection restfulConnection;
    private RestfulConnection restfulConnection;

    @Rule
    public ExpectedException thrown = ExpectedException.none();


    @Before
    public void setUp() throws SQLException {
        PowerMockito.mockStatic(HttpUtil.class);
        defaultUrl = new JDBCURL("jdbc:ONEAPM://127.0.0.1:8080/dbname");
        restfulConnection = new RestfulConnection(defaultUrl);
    }

    @Test
    public void query_nullFuture() throws SQLException {
        when(HttpUtil.get(eq(defaultUrl.toUrl()), any(String.class), any(FutureCallback.class),
                any(CloseableHttpAsyncClient.class), any(RequestConfig.class))).thenReturn(null);

        thrown.expect(SQLException.class);
        thrown.expectMessage("unknown exception!!!");

        restfulConnection.query(defaultSql);
    }

    @Test
    public void query_futureGetThrownInterruptedException()
            throws SQLException, ExecutionException, InterruptedException {
        Future future = mock(Future.class);
        when(future.isDone()).thenReturn(true);
        when(future.get()).thenThrow(new InterruptedException("ine"));
        when(HttpUtil.get(eq(defaultUrl.toUrl()), any(String.class), any(FutureCallback.class),
                any(CloseableHttpAsyncClient.class), any(RequestConfig.class))).thenReturn(future);

        thrown.expect(QueryInterruptedException.class);
        thrown.expectMessage("maybe query thread interrupted or query timeout");

        restfulConnection.query(defaultSql);
    }

    @Test
    public void query_HttpUtilThrownCommonSQLException()
            throws SQLException, ExecutionException, InterruptedException {
        Future future = mock(Future.class);
        HttpResponse response = mock(HttpResponse.class);
        when(future.isDone()).thenReturn(true);
        when(future.get()).thenReturn(response);
        when(HttpUtil.get(eq(defaultUrl.toUrl()), any(String.class), any(FutureCallback.class),
                any(CloseableHttpAsyncClient.class), Mockito.any(RequestConfig.class))).thenReturn(future);
        when(HttpUtil.response2JsonObject(response))
                .thenThrow(new SQLException("common response body"));

        thrown.expect(SQLException.class);
        thrown.expectMessage("common response body");

        restfulConnection.query(defaultSql);
    }

    @Test
    public void query_HttpUtilThrownSQLException400()
            throws SQLException, ExecutionException, InterruptedException {
        Future future = mock(Future.class);
        HttpResponse response = mock(HttpResponse.class);
        when(future.isDone()).thenReturn(true);
        when(future.get()).thenReturn(response);
        when(HttpUtil.get(eq(defaultUrl.toUrl()), any(String.class), any(FutureCallback.class),
                any(CloseableHttpAsyncClient.class), any(RequestConfig.class))).thenReturn(future);
        when(HttpUtil.response2JsonObject(response)).thenThrow(new HttpUtilException(400, "400 response body"));

        thrown.expect(MetricStoreBadRequestException.class);
        thrown.expectMessage(containsString(defaultSql));
        thrown.expectMessage(containsString("400 response body"));

        restfulConnection.query(defaultSql);
    }

    @Test
    public void query_HttpUtilThrownSQLException500()
            throws SQLException, ExecutionException, InterruptedException {
        Future future = mock(Future.class);
        HttpResponse response = mock(HttpResponse.class);
        when(future.isDone()).thenReturn(true);
        when(future.get()).thenReturn(response);
        when(HttpUtil.get(eq(defaultUrl.toUrl()), any(String.class), any(FutureCallback.class),
                any(CloseableHttpAsyncClient.class), any(RequestConfig.class))).thenReturn(future);
        when(HttpUtil.response2JsonObject(response)).thenThrow(new HttpUtilException(500, "500 response body"));

        thrown.expect(MetricStoreInternalServerException.class);
        thrown.expectMessage(containsString(defaultSql));
        thrown.expectMessage(containsString("500 response body"));

        restfulConnection.query(defaultSql);
    }

    @Test
    public void query_HttpUtilThrownSQLExceptionUnhandledStatusCode()
            throws SQLException, ExecutionException, InterruptedException {
        Future future = mock(Future.class);
        HttpResponse response = mock(HttpResponse.class);
        when(future.isDone()).thenReturn(true);
        when(future.get()).thenReturn(response);
        when(HttpUtil.get(eq(defaultUrl.toUrl()), any(String.class), any(FutureCallback.class),
                any(CloseableHttpAsyncClient.class), any(RequestConfig.class))).thenReturn(future);
        when(HttpUtil.response2JsonObject(response)).thenThrow(new HttpUtilException(502, "502 response body"));

        thrown.expect(MetricStoreServerException.class);
        thrown.expectMessage(containsString(defaultSql));
        thrown.expectMessage(containsString("502 response body"));
        thrown.expectMessage(containsString("返回状态码:502"));

        restfulConnection.query(defaultSql);
    }


    @Test
    public void query_timeout() throws SQLException, ExecutionException, InterruptedException {
        int timeout = 1000;
        JDBCURL url = new JDBCURL("jdbc:ONEAPM://127.0.0.1:8080/dbname?connectionTimeout=" + timeout);
        RestfulConnection restfulConnection = new RestfulConnection(url);

        final Future future = mock(Future.class);
        when(future.get()).thenThrow(new ExecutionException("ex", new RuntimeException()));
        when(HttpUtil.get(eq(url.toUrl()), any(String.class), any(FutureCallback.class),
                          any(CloseableHttpAsyncClient.class), any(RequestConfig.class))).then(new Answer() {
            public Object answer(InvocationOnMock invocation) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return future;
            }
        });

        thrown.expect(QueryInterruptedException.class);
        thrown.expectMessage("maybe query thread interrupted or query timeout");
        restfulConnection.query(defaultSql);
    }

    @Test
    public void testSpringBootHealthCheckingQuery_valid() throws SQLException, ExecutionException, InterruptedException {
        Future future = mock(Future.class);
        HttpResponse response = mock(HttpResponse.class);
        when(future.get()).thenReturn(response);
        when(HttpUtil.get(eq(defaultUrl.toUrl()), any(String.class), any(FutureCallback.class),
                          any(CloseableHttpAsyncClient.class), any(RequestConfig.class))).thenReturn(future);

        Object result = restfulConnection.query("SELECT 1");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("column", 1);
        JSONArray array = new JSONArray();
        array.add(jsonObject);

        assertThat(result, instanceOf(JSONArray.class));
        assertThat(((JSONArray)result), is(array));
    }

    @Test
    public void testSpringBootHealthCheckingQuery_invalid() throws SQLException, ExecutionException, InterruptedException {
        Future future = mock(Future.class);
        HttpResponse response = mock(HttpResponse.class);
        when(future.get()).thenReturn(response);
        when(HttpUtil.get(eq(defaultUrl.toUrl()), any(String.class), any(FutureCallback.class),
                          any(CloseableHttpAsyncClient.class), any(RequestConfig.class))).thenThrow(new SQLException());

        Object result = restfulConnection.query("SELECT 1");

        assertThat(result, instanceOf(JSONArray.class));
        assertThat(((JSONArray)result).size(), is(0));
    }

    @Test
    public void testGetMetaData() throws SQLException {
        assertThat(restfulConnection.getMetaData().getDatabaseProductName(), is("MetricStore_Druid"));
    }

}
