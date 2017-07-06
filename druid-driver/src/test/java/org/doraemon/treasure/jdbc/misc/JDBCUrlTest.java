package org.doraemon.treasure.jdbc.misc;

import java.sql.SQLException;
import java.util.Properties;

import org.junit.Test;

import org.doraemon.treasure.jdbc.exception.UrlEmptyException;
import org.doraemon.treasure.jdbc.exception.UrlParserException;

import junit.framework.Assert;

public class JDBCUrlTest {


    @Test(expected = UrlEmptyException.class)
    public void emptyUrlTest() throws SQLException {
        // empty url
        new JDBCURL("");
    }

    @Test(expected = UrlParserException.class)
    public void majorProtocolTest() throws SQLException {
        String url = "jdbc:ONEAPM://127.0.0.1/dbname";
        JDBCURL jdbcUrl = new JDBCURL(url);
        Assert.assertEquals("jdbc", jdbcUrl.getMajorProtocol());

        try {
            // Only protocol error
            new JDBCURL("jdbc:");
            Assert.fail("error jdbc url is pass");
        } catch (SQLException e) {
            Assert.assertEquals(e.getMessage(), "jdbc url[jdbc:] is error!");
            throw e;
        }
    }

    @Test(expected = UrlParserException.class)
    public void minjorProtocolTest() throws SQLException {

        String url = "jdbc:ONEAPM://127.0.0.1/dbname";
        JDBCURL jdbcUrl = new JDBCURL(url);
        Assert.assertEquals("jdbc", jdbcUrl.getMajorProtocol());
        Assert.assertEquals("ONEAPM", jdbcUrl.getMinorProtocol());

        try {
            // Not complete jdbc url
            new JDBCURL("jdbc:ONEAPM");
            Assert.fail("error jdbc url is pass");
        } catch (SQLException e) {
            Assert.assertEquals(e.getMessage(), "jdbc url[jdbc:ONEAPM] is error!");
            throw e;
        }
    }

    @Test(expected = UrlParserException.class)
    public void hostTest() throws SQLException {
        String url = "jdbc:ONEAPM://127.0.0.1:8080/dbname";
        JDBCURL jdbcUrl = new JDBCURL(url);
        Assert.assertEquals("jdbc", jdbcUrl.getMajorProtocol());
        Assert.assertEquals("ONEAPM", jdbcUrl.getMinorProtocol());
        Assert.assertEquals("127.0.0.1", jdbcUrl.getHost());
        Assert.assertEquals(8080, jdbcUrl.getPort().intValue());

        url = "jdbc:ONEAPM://127.0.0.1/dbname";
        jdbcUrl = new JDBCURL(url);
        Assert.assertEquals("jdbc", jdbcUrl.getMajorProtocol());
        Assert.assertEquals("ONEAPM", jdbcUrl.getMinorProtocol());
        Assert.assertEquals("127.0.0.1", jdbcUrl.getHost());
        Assert.assertEquals(80, jdbcUrl.getPort().intValue());

        try {
            new JDBCURL("jdbc:ONEAPM://127.0.0.1:8080");
            Assert.fail("error jdbc url is pass");
        } catch (SQLException e) {
            Assert.assertEquals(e.getMessage(), "jdbc url[jdbc:ONEAPM://127.0.0.1:8080] is error!");
            throw e;
        }
    }

    @Test
    public void dbNameTest() throws SQLException {
        String url = "jdbc:ONEAPM://127.0.0.1:8080/dbname";
        JDBCURL jdbcUrl = new JDBCURL(url);
        Assert.assertEquals("jdbc", jdbcUrl.getMajorProtocol());
        Assert.assertEquals("ONEAPM", jdbcUrl.getMinorProtocol());
        Assert.assertEquals("127.0.0.1", jdbcUrl.getHost());
        Assert.assertEquals(8080, jdbcUrl.getPort().intValue());

        Assert.assertEquals("dbname", jdbcUrl.getDBName());

        url = "jdbc:ONEAPM://127.0.0.1/dbname/aaa/bbb";
        jdbcUrl = new JDBCURL(url);
        Assert.assertEquals("jdbc", jdbcUrl.getMajorProtocol());
        Assert.assertEquals("ONEAPM", jdbcUrl.getMinorProtocol());
        Assert.assertEquals("127.0.0.1", jdbcUrl.getHost());
        Assert.assertEquals(80, jdbcUrl.getPort().intValue());
        Assert.assertEquals("dbname/aaa/bbb", jdbcUrl.getDBName());

    }

