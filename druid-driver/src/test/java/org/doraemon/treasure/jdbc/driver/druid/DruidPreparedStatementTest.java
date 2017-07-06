package org.doraemon.treasure.jdbc.driver.druid;

import java.sql.SQLException;
import java.util.Properties;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class DruidPreparedStatementTest {

	@Test
	public void testExecuteQueryString() throws SQLException {
		RestfulConnection connection = Mockito.mock(RestfulConnection.class);
		Mockito.when(connection.query(Mockito.anyString())).thenAnswer(new Answer<Object>() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				String sql = invocation.getArgumentAt(0, String.class);
				String sql2 = "SELECT metricId,DOUBLESUM(num1) as num1,DOUBLESUM(num2) as num2, DOUBLESUM(num3) as num3,DOUBLEMIN(num4) as num4,DOUBLEMAX(num5) as num5, DOUBLESUM(num6) as num6 FROM druid_metric WHERE timespamp between 1442217600000 and 1442221200000 and applicationId=847 and (metricTypeId=-17362124 OR metricTypeId=-17362109 OR metricTypeId=-17362126) and (metricId=382528048 OR metricId=158685823 OR metricId=158685824) GROUP BY metricId order by doublesum(num2)/doublesum(num1) asc LIMIT 0 10 BREAK BY 1442217600000 to 3600000 ";
				Assert.assertEquals(sql2, sql);
				return null;
			}
		});
		Properties props = new Properties();
		RestfulPreparedStatement pst = new RestfulPreparedStatement(
				"SELECT metricId,DOUBLESUM(num1) as num1,DOUBLESUM(num2) as num2, DOUBLESUM(num3) as num3,DOUBLEMIN(num4) as num4,DOUBLEMAX(num5) as num5, DOUBLESUM(num6) as num6 FROM druid_metric WHERE timespamp between ? and ? and applicationId=? and (metricTypeId=? OR metricTypeId=? OR metricTypeId=?) and (metricId=? OR metricId=? OR metricId=?) GROUP BY metricId order by doublesum(num2)/doublesum(num1) asc LIMIT ? ? BREAK BY ? to ? ",
				connection, props);

		pst.setLong(1, 1442217600000L);
		pst.setLong(2, 1442221200000L);
		pst.setLong(3, 847L);
		pst.setLong(4, -17362124L);
		pst.setLong(5, -17362109L);
		pst.setLong(6, -17362126L);
		pst.setLong(7, 382528048L);
		pst.setLong(8, 158685823L);
		pst.setLong(9, 158685824L);

		pst.setInt(10, 0);
		pst.setLong(11, 10);

		pst.setLong(12, 1442217600000L);
		pst.setLong(13, 3600000L);

		pst.executeQuery();
	}
}
