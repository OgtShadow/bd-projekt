package com.example.dbapp.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection { 
    private static final String HOST = "155.158.112.45";
    private static final String PORT = "1521";
    private static final String SID = "oltpstud"; 
    private static final String USER = "ziibd4"; 
    private static final String PASSWORD = "haslo2025"; 

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:oracle:thin:@" + HOST + ":" + PORT + ":" + SID;
        return DriverManager.getConnection(url, USER, PASSWORD);
    }

    public static String checkConnection() {
        try (Connection conn = getConnection()) {
            return "OK";
        } catch (SQLException e) {
            return "Błąd połączenia: " + e.getMessage() + "\nErrorCode: " + e.getErrorCode();
        }
    }
}