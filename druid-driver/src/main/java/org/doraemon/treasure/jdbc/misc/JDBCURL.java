package org.doraemon.treasure.jdbc.misc;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Queue;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.doraemon.treasure.jdbc.exception.UrlEmptyException;
import org.doraemon.treasure.jdbc.exception.UrlParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JDBC URL的抽象
 *
 * @author 964700108@qq.com
 *
 * @version 1.0
 *
 */
public class JDBCURL {

	private static final Logger logger = LoggerFactory.getLogger(JDBCURL.class);

	private String majorProtocol;

	private String minorProtocol;

	private String hosts;

	private Integer port;

	private String dbname;

	private String queryString;

	private Properties queryProperties = new Properties();

	/**
	 * 对JDBC URL的封装解析,格式错误将抛出<see>MalformedURLException</see>
	 *
	 * @param url
	 * @throws MalformedURLException
	 *             错误的URL格式 jdbc:子协议//hosts[:port]/dbname?queryString
	 */
	public JDBCURL(String url) throws SQLException {
		if (StringUtils.isNotBlank(url)) {
			if (logger.isDebugEnabled()) {
				logger.debug("accept JDBC URL[{}]", url);
			}
			StringTokenizer tokenizer = new StringTokenizer(url, "/");
			if (tokenizer.countTokens() > 2) {
				// 最少存在2个
				int pos = 0;
				while (tokenizer.hasMoreTokens()) {
					pos++;
					String part = tokenizer.nextToken();
					if (StringUtils.isNotBlank(part)) {
						if (pos == 1) {
							acceptProtocol(part);
						} else if (pos == 2) {
							acceptHost(part);
						} else {
							// url
							acceptDbName(tokenizer, part);
						}
					}
				}
				return;
			} else {
				throw new UrlParserException("jdbc url[%s] is error!", url);
			}
		}
		throw new UrlEmptyException("jdbc url is empty");
	}

	/**
	 * 获取jdbc主协议 常量JDBC
	 *
	 * @author 964700108@qq.com
	 * @return
	 */
	public String getMajorProtocol() {
		return this.majorProtocol;
	}

	/**
	 * 获取JDBC次协议 由框架进行制定命名 如:mysql,oneapm 等
	 *
	 * @author 964700108@qq.com
	 * @return
	 */
	public String getMinorProtocol() {
		return this.minorProtocol;
	}

	/**
	 * 获取JDBC链接的Host 如: db-server,127.0.0.1
	 *
	 * @author 964700108@qq.com
	 * @return
	 */
	public String getHost() {
		return this.hosts;
	}

	/**
	 * 获取JDBC链接的端口 如: 3366,8448
	 *
	 * @author 964700108@qq.com
	 * @return
	 */
	public Integer getPort() {
		return this.port;
	}

	/**
	 * 获取JDBC连接的DBName
	 *
	 * @author 964700108@qq.com
	 * @return
	 */
	public String getDBName() {
		return this.dbname;
	}

	/**
	 * 获取JDBC携带的参数
	 *
	 * @author 964700108@qq.com
	 * @return
	 */
	public String getQueryStr() {
		return this.queryString;
	}

	/**
	 * 获取JDBC携带的参数解析后的properties对象, 如果url没有设置query部分则返回一个没有内容的Properties对象, 不会返回刚null
	 *
	 * @return
	 */
	public Properties getQueryProperties() {
		return this.queryProperties;
	}

	public String toUrl() {
		//TO METRIC STORE
        if(this.minorProtocol.equals("METRIC_STORE")) {
            if (StringUtils.isBlank(this.queryString)) {
                return String.format("%s://%s:%d/%s", Constans.CALPROTOCOL, this.hosts, this.port, "api/query");
            }
            return String.format("%s://%s:%d/%s?%s", Constans.CALPROTOCOL, this.hosts, this.port, "api/query",
                    this.queryString
            );
        } else {
            if (StringUtils.isBlank(this.queryString)) {
                return String.format("%s://%s:%d/%s", Constans.CALPROTOCOL, this.hosts, this.port, this.dbname);
            }
            return String.format("%s://%s:%d/%s?%s", Constans.CALPROTOCOL, this.hosts, this.port, this.dbname,
                    this.queryString
            );
        }
	}

