package com.ConnInit;

import java.sql.*;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConn 
{
    public static Connection conn;

    static 
    {
        try 
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Room_rover", "root", "admin123");
        } 
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load MySQL JDBC driver");
        } 
        catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to establish a database connection");
        }
    }

    public static Connection getConnection() {
        return conn;
    }
}
