package org.doraemon.treasure.jdbc.misc;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.doraemon.treasure.jdbc.exception.MetricStoreServerException;
import junit.framework.Assert;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


public class HttpUtilTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void response2JsonObject_statusLineIsNull() throws Exception {

        HttpResponse response = mock(HttpResponse.class);
        when(response.getStatusLine()).thenReturn(null);

        thrown.expect(SQLException.class);
        thrown.expectMessage(startsWith("Can't get the status line from response :"));

        HttpUtil.response2JsonObject(response);
    }

    @Test
    public void response2JsonObject_IOExceptionWhenGetEntity() throws Exception {

        HttpResponse response = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);
        HttpEntity entity = mock(HttpEntity.class);
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);

        IOException ioe = new IOException("mock io exception");
        when(entity.getContent()).thenThrow(ioe);

        thrown.expect(SQLException.class);
        thrown.expect(hasProperty("cause", is(ioe)));
        thrown.expectMessage(startsWith("An error occurs while reading the response body input stream"));

        HttpUtil.response2JsonObject(response);
    }

    @Test
    public void response2JsonObject_statusOKButEmptyContent() throws Exception {

        HttpResponse response = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);
        HttpEntity entity = mock(HttpEntity.class);
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(new byte[]{}));

        thrown.expect(MetricStoreServerException.class);
        thrown.expectMessage(startsWith("remote server returned nothing, but we expected a JSONArray or a JSONObject"));

        HttpUtil.response2JsonObject(response);
    }

    @Test
    public void response2JsonObject_returnJSONObject() throws Exception {

        HttpResponse response = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);
        HttpEntity entity = mock(HttpEntity.class);
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);

        String responseContent = "{\"k1\" : \"v1\", \"k2\" : \"v2\"}";
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(responseContent.getBytes()));

        JSONArray expected = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("k1", "v1");
        jsonObject.put("k2", "v2");
        expected.add(jsonObject);

        Assert.assertEquals(expected, HttpUtil.response2JsonObject(response));
    }

    @Test
    public void response2JsonObject_returnJSONObject_fieldOrder1() throws Exception {

        HttpResponse response = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);
        HttpEntity entity = mock(HttpEntity.class);
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);

        String responseContent = "{\"k1\" : \"v1\", \"k2\" : \"v2\", \"k3\" : \"v3\"}";
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(responseContent.getBytes()));

        JSONArray expected = new JSONArray();
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("k1", "v1");
        jsonObject.put("k2", "v2");
        jsonObject.put("k3", "v3");
        expected.add(jsonObject);

        JSONArray actual = HttpUtil.response2JsonObject(response);
        Assert.assertEquals(expected, actual);
        JSONObject actualJsonObject = (JSONObject) actual.get(0);

        org.junit.Assert.assertArrayEquals(new String[]{"k1", "k2", "k3"}, actualJsonObject.keySet().toArray(new String[0]));
    }

    @Test
    public void response2JsonObject_returnJSONObject_fieldOrder2() throws Exception {

        HttpResponse response = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);
        HttpEntity entity = mock(HttpEntity.class);
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);

        String responseContent = "{\"k2\" : \"v2\", \"k1\" : \"v1\", \"k3\" : \"v3\"}";
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(responseContent.getBytes()));

        JSONArray expected = new JSONArray();
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("k2", "v2");
        jsonObject.put("k1", "v1");
        jsonObject.put("k3", "v3");
        expected.add(jsonObject);

        JSONArray actual = HttpUtil.response2JsonObject(response);
        Assert.assertEquals(expected, actual);
        JSONObject actualJsonObject = (JSONObject) actual.get(0);

        org.junit.Assert.assertArrayEquals(new String[]{"k2", "k1", "k3"}, actualJsonObject.keySet().toArray(new String[0]));
    }

    @Test
    public void response2JsonObject_returnJSONArray() throws Exception {

        HttpResponse response = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);
        HttpEntity entity = mock(HttpEntity.class);
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);

        String responseContent = "[{\"k1\" : \"v1\", \"k2\" : \"v2\"}, {\"k11\" : \"v11\", \"k12\" : \"v12\"}]";
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(responseContent.getBytes()));

        JSONArray expected = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("k1", "v1");
        jsonObject.put("k2", "v2");

        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("k11", "v11");
        jsonObject2.put("k12", "v12");

        expected.add(jsonObject);
        expected.add(jsonObject2);

        Assert.assertEquals(expected, HttpUtil.response2JsonObject(response));
    }

    @Test
    public void response2JsonObject_status400() throws Exception {

        HttpResponse response = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);
        HttpEntity entity = mock(HttpEntity.class);
        when(statusLine.getStatusCode()).thenReturn(400);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);

        String responseContent = "response content400";
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(responseContent.getBytes()));

        thrown.expect(HttpUtilException.class);
        thrown.expect(hasProperty("statusCode", equalTo(400)));
        thrown.expect(hasProperty("responseBody", equalTo(responseContent)));

        HttpUtil.response2JsonObject(response);
    }

    @Test
    public void response2JsonObject_status500() throws Exception {

        HttpResponse response = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);
        HttpEntity entity = mock(HttpEntity.class);
        when(statusLine.getStatusCode()).thenReturn(500);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);

        String responseContent = "response content500";
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(responseContent.getBytes()));

        thrown.expect(HttpUtilException.class);
        thrown.expect(hasProperty("statusCode", equalTo(500)));
        thrown.expect(hasProperty("responseBody", equalTo(responseContent)));

        HttpUtil.response2JsonObject(response);
    }

}
