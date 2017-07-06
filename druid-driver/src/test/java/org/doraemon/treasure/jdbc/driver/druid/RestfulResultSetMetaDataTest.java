package org.doraemon.treasure.jdbc.driver.druid;

import java.sql.*;

import org.junit.Assert;
import org.junit.Test;

import com.alibaba.fastjson.JSONArray;

public class RestfulResultSetMetaDataTest {

	@Test
	public void test() throws SQLException {
		String result = "[{\"timestamp\":\"2015-10-10T08:00:00.000Z\",\"result\":[{\"APDEXSAT\":13571,\"APP\":0,\"NETWORK\":1167165,\"DOM\":8689611,\"EXTRA\":0,\"CALLCOUNT\":26044,\"FEELTIME\":284361,\"hostId\":null,\"PAGE\":64703241920,\"QUEUE\":0,\"DOMLOADINGTIME\":269169,\"CALLCOUNTNUM\":31545,\"ALLTIME\":64711899890,\"WEB\":0,\"APDEXTOL\":0,\"DOMCONTENTLOADEDEVENTSTART\":536560,\"APDEXFRU\":17974,\"LOADEVENTSTART\":252458},{\"APDEXSAT\":319,\"APP\":0,\"NETWORK\":25604,\"DOM\":190905,\"EXTRA\":0,\"CALLCOUNT\":467,\"FEELTIME\":6103,\"hostId\":\"95\",\"PAGE\":1342756399,\"QUEUE\":0,\"DOMLOADINGTIME\":5623,\"CALLCOUNTNUM\":692,\"ALLTIME\":1342944232,\"WEB\":0,\"APDEXTOL\":0,\"DOMCONTENTLOADEDEVENTSTART\":11676,\"APDEXFRU\":373,\"LOADEVENTSTART\":5632},{\"APDEXSAT\":248,\"APP\":0,\"NETWORK\":20202,\"DOM\":150348,\"EXTRA\":0,\"CALLCOUNT\":382,\"FEELTIME\":4949,\"hostId\":\"93\",\"PAGE\":1072763425,\"QUEUE\":0,\"DOMLOADINGTIME\":4716,\"CALLCOUNTNUM\":546,\"ALLTIME\":1072911644,\"WEB\":0,\"APDEXTOL\":0,\"DOMCONTENTLOADEDEVENTSTART\":9995,\"APDEXFRU\":298,\"LOADEVENTSTART\":4422},{\"APDEXSAT\":34,\"APP\":0,\"NETWORK\":2664,\"DOM\":19662,\"EXTRA\":0,\"CALLCOUNT\":52,\"FEELTIME\":659,\"hostId\":\"21\",\"PAGE\":136793020,\"QUEUE\":0,\"DOMLOADINGTIME\":786,\"CALLCOUNTNUM\":72,\"ALLTIME\":136812173,\"WEB\":0,\"APDEXTOL\":0,\"DOMCONTENTLOADEDEVENTSTART\":1189,\"APDEXFRU\":38,\"LOADEVENTSTART\":556},{\"APDEXSAT\":37,\"APP\":0,\"NETWORK\":2886,\"DOM\":21474,\"EXTRA\":0,\"CALLCOUNT\":50,\"FEELTIME\":695,\"hostId\":\"19\",\"PAGE\":147593224,\"QUEUE\":0,\"DOMLOADINGTIME\":678,\"CALLCOUNTNUM\":78,\"ALLTIME\":147614081,\"WEB\":0,\"APDEXTOL\":0,\"DOMCONTENTLOADEDEVENTSTART\":1452,\"APDEXFRU\":41,\"LOADEVENTSTART\":597}]}]";
		RestfulResultSet resultSet = new RestfulResultSet(JSONArray.parseArray(result));
		ResultSetMetaData metaData = resultSet.getMetaData();

		Assert.assertNotNull(metaData);
		Assert.assertEquals(metaData.getColumnCount(), 19);

		for (int i = 0; i < metaData.getColumnCount(); i++) {
			int index = i + 1;
			String columName = metaData.getColumnName(index);
			switch (columName) {
			case "timestamp":
				Assert.assertEquals(Types.DATE, metaData.getColumnType(index));
				break;
			case "APDEXSAT":
				Assert.assertEquals(Types.INTEGER, metaData.getColumnType(index));
				break;
			case "NETWORK":
				Assert.assertEquals(Types.INTEGER, metaData.getColumnType(index));
				break;
			case "DOM":
				Assert.assertEquals(Types.INTEGER, metaData.getColumnType(index));
				break;
			case "EXTRA":
				Assert.assertEquals(Types.INTEGER, metaData.getColumnType(index));
				break;
			case "CALLCOUNT":
				Assert.assertEquals(Types.INTEGER, metaData.getColumnType(index));
				break;
			case "FEELTIME":
				Assert.assertEquals(Types.INTEGER, metaData.getColumnType(index));
				break;
			case "hostId":
				Assert.assertEquals(Types.VARCHAR, metaData.getColumnType(index));
				break;
			case "PAGE":
				Assert.assertEquals(Types.BIGINT, metaData.getColumnType(index));
				break;
			case "QUEUE":
				Assert.assertEquals(Types.INTEGER, metaData.getColumnType(index));
				break;
			case "DOMLOADINGTIME":
				Assert.assertEquals(Types.INTEGER, metaData.getColumnType(index));
				break;
			case "CALLCOUNTNUM":
				Assert.assertEquals(Types.INTEGER, metaData.getColumnType(index));
				break;
			case "ALLTIME":
				Assert.assertEquals(Types.BIGINT, metaData.getColumnType(index));
				break;
			case "WEB":
				Assert.assertEquals(Types.INTEGER, metaData.getColumnType(index));
				break;
			case "APDEXTOL":
				Assert.assertEquals(Types.INTEGER, metaData.getColumnType(index));
				break;
			case "DOMCONTENTLOADEDEVENTSTART":
				Assert.assertEquals(Types.INTEGER, metaData.getColumnType(index));
				break;
			case "APDEXFRU":
				Assert.assertEquals(Types.INTEGER, metaData.getColumnType(index));
				break;
			case "LOADEVENTSTART":
				Assert.assertEquals(Types.INTEGER, metaData.getColumnType(index));
				break;
			case "APP":
				Assert.assertEquals(Types.INTEGER, metaData.getColumnType(index));
				break;
			default:
				Assert.fail("get result type error " + columName);

			}
		}
	}

