package com.example.dbapp.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // 1. ADRES BAZY (Host) 
    private static final String HOST = "155.158.112.45";
    
    // 2. PORT - 1521 to standard dla Oracle
    private static final String PORT = "1521";
    
    // 3. NAZWA BAZY (SID)
    private static final String SID = "oltpstud"; 
    
    // 4. LOGIN I HASŁO
    private static final String USER = "ziibd4"; 
    private static final String PASSWORD = "haslo2025"; 

    public static Connection getConnection() throws SQLException {
        // Składanie adresu w całość
        // DLA SID: jdbc:oracle:thin:@host:port:SID
        // DLA Service Name: jdbc:oracle:thin:@//host:port/ServiceName
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