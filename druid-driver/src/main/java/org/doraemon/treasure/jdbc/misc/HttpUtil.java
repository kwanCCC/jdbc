package org.doraemon.treasure.jdbc.misc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.Future;

import com.alibaba.fastjson.parser.Feature;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.doraemon.treasure.jdbc.exception.MetricStoreServerException;

public class HttpUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    private static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * http get
     * 
     * @param url
     * @param param
     * @return
     */
    public static final Future<HttpResponse> get(String url, String param, FutureCallback<HttpResponse> callBack, CloseableHttpAsyncClient client) throws SQLException {
        return get(url, param, callBack, client, RequestConfig.DEFAULT);
    }

    public static final Future<HttpResponse> get(String url, String param,
            FutureCallback<HttpResponse> callBack, CloseableHttpAsyncClient client,
            RequestConfig requestConfig) throws SQLException {
        try {
            RequestBuilder builder = RequestBuilder.post()
                                  .setUri(url)
                                  // " JSON text SHALL be encoded in Unicode. The default encoding is UTF-8. "
                                  // See https://tools.ietf.org/html/rfc4627 , section 3
                                  .setEntity(new StringEntity(param, ContentType.APPLICATION_JSON))
                                  .setConfig(requestConfig);
            return client.execute(builder.build(), callBack);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("jdbc driver call http error!", e);
            }
            throw new SQLException(e);
        }
    }

    public static final JSONArray response2JsonObject(HttpResponse response) throws SQLException {
        StatusLine status = response.getStatusLine();

        if(status == null) {
            throw new SQLException("Can't get the status line from response :" + response);
        }

        Integer statusCode = status.getStatusCode();
        String responseContent = "";

        try {
            responseContent = EntityUtils.toString(response.getEntity(), DEFAULT_CHARSET);
        } catch (IOException e) {
            throw new SQLException("An error occurs while reading the response body input stream", e);
        }

        if (statusCode.equals(HttpStatus.SC_OK)) {
            // 请求成功
            if (StringUtils.isNotBlank(responseContent)) {
                responseContent = responseContent.trim();
                if (responseContent.startsWith("{")) {
                    // use Feature.OrderedField to keep json object fields order same as the json text
                    JSONObject returnObject = JSON.parseObject(responseContent, Feature.OrderedField);
                    if (returnObject != null) {
                        JSONArray array = new JSONArray();
                        array.add(returnObject);
                        return array;
                    }
                } else if (responseContent.startsWith("[")) {
                    JSONArray array = JSON.parseArray(responseContent);
                    if (array != null) {
                        return array;
                    }
                }
            }
            throw new MetricStoreServerException("remote server returned nothing, but we expected a JSONArray or a JSONObject");
        } else {
            throw new HttpUtilException(statusCode, responseContent);
        }

    }

    public static CloseableHttpAsyncClient getHttpClient() {
        CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();
        client.start();
        return client;
    }


}
