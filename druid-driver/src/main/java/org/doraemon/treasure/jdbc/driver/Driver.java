package org.doraemon.treasure.jdbc.driver;

import java.sql.SQLException;

public class Driver extends NoRegisterDriver {

    static {
        try {
            java.sql.DriverManager.registerDriver(new Driver());
        } catch (SQLException E) {
            throw new RuntimeException("Can't register driver!");
        }
    }

    public Driver() throws SQLException {

    }

}
