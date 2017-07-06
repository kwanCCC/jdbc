package org.doraemon.treasure.jdbc.driver.druid;

import java.sql.Date;
import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.doraemon.treasure.grammer.parser.exceptions.DruidSQLException;
import com.google.common.base.Preconditions;

public class ResultSetTransfrom {


    public static final JSONArray parser(JSONArray array) {
        JSONArray returnVal = new JSONArray();
        // TODO:不划分结构进行merge
        for (Object object : array) {
            if (object instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) object;
                String timestamp = jsonObject.getString("timestamp");
                if (StringUtils.isNotBlank(timestamp)) {
                    Date date = null;
                    date = parseTimestamp(timestamp);
                    // JSONArray result = jsonObject.getJSONArray("result");
                    Object obj = jsonObject.get("result");
                    if (obj != null) {
                        if (obj instanceof JSONObject) {
                            JSONObject result = (JSONObject) obj;
                            if (isSelectQueryResult(result)) { //select query
                                return mergeSelectQueryResult(result);
                            } else {
                                result.put("timestamp", date); //timeseries
                                returnVal.add(result);
                            }

                        } else if (obj instanceof JSONArray) { //topN
                            JSONArray result = (JSONArray) obj;
                            for (int i = 0; i < result.size(); i++) {
                                result.getJSONObject(i).put("timestamp", date);
                            }
                            returnVal.addAll(result);
                        }
                    } else { // group by
                        JSONObject event = jsonObject.getJSONObject("event");
                        event.put("timestamp", date);
                        if (event != null) {
                            returnVal.add(event);
                        }
                    }
                } else {
                    returnVal.add(object);
                }

            }
        }
        return returnVal;
    }

    private static Date parseTimestamp(String timestamp) {
        java.util.Date d;
        try {
            d = DateFormatUtils.ISO_DATETIME_FORMAT.parse(timestamp);
        } catch (ParseException e) {
            throw new DruidSQLException("cant't parse timestamp : " + timestamp , e);
        }
        return new Date(d.getTime() + 8 * 3600 * 1000);
    }

    private static boolean isSelectQueryResult(JSONObject result) {
        return result.get("pagingIdentifiers") != null;
    }

    private static JSONArray mergeSelectQueryResult(JSONObject result) {
        final JSONArray events = result.getJSONArray("events");
        JSONArray returnEvents = new JSONArray();
        Preconditions.checkArgument(events != null);
        for(Object item: events) {
            JSONObject jsonObject = (JSONObject)item;
            JSONObject event = jsonObject.getJSONObject("event");
            event.put("timestamp", parseTimestamp(event.getString("timestamp")));
            returnEvents.add(event);
        }
        return returnEvents;
    }


}