	@Test
	public void test_select_query_result_set() throws Exception {
		RestfulResultSet resultSet = new RestfulResultSet(JSONArray.parseArray(selectReturnJson));
		ResultSetMetaData metaData = resultSet.getMetaData();

		Assert.assertNotNull(metaData);
		Assert.assertEquals(metaData.getColumnCount(), 14);

		resultSet.next();
		Assert.assertNotNull(resultSet.getString("robot"));
		Assert.assertNotNull(resultSet.getString("page"));
		Assert.assertNotNull(resultSet.getDate("timestamp"));
	}

	@Test
	public void emptyResultTest() throws SQLException {
		// 如果result为empty
		String result = "[]";
		RestfulResultSet resultSet = new RestfulResultSet(JSONArray.parseArray(result));
		Assert.assertNotNull(resultSet);
		ResultSetMetaData metaData = resultSet.getMetaData();
		Assert.assertNotNull(metaData);
		Assert.assertEquals(0, metaData.getColumnCount());
	}


	private String selectReturnJson = "[{\"timestamp\":\"2013-01-01T00:00:00.000Z\",\"result\":{\"pagingIdentifiers\":{\"wikipedia_2012-12-29T00:00:00.000Z_2013-01-10T08:00:00.000Z_2013-01-10T08:13:47.830Z_v9\":4},\"events\":[{\"segmentId\":\"wikipedia_editstream_2012-12-29T00:00:00.000Z_2013-01-10T08:00:00.000Z_2013-01-10T08:13:47.830Z_v9\",\"offset\":0,\"event\":{\"timestamp\":\"2013-01-01T00:00:00.000Z\",\"robot\":\"1\",\"namespace\":\"article\",\"anonymous\":\"0\",\"unpatrolled\":\"0\",\"page\":\"11._korpus_(NOVJ)\",\"language\":\"sl\",\"newpage\":\"0\",\"user\":\"EmausBot\",\"count\":1,\"added\":39,\"delta\":39,\"variation\":39,\"deleted\":0}},{\"segmentId\":\"wikipedia_2012-12-29T00:00:00.000Z_2013-01-10T08:00:00.000Z_2013-01-10T08:13:47.830Z_v9\",\"offset\":4,\"event\":{\"timestamp\":\"2013-01-01T00:00:00.000Z\",\"robot\":\"0\",\"namespace\":\"article\",\"anonymous\":\"0\",\"unpatrolled\":\"0\",\"page\":\"113_U.S._756\",\"language\":\"en\",\"newpage\":\"1\",\"user\":\"MZMcBride\",\"count\":1,\"added\":68,\"delta\":68,\"variation\":68,\"deleted\":0}}]}}]";
}