	private void acceptProtocol(String protocol) throws SQLException {
		if (StringUtils.isNotBlank(protocol)) {
			// 各个协议
			StringTokenizer protocolToken = new StringTokenizer(protocol, ":");
			if (protocolToken.countTokens() != 2) {
				logger.error("accept jdbc protocol[{}] error!", protocol);
				throw new SQLException("accept jdbc protocol[" + protocol + "] error!");
			}
			this.majorProtocol = protocolToken.nextToken();
			// TODO:validate minorProtocol
			String minorProtocol = protocolToken.nextToken();
			if (minorProtocol.equals(Constans.MINORPROTOCOL) || minorProtocol.equals("METRIC_STORE")) {
				this.minorProtocol = minorProtocol;
			}  else {
				logger.error("jdbc url minorprotocol is error");
				throw new SQLException("jdbc url minorprotocol is error");
			}
		}
	}

	private void acceptHost(String host) throws SQLException {
		if (StringUtils.isNotBlank(host)) {
			StringTokenizer hostToken = new StringTokenizer(host, ":");
			if (hostToken.hasMoreTokens()) {
				this.hosts = hostToken.nextToken();
			}
			if (hostToken.hasMoreTokens()) {
				String portStr = hostToken.nextToken();
				if (NumberUtils.isNumber(portStr)) {
					this.port = Integer.valueOf(portStr);
				}
			} else {
				this.port = 80;
			}
		}
	}

	private void acceptDbName(StringTokenizer dbNameToken, String currentPart) throws SQLException {
		Queue<String> que = new LinkedList<String>();

		if (!dbNameToken.hasMoreTokens()) {
			// last
			int index = currentPart.indexOf("?");
			if (index != -1) {
				acceptQueryStr(currentPart.substring(index + 1));
				currentPart = currentPart.substring(0, index);

			}
			que.offer(currentPart);
		} else {
			if (que.offer(currentPart)) {
				while (dbNameToken.hasMoreTokens()) {
					String urlPart = dbNameToken.nextToken();
					if (StringUtils.isNotBlank(urlPart)) {
						if (!dbNameToken.hasMoreTokens()) {
							// last?
							int index = urlPart.indexOf("?");
							if (index != -1) {
								acceptQueryStr(urlPart.substring(index + 1));
								urlPart = urlPart.substring(0, index);
							}
						}
						if (!que.offer(urlPart)) {
							throw new SQLException("parser jdbc url error! url Too long");
						}
					}
				}
			} else {
				throw new SQLException("parser jdbc url error! queue offer is false");
			}
		}

		if (que.size() > 0) {
			StringBuilder builder = new StringBuilder();
			for (String str : que) {
				builder.append("/").append(str);
			}
			this.dbname = builder.substring(1);
		} else {
			throw new UrlParserException("parser jdbc url[%s] error!", que);
		}
	}

    private void acceptQueryStr(String queryStr) {
        this.queryString = queryStr;

        Properties props = new Properties();

        StringTokenizer queryParams = new StringTokenizer(queryStr, "&");

        while (queryParams.hasMoreTokens()) {
            String parameterValuePair = queryParams.nextToken();

            int indexOfEquals = StringUtils.indexOfIgnoreCase(parameterValuePair, "=");

            String parameter = null;
            String value = null;

            if (indexOfEquals != -1) {
                parameter = parameterValuePair.substring(0, indexOfEquals);

                if (indexOfEquals + 1 < parameterValuePair.length()) {
                    value = parameterValuePair.substring(indexOfEquals + 1);
                }
            }

            if ((value != null && value.length() > 0)  && (parameter != null && parameter.length() > 0)) {
                try {
                    props.put(parameter, URLDecoder.decode(value, "UTF-8"));
                } catch (UnsupportedEncodingException | NoSuchMethodError badEncoding) {
                    logger.warn("cant't decode url with UTF-8 encoding, try again with platform default encoding");
                    props.put(parameter, URLDecoder.decode(value));
                }
			}
        }
        this.queryProperties = props;
    }

}
