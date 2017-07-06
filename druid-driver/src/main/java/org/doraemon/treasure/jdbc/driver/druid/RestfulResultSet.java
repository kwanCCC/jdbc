package org.doraemon.treasure.jdbc.driver.druid;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.doraemon.treasure.jdbc.driver.wrap.ResultSetWrap;

public class RestfulResultSet extends ResultSetWrap {
	private Iterator<Object> it;

	private JSONObject indexObject;

	private List<String> head;

	private RestfulResultSetMetaData resultSetMetaData;

	public RestfulResultSet(Object obj) {
		throw new RuntimeException();
	}

	public RestfulResultSet(JSONObject object) {
		this(new JSONArray(Arrays.asList((Object) object)));
	}

	public RestfulResultSet(JSONArray array) {
		array = ResultSetTransfrom.parser(array);
		head = new LinkedList<String>();
		for (Object object : array) {
			if (object instanceof JSONObject) {
				JSONObject jsonObject = (JSONObject) object;

				for (Map.Entry<String, Object> resultEntity : jsonObject.entrySet()) {
					String key = resultEntity.getKey();
					if (!head.contains(key)) {
						head.add(key);
					}
				}
			}
		}
		this.it = array.iterator();
		if (array.size() > 0) {
			this.resultSetMetaData = new RestfulResultSetMetaData(head, array);
		} else {
			this.resultSetMetaData = new RestfulResultSetMetaData(new ArrayList<String>(), new JSONArray());
		}
	}

	@Override
	public boolean next() throws SQLException {
		if (it.hasNext()) {
			Object rowObject = it.next();

			if(!(rowObject instanceof JSONObject)) {
				throw new RuntimeException("expect a JSONObject, but an object of type "
					+ rowObject.getClass().getCanonicalName() + " returned");
			}

			indexObject = (JSONObject) rowObject;
			return true;
		}
		return false;
	}

	@Override
	public byte getByte(int columnIndex) throws SQLException {
		return getByte(head.get(getRealIndex(columnIndex)));
	}

	@Override
	public byte getByte(String columnLabel) throws SQLException {
		return indexObject.getByteValue(columnLabel);
	}

	@Override
	public short getShort(int columnIndex) throws SQLException {
		return getShort(head.get(getRealIndex(columnIndex)));
	}

	@Override
	public short getShort(String columnLabel) throws SQLException {
		return indexObject.getShortValue(columnLabel);
	}

	@Override
	public int getInt(int columnIndex) throws SQLException {
		return getInt(head.get(getRealIndex(columnIndex)));
	}

	@Override
	public int getInt(String columnLabel) throws SQLException {
		return indexObject.getIntValue(columnLabel);
	}

	@Override
	public long getLong(int columnIndex) throws SQLException {
		return getLong(head.get(getRealIndex(columnIndex)));
	}

	@Override
	public long getLong(String columnLabel) throws SQLException {
		return indexObject.getLongValue(columnLabel);
	}

	@Override
	public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		return getBigDecimal(head.get(getRealIndex(columnIndex)));
	}

	@Override
	public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
		return indexObject.getBigDecimal(columnLabel);
	}

	@Override
	public double getDouble(int columnIndex) throws SQLException {
		return getDouble(head.get(getRealIndex(columnIndex)));
	}

	@Override
	public double getDouble(String columnLabel) throws SQLException {
		return indexObject.getDoubleValue(columnLabel);
	}

	@Override
	public float getFloat(int columnIndex) throws SQLException {
		return getFloat(head.get(getRealIndex(columnIndex)));
	}

	@Override
	public float getFloat(String columnLabel) throws SQLException {
		return indexObject.getFloatValue(columnLabel);
	}

	@Override
	public String getString(int columnIndex) throws SQLException {
		return getString(head.get(getRealIndex(columnIndex)));
	}

	@Override
	public String getString(String columnLabel) throws SQLException {
		return indexObject.getString(columnLabel);
	}

	@Override
	public Date getDate(int columnIndex) throws SQLException {
		return getDate(head.get(getRealIndex(columnIndex)));
	}

	@Override
	public Date getDate(String columnLabel) throws SQLException {
		Long time = indexObject.getDate(columnLabel).getTime();
		return new Date(time);
	}

	@Override
	public Time getTime(int columnIndex) throws SQLException {
		return getTime(head.get(getRealIndex(columnIndex)));
	}

	@Override
	public Time getTime(String columnLabel) throws SQLException {
		Long time = indexObject.getDate(columnLabel).getTime();
		return new Time(time);
	}

	@Override
	public boolean getBoolean(int columnIndex) throws SQLException {
		return getBoolean(head.get(getRealIndex(columnIndex)));
	}

	@Override
	public boolean getBoolean(String columnLabel) throws SQLException {
		return indexObject.getBooleanValue(columnLabel);
	}

	@Override
	public Object getObject(int columnIndex) throws SQLException {
		return getObject(head.get(getRealIndex(columnIndex)));
	}

	@Override
	public Object getObject(String columnLabel) throws SQLException {
		return indexObject.get(columnLabel);
	}

	@Override
	public String toString() {
		return it.toString();
	}

	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		if (this.resultSetMetaData == null) {
			throw new SQLException("get metaData error!!! result is null");
		}
		return this.resultSetMetaData;
	}

	private int getRealIndex(int columnIndex) {
		if (columnIndex <= 0) {
			return 0;
		}
		return columnIndex - 1;
	}

}
