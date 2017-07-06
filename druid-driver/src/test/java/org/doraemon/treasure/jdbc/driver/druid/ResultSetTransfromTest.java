package org.doraemon.treasure.jdbc.driver.druid;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.text.ParseException;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.Before;
import org.junit.Test;

import com.alibaba.fastjson.JSONArray;
import org.doraemon.treasure.grammer.parser.exceptions.DruidSQLException;

import junit.framework.Assert;

public class ResultSetTransfromTest {

    private static final String SELECT_RESULT =
    "[\n"
    + "    {\n"
    + "        \"timestamp\": \"2013-01-01T00:00:00.000Z\",\n"
    + "        \"result\": {\n"
    + "            \"pagingIdentifiers\": {\n"
    + "                \"wikipedia_2012-12-29T00:00:00.000Z_2013-01-10T08:00:00.000Z_2013-01-10T08:13:47.830Z_v9\": 4\n"
    + "            },\n"
    + "            \"events\": [\n"
    + "                {\n"
    + "                    \"segmentId\": \"wikipedia_editstream_2012-12-29T00:00:00.000Z_2013-01-10T08:00:00.000Z_2013-01-10T08:13:47.830Z_v9\",\n"
    + "                    \"offset\": 0,\n"
    + "                    \"event\": {\n"
    + "                        \"timestamp\": \"2013-01-01T00:00:00.000Z\",\n"
    + "                        \"robot\": \"1\",\n"
    + "                        \"namespace\": \"article\",\n"
    + "                        \"anonymous\": \"0\",\n"
    + "                        \"unpatrolled\": \"0\",\n"
    + "                        \"page\": \"11._korpus_(NOVJ)\",\n"
    + "                        \"language\": \"sl\",\n"
    + "                        \"newpage\": \"0\",\n"
    + "                        \"user\": \"EmausBot\",\n"
    + "                        \"count\": 1,\n"
    + "                        \"added\": 39,\n"
    + "                        \"delta\": 39,\n"
    + "                        \"variation\": 39,\n"
    + "                        \"deleted\": 0\n"
    + "                    }\n"
    + "                },\n"
    + "                {\n"
    + "                    \"segmentId\": \"wikipedia_2012-12-29T00:00:00.000Z_2013-01-10T08:00:00.000Z_2013-01-10T08:13:47.830Z_v9\",\n"
    + "                    \"offset\": 1,\n"
    + "                    \"event\": {\n"
    + "                        \"timestamp\": \"2013-01-01T00:00:00.000Z\",\n"
    + "                        \"robot\": \"0\",\n"
    + "                        \"namespace\": \"article\",\n"
    + "                        \"anonymous\": \"0\",\n"
    + "                        \"unpatrolled\": \"0\",\n"
    + "                        \"page\": \"112_U.S._580\",\n"
    + "                        \"language\": \"en\",\n"
    + "                        \"newpage\": \"1\",\n"
    + "                        \"user\": \"MZMcBride\",\n"
    + "                        \"count\": 1,\n"
    + "                        \"added\": 70,\n"
    + "                        \"delta\": 70,\n"
    + "                        \"variation\": 70,\n"
    + "                        \"deleted\": 0\n"
    + "                    }\n"
    + "                }\n"
    + "            ]\n"
    + "        }\n"
    + "    }\n"
    + "]"
    ;

    private JSONArray resultJsonArray;

    @Before
    public void setUp() {
        resultJsonArray = JSONArray.parseArray(SELECT_RESULT);
    }

    @Test
    public void testParser() throws ParseException {
        String dateStr = "2015-09-14T14:15:00.000Z";
        java.util.Date d = DateFormatUtils.ISO_DATETIME_FORMAT.parse(dateStr);
        java.util.Date dd = new java.util.Date(d.getTime() + 8 * 3600 * 1000);
        Assert.assertEquals(22, dd.getHours());
    }

    @Test
    public void testParserThrowExceptionWhenInvalidTimestampReceived() {
        String invalidTimestamp = "invalidTimestamp";
        resultJsonArray.getJSONObject(0).put("timestamp", invalidTimestamp);
        try {
            ResultSetTransfrom.parser(resultJsonArray);
            fail(DruidSQLException.class.getName() + "expected, but no exception thrown");
        } catch (DruidSQLException e) {
            assertThat(e.getMessage(), containsString("cant't parse timestamp : " + invalidTimestamp));
        }
    }

    @Test
    public void testParserThrowExceptionWhenInvalidSelectQueryResultReceived() {
        String invalidTimestamp = "invalidTimestamp";
        resultJsonArray.getJSONObject(0)
                       .getJSONObject("result")
                       .getJSONArray("events")
                       .getJSONObject(0)
                       .getJSONObject("event")
                       .put("timestamp", invalidTimestamp);
        try {
            ResultSetTransfrom.parser(resultJsonArray);
            fail(DruidSQLException.class.getName() + "expected, but no exception thrown");
        } catch (DruidSQLException e) {
            assertThat(e.getMessage(), containsString("cant't parse timestamp : " + invalidTimestamp));
        }
    }

}
