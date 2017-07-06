package org.doraemon.treasure.jdbc.driver.druid;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.Future;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import org.doraemon.treasure.jdbc.misc.HttpUtil;
import org.doraemon.treasure.jdbc.misc.JDBCURL;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ HttpUtil.class })
public class DruidConnectionTest {

	private RestfulConnection connection;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws SecurityException, IllegalArgumentException, IllegalAccessException, SQLException {
		PowerMockito.mockStatic(HttpUtil.class);

		PowerMockito.when(HttpUtil.getHttpClient()).thenReturn(Mockito.mock(CloseableHttpAsyncClient.class));

		PowerMockito
				.when(HttpUtil.get(Mockito.anyString(), Mockito.any(String.class),
						Mockito.any(FutureCallback.class), Mockito.any(CloseableHttpAsyncClient.class), Mockito.any(RequestConfig.class)))
				.thenAnswer(new Answer<Future<HttpResponse>>() {

					@Override
					public Future<HttpResponse> answer(InvocationOnMock invocation) throws Throwable {
						String json = "[{\"timestamp\":\"2012-01-01T00:00:00.000Z\",\"result\":{\"sample_name1\":\"sample_name1val\",\"sample_name2\":\"sample_name2val\",\"sample_divide\":\"sample_divideval\"}},{\"timestamp\":\"2012-01-02T00:00:00.000Z\",\"result\":{\"sample_name1\":\"sample_name1val\",\"sample_name2\":\"sample_name2val\",\"sample_divide\":\"sample_divideval\"}}]";

						HttpResponse response = new BasicHttpResponse(
								new BasicStatusLine(HttpVersion.HTTP_1_0, 200, ""));
						response.setEntity(new StringEntity(json));

						Future<HttpResponse> future = Mockito.mock(Future.class);

						Mockito.when(future.isDone()).thenReturn(true);
						Mockito.when(future.get()).thenReturn(response);
						return future;
					}

				});
		PowerMockito.when(HttpUtil.response2JsonObject(Mockito.any(HttpResponse.class))).thenCallRealMethod();
		this.connection = new RestfulConnection(new JDBCURL("jdbc:ONEAPM://127.0.0.1:8080/dbname"));
	}

	@Test
	public void prepareStatementTest() throws SQLException, ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		PreparedStatement preparedStatement = connection
				.prepareStatement("select aaa from bbb where timestamps between 10321564 and 215646123");
		preparedStatement.execute();
		ResultSet resultSet = preparedStatement.getResultSet();

		Assert.assertTrue(resultSet.next());

		Assert.assertEquals(resultSet.getDate("timestamp"), new Date(format.parse("2012-01-01").getTime() + 28800000));
		Assert.assertEquals(resultSet.getString("sample_name1"), "sample_name1val");
		Assert.assertEquals(resultSet.getString("sample_name2"), "sample_name2val");
		Assert.assertEquals(resultSet.getString("sample_divide"), "sample_divideval");

		Assert.assertTrue(resultSet.next());
		Assert.assertEquals(resultSet.getDate("timestamp"), new Date(format.parse("2012-01-02").getTime() + 28800000));
		Assert.assertEquals(resultSet.getString("sample_name1"), "sample_name1val");
		Assert.assertEquals(resultSet.getString("sample_name2"), "sample_name2val");
		Assert.assertEquals(resultSet.getString("sample_divide"), "sample_divideval");

	}

	@Test
	public void resultSetMetaDataTest() throws Exception {
		PreparedStatement preparedStatement = connection
				.prepareStatement("select aaa from bbb where timestamps between 10321564 and 215646123");
		preparedStatement.execute();
		ResultSet resultSet = preparedStatement.getResultSet();
		Assert.assertNotNull(resultSet);
		ResultSetMetaData metaData = resultSet.getMetaData();
		Assert.assertNotNull(metaData);

		Assert.assertEquals(metaData.getColumnCount(), 4);

	}

}
