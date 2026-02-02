package com.example.dbapp.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;

public class DatabaseConnection { 
    private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

    private static final String HOST = dotenv.get("DB_HOST");
    private static final String PORT = dotenv.get("DB_PORT");
    private static final String SID = dotenv.get("DB_SID"); 
    private static final String USER = dotenv.get("DB_USER"); 
    private static final String PASSWORD = dotenv.get("DB_PASSWORD"); 

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