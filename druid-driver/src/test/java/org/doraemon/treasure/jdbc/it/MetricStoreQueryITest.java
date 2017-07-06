package org.doraemon.treasure.jdbc.it;

import org.doraemon.treasure.jdbc.driver.druid.RestfulConnection;
import org.doraemon.treasure.jdbc.driver.druid.RestfulPreparedStatement;
import org.doraemon.treasure.jdbc.misc.JDBCURL;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.junit.Rule;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MetricStoreQueryITest {
    Interval interval     = Interval.parse("2016-06-01T15:26:12.116+08:00/2016-06-01T15:56:12.116+08:00");
    String   startTimeStr = interval.getStart().withZone(DateTimeZone.UTC).toString();
    String   endTimeStr   = interval.getEnd().withZone(DateTimeZone.UTC).toString();
    
    private final String timeseriesResultJson =
            "[{" + "\"timestamp\": \"" + startTimeStr + "\"," + "\"result\": {\"name\": \"name1\",\"id\": 1}" + "},"
                    + "{" + "\"timestamp\": \"" + endTimeStr + "\"," + "\"result\": {\"name\": \"name2\",\"id\": 2}"
                    + "}]";
    
    @Rule public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().dynamicPort());
    
    @Test
    public void test_simple_sql_query() throws SQLException {
        stubFor(post(urlEqualTo("/api/query?f=druid"))
                        .willReturn(aResponse().withStatus(SC_OK).withBody(timeseriesResultJson)));
        
        JDBCURL jdbcurl =
                new JDBCURL(String.format("jdbc:METRIC_STORE://localhost:%d/all?f=druid", wireMockRule.port()));
        
        String sql =
                "select doubleSum(num1), doubleMin(num4) from table1 where timespamp between ? and ? and name=? and id=? BREAK BY ? to ?";
        RestfulPreparedStatement pstat =
                new RestfulPreparedStatement(sql, new RestfulConnection(jdbcurl), new Properties());
        
        pstat.setLong(1, 1442217600000L);
        pstat.setLong(2, 1442221200000L);
        pstat.setString(3, "name1");
        pstat.setLong(4, 2L);
        pstat.setLong(5, 1442217600000L);
        pstat.setLong(6, 3600000L);
        ResultSet resultSet = pstat.executeQuery();
        assertTrue(resultSet.next());
        assertEquals(1464765972000L, resultSet.getTime("timestamp").getTime());
        assertTrue(resultSet.next());
        assertFalse(resultSet.next());
    }
    
    
}
