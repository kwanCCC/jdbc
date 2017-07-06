package org.doraemon.treasure.jdbc.misc;

import java.io.IOException;

public class Constans {

    public static final Integer MAJORVERSION;
    public static final Integer MINORVERSION;

    public static final String MAXORPROTOCOL;
    public static final String MINORPROTOCOL;

    public static final String CALPROTOCOL;



    static {
        PropertiesProxy properties = new PropertiesProxy();
        try {
            properties.load(Constans.class.getResourceAsStream("app.properties"));
        } catch (IOException e) {
            properties = new PropertiesProxy();
        }
        MAJORVERSION = properties.getInt("java.sql.driver.majorVersion", 1);
        MINORVERSION = properties.getInt("java.sql.driver.minorVersion", 1);

        MAXORPROTOCOL = properties.getProperty("java.sql.driver.maxorprotocol", "jdbc");
        MINORPROTOCOL = properties.getProperty("java.sql.driver.minorprotocol", "ONEAPM");

        CALPROTOCOL = properties.getProperty("java.sql.driver.calprotocol", "http");


    }


}
