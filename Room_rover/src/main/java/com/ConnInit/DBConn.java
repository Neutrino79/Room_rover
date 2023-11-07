package com.ConnInit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConn {
    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Room_rover", "root", "admin123");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load MySQL JDBC driver");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to establish a database connection");
        }
        return conn;
    }
}
