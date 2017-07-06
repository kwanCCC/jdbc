package org.doraemon.treasure.jdbc.driver.druid;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.doraemon.treasure.jdbc.misc.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.doraemon.treasure.jdbc.driver.wrap.PreparedStatementWrap;

public class RestfulPreparedStatement extends PreparedStatementWrap {
	
	private Logger logger = LoggerFactory.getLogger(RestfulPreparedStatement.class);

	private String sql;

	private List<String> paramters;

	private ResultSet resultSet;

	private RestfulConnection connection;

	public RestfulPreparedStatement(String sql, RestfulConnection connection, Properties properties) {
        int count = 0;
        if (!StringUtils.isEmpty(sql)) {
            count = StringUtils.countMatches(sql, "?");
            this.sql = sql.replace("?", "%s");
        }
        this.connection = connection;
        paramters = new ArrayList<String>(count);
    }

	@Override
	public ResultSet executeQuery() throws SQLException {
		// TODO:parser sql exec
		String sql = String.format(this.sql.replace("?", "%s"), paramters.toArray());
		Object object = this.connection.query(sql);
		if (object instanceof JSONArray) {
			return new RestfulResultSet((JSONArray) object);
		} else if (object instanceof JSONObject) {
			return new RestfulResultSet((JSONObject) object);
		}
		return null;
	}

	@Override
	public ResultSet executeQuery(String sql) throws SQLException {
		int count = 0;
		if (!StringUtils.isEmpty(sql)) {
			count = StringUtils.countMatches(sql, "?");
			this.sql = sql.replace("?", "%s");
		}
		paramters = new ArrayList<String>(count);
		return executeQuery();
	}

	@Override
	public boolean execute() throws SQLException {
		this.resultSet = executeQuery();
		return this.resultSet != null;
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		return this.resultSet;
	}

	@Override
	public void setByte(int parameterIndex, byte x) throws SQLException {
		setShort(parameterIndex, x);
	}

	@Override
	public void setShort(int parameterIndex, short x) throws SQLException {
		setInt(parameterIndex, x);
	}

	@Override
	public void setInt(int parameterIndex, int x) throws SQLException {
		setLong(parameterIndex, x);
	}

	@Override
	public void setLong(int parameterIndex, long x) throws SQLException {
		paramters.add(parameterIndex - 1, String.valueOf(x));
	}

	@Override
	public void setFloat(int parameterIndex, float x) throws SQLException {
		setDouble(parameterIndex, x);
	}

	@Override
	public void setDouble(int parameterIndex, double x) throws SQLException {
		paramters.add(parameterIndex - 1, String.valueOf(x));
	}

	@Override
	public void setString(int parameterIndex, String x) throws SQLException {
		if(x == null) {
			paramters.add(parameterIndex - 1, null);
		} else {
			paramters.add(parameterIndex - 1, "\'" + StringEscapeUtils.escape(x) + "\'");
		}
	}

	@Override
	public void setObject(int parameterIndex, Object x) throws SQLException {
		paramters.add(parameterIndex - 1, String.valueOf(x));
	}

	@Override
	public void setDate(int parameterIndex, Date x) throws SQLException {
		paramters.add(parameterIndex - 1, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(x));
	}

}
