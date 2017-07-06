package org.doraemon.treasure.jdbc.driver.druid;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.doraemon.treasure.jdbc.driver.wrap.ResultSetMetaDataWrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class RestfulResultSetMetaData extends ResultSetMetaDataWrap {

    private static final Logger logger = LoggerFactory.getLogger(RestfulResultSetMetaData.class);

    private JSONArray           array;
    private List<String>        header;

    public RestfulResultSetMetaData(List<String> header, JSONArray array) {
        this.array = array;
        this.header = header;
        if (logger.isDebugEnabled()) {
            if (header != null) {
                Map<Integer, String> headerMap = new HashMap<Integer, String>();
                for (int i = 0; i < header.size(); i++) {
                    String head = header.get(i);
                    headerMap.put(i, head);
                }
                logger.debug("headers:" + JSON.toJSONString(headerMap, true));
            } else {
                logger.debug("resultSetMetaData headers is null");
            }
        }
    }

    @Override
    public String getColumnClassName(int column) throws SQLException {
        for (Object object : array) {
            if (object != null) {
                if (object instanceof JSONObject) {
                    JSONObject jsonObject = (JSONObject) object;
                    Object value = jsonObject.get(header.get(column - 1));
                    if (value != null) {
                        return value.getClass().getName();
                    }
                }
            }

        }
        throw new SQLException("result metadata getColumnClassName error object is null");
    }

    @Override
    public int getColumnCount() throws SQLException {
        return header.size();
    }

    @Override
    public String getColumnLabel(int column) throws SQLException {
        return getColumnName(column);
    }

    @Override
    public String getColumnName(int column) throws SQLException {
        return header.get(column - 1);
    }

    @Override
    public int getColumnType(int column) throws SQLException {
        if (header.size() > column - 1) {
            Object obj = null;
            for (Object object : array) {
                if (object instanceof JSONObject) {
                    if (object != null) {
                        obj = ((JSONObject) object).get(header.get(column - 1));
                        if (obj != null) {
                            break;
                        }
                    }
                }
            }
            if (obj != null) {
                if (logger.isDebugEnabled()) {
                    Map<String, Object> jsonMap = new HashMap<String, Object>();
                    jsonMap.put("index", column);
                    if (header.size() > column - 1) {
                        jsonMap.put("name", header.get(column - 1));
                    }
                    jsonMap.put("result", obj);
                    jsonMap.put("type", obj.getClass());

                    logger.debug("dao framwork get columnType:\n" + JSON.toJSONString(jsonMap, true));
                }
                if (obj instanceof String) {
                    return Types.VARCHAR;
                } else if (obj instanceof Double || obj instanceof BigDecimal) {
                    return Types.DOUBLE;
                } else if (obj instanceof Float) {
                    return Types.FLOAT;
                } else if (obj instanceof Integer || obj instanceof Short) {
                    return Types.INTEGER;
                } else if (obj instanceof Long) {
                    return Types.BIGINT;
                } else if (obj instanceof Date) {
                    return Types.DATE;
                } else if (obj instanceof Timestamp) {
                    return Types.TIMESTAMP;
                } else if (obj instanceof Character) {
                    return Types.CHAR;
                } else if (obj instanceof Boolean) {
                    return Types.BOOLEAN;
                }
            }
        }

        throw new SQLException("not found column:{index:" + column + ",headers:" + Arrays.toString(header.toArray()) + "}");
    }

}
