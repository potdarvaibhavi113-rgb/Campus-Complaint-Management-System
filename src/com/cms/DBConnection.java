
package com.cms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
        "jdbc:mysql://localhost:3306/complaint_db?useSSL=false&serverTimezone=UTC";

    private static final String USER = "root";

    // Read MySQL password from environment variable
    private static final String PASSWORD =
        System.getenv("DB_PASSWORD");

    public static Connection getConnection()
            throws SQLException {

        if (PASSWORD == null || PASSWORD.isEmpty()) {
            throw new SQLException(
                "Database password not configured. " +
                "Please set DB_PASSWORD environment variable."
            );
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                "MySQL JDBC Driver not found. Add MySQL Connector/J.",
                e
            );
        }

        return DriverManager.getConnection(
            URL,
            USER,
            PASSWORD
        );
    }
}
