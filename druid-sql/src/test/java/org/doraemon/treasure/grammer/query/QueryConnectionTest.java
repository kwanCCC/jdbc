/*package com.blueocn.druid.query;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.message.BasicStatusLine;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;



@RunWith(MockitoJUnitRunner.class)
public class QueryConnectionTest {

    @InjectMocks
    private QueryConnection          connection;

    @Mock
    private CloseableHttpAsyncClient client;

    @Mock
    private Properties               properties;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() {
        properties.put(QueryProperties.URL, "/testcase");
        Mockito.when(client.execute(Mockito.any(HttpUriRequest.class), Mockito.any(FutureCallback.class))).thenAnswer(new Answer<Future<HttpResponse>>() {

            @Override
            public Future<HttpResponse> answer(final InvocationOnMock invocation) throws Throwable {
                new Thread() {
                    public void run() {
                        // 模拟请求耗时,完成
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            invocation.getArgumentAt(1, FutureCallback.class).failed(e);
                            return;
                        }
                        invocation.getArgumentAt(1, FutureCallback.class).completed(null);
                    };
                }.start();
                return new Future<HttpResponse>() {

                    @Override
                    public boolean isDone() {
                        return true;
                    }

                    @Override
                    public boolean isCancelled() {
                        return false;
                    }

                    @Override
                    public HttpResponse get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                        return null;
                    }

                    @Override
                    public HttpResponse get() throws InterruptedException, ExecutionException {
                        HttpResponse response = Mockito.mock(HttpResponse.class);
                        Mockito.when(response.getStatusLine()).thenAnswer(new Answer<StatusLine>() {

                            @Override
                            public StatusLine answer(InvocationOnMock invocation) throws Throwable {
                                return new BasicStatusLine(new HttpVersion(1, 0), 200, null);
                            }
                        });
                        Mockito.when(response.getEntity()).thenAnswer(new Answer<HttpEntity>() {
                            @Override
                            public HttpEntity answer(InvocationOnMock invocation) throws Throwable {
                                return new StringEntity("[{\"name\":\"zhangsan\",\"age\":15},{\"name\":\"lisi\",\"age\":18}]", "utf-8");
                            }

                        });
                        return response;
                    }

                    @Override
                    public boolean cancel(boolean mayInterruptIfRunning) {
                        return false;
                    }
                };
            }

        });

    }

    @Test
    public void queryTest() {
        JSONObject param = new JSONObject();
        param.put("AA", "SSS");
        Object queryResult = connection.query(param);
        Assert.assertNotNull(queryResult);
        Assert.assertEquals(queryResult.getClass(), JSONArray.class);
        JSONArray array = (JSONArray) queryResult;
        Assert.assertEquals(array.size(), 2);

        JSONObject object = array.getJSONObject(0);
        Assert.assertNotNull(object);

        Assert.assertEquals(object.getString("name"), "zhangsan");
        Assert.assertEquals(object.getInteger("age"), new Integer(15));

        JSONObject two = array.getJSONObject(1);
        Assert.assertNotNull(two);

        Assert.assertEquals(two.getString("name"), "lisi");
        Assert.assertEquals(two.getInteger("age"), new Integer(18));


    }
}
*/