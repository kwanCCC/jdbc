package org.doraemon.treasure.jdbc.driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class DriverTest {

    @Before
    public void setUp() {
        try {
            Class.forName("org.doraemon.treasure.jdbc.driver.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("加载驱动文件错误", e);
        }
    }

    @Test
    public void getConnectionTest() throws SQLException {
        String jdbcUrl = "jdbc:ONEAPM://localhost:3366/druid/v2";
        Connection connection = DriverManager.getConnection(jdbcUrl);
        Assert.assertNotNull(connection);
    }

    @Test
    public void getPrepareStatementTest() throws SQLException {
        String jdbcUrl = "jdbc:ONEAPM://localhost:3366/druid/v2";
        String sql =
                "select longsum(count) as count,doublesum(domTime) as domTime from druid_beacon where timespamp between 1438358400000 and 1438704000000 and applicationId=1300 and userId=4885 group by city order by count limit 0 50";
        Connection connection = DriverManager.getConnection(jdbcUrl);
        Assert.assertNotNull(connection);

        PreparedStatement statement = connection.prepareStatement(sql);
        Assert.assertNotNull(statement);
    }

}