    @Test
    public void queryStrTest() throws SQLException {
        String url = "jdbc:ONEAPM://127.0.0.1:8080/dbname?aaa=bbb";
        JDBCURL jdbcUrl = new JDBCURL(url);
        Assert.assertEquals("jdbc", jdbcUrl.getMajorProtocol());
        Assert.assertEquals("ONEAPM", jdbcUrl.getMinorProtocol());
        Assert.assertEquals("127.0.0.1", jdbcUrl.getHost());
        Assert.assertEquals(8080, jdbcUrl.getPort().intValue());
        Assert.assertEquals("dbname", jdbcUrl.getDBName());
        Assert.assertEquals("aaa=bbb", jdbcUrl.getQueryStr());

        url = "jdbc:ONEAPM://127.0.0.1/dbname/aaa/bbb?aaa=bbb&sss=ddd";
        jdbcUrl = new JDBCURL(url);
        Assert.assertEquals("jdbc", jdbcUrl.getMajorProtocol());
        Assert.assertEquals("ONEAPM", jdbcUrl.getMinorProtocol());
        Assert.assertEquals("127.0.0.1", jdbcUrl.getHost());
        Assert.assertEquals(80, jdbcUrl.getPort().intValue());
        Assert.assertEquals("dbname/aaa/bbb", jdbcUrl.getDBName());
        Assert.assertEquals("aaa=bbb&sss=ddd", jdbcUrl.getQueryStr());

    }

    @Test
    public void testQueryProperties() throws SQLException {
        Properties queryProps = new Properties();

        // single k-v pair
        String url = "jdbc:ONEAPM://127.0.0.1:8080/dbname?aaa=bbb";
        JDBCURL jdbcUrl = new JDBCURL(url);
        queryProps.clear();
        queryProps.put("aaa", "bbb");
        Assert.assertEquals(queryProps, jdbcUrl.getQueryProperties());

        // double k-v pair
        url = "jdbc:ONEAPM://127.0.0.1/dbname/aaa/bbb?aaa=bbb&sss=ddd";
        jdbcUrl = new JDBCURL(url);
        queryProps.clear();
        queryProps.put("aaa", "bbb");
        queryProps.put("sss", "ddd");
        Assert.assertEquals(queryProps, jdbcUrl.getQueryProperties());

        // empty value of first k-v pair
        url = "jdbc:ONEAPM://127.0.0.1/dbname/aaa/bbb?aaa=&sss=ddd";
        jdbcUrl = new JDBCURL(url);
        queryProps.clear();
        queryProps.put("sss", "ddd");
        Assert.assertEquals(queryProps, jdbcUrl.getQueryProperties());

        // emtpy name of first k-v pair
        url = "jdbc:ONEAPM://127.0.0.1/dbname/aaa/bbb?=bbb&sss=ddd";
        jdbcUrl = new JDBCURL(url);
        queryProps.clear();
        queryProps.put("sss", "ddd");
        Assert.assertEquals(queryProps, jdbcUrl.getQueryProperties());

        // empty value of last k-v pair
        url = "jdbc:ONEAPM://127.0.0.1/dbname/aaa/bbb?aaa=bbb&sss=";
        jdbcUrl = new JDBCURL(url);
        queryProps.clear();
        queryProps.put("aaa", "bbb");
        Assert.assertEquals(queryProps, jdbcUrl.getQueryProperties());

        // emtpy name of first k-v pair
        url = "jdbc:ONEAPM://127.0.0.1/dbname/aaa/bbb?aaa=bbb&=ddd";
        jdbcUrl = new JDBCURL(url);
        queryProps.clear();
        queryProps.put("aaa", "bbb");
        Assert.assertEquals(queryProps, jdbcUrl.getQueryProperties());

    }

}
