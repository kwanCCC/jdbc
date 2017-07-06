package org.doraemon.treasure.jdbc.driver;

import java.sql.Connection;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import org.doraemon.treasure.jdbc.driver.druid.RestfulConnection;
import org.doraemon.treasure.jdbc.misc.Constans;
import org.doraemon.treasure.jdbc.misc.JDBCURL;


/**
 * 该类中不包含注册行为
 * 
 * url:<a> jdbc:ONEAPM//hostName[:port]/[dbname][?param1=value1][&param2=value2] </a>
 * 
 * 
 * @author 964700108@qq.com
 * 
 * @version 1.0
 *
 */
public class NoRegisterDriver implements java.sql.Driver {

    public static final String majorVersion = "java.sql.driver.majorVersion";
    public static final String minorVersion = "java.sql.driver.minorVersion";

    public final String DEFAULT_SUFFIX = Constans.MAXORPROTOCOL + ":" + Constans.MINORPROTOCOL;

    /**
     * driverManager 获取连接
     */
    public Connection connect(String url, java.util.Properties info) throws SQLException {
        if (acceptsURL(url)) {
            // XXX:使用者传入的配置项,账号?密码?直接舍弃
            // java.util.Properties pro = new java.util.Properties();
            JDBCURL u = new JDBCURL(url);
            // new connection;
            return new RestfulConnection(u);

        }
        return null;
    }

    /**
     * 检查本驱动是否支持连接协议
     */
    public boolean acceptsURL(String url) throws SQLException {
        if (url == null || url.trim().isEmpty()) {
            return false;
        }
        return url.startsWith(DEFAULT_SUFFIX) || url.startsWith(Constans.MAXORPROTOCOL + ":METRIC_STORE");
    }

    /**
     * 返回驱动的一些属性
     */
    public DriverPropertyInfo[] getPropertyInfo(String url, java.util.Properties info) throws SQLException {
        // TODO:参照其他驱动实现
        return null;
    }

    /**
     * 驱动的主版本号
     */
    public int getMajorVersion() {
        Integer majorVersion = Constans.MAJORVERSION;
        if (majorVersion != null) {
            return majorVersion;
        }
        return 0;
    }

    /**
     * 驱动的次版本号
     */
    public int getMinorVersion() {
        Integer minorVersion = Constans.MINORVERSION;
        if (minorVersion != null) {
            return minorVersion;
        }
        return 0;
    }

    /**
     * 是否为真正的JDBC驱动
     */
    public boolean jdbcCompliant() {
        // XXX:不是一个JDBC驱动,仅仅实现了JDBC驱动接口进行转换到HTTP协议的请求
        return false;
    }

    /**
     * JDK7 新加函数,非必须支持
     */
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

}
