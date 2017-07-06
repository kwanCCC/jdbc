package org.doraemon.treasure.jdbc.misc;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

/**
 * 提供一些便捷的提取方法
 * 
 * @author <a href="mailto:zhangjianyf@oneapm.com">zhangjianyf@oneapm.com</a>
 *
 */
public class PropertiesProxy extends Properties {

    /**
     * 
     */
    private static final long serialVersionUID = 5922140838283180488L;

    public Integer getInt(String key) {
        return getInt(key, null);
    }

    public Integer getInt(String key, Integer defaultVal) {
        String val = getProperty(key);
        if (StringUtils.isEmpty(val)) {
            return defaultVal;
        }
        if (NumberUtils.isNumber(val)) {
            return Integer.valueOf(val);
        }
        return null;
    }

}
