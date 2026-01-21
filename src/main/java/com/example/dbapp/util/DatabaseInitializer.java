package com.example.dbapp.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

public class DatabaseInitializer {

    private static boolean isDatabaseInitialized() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();
            try (ResultSet rs = meta.getTables(null, null, "AUTOR", null)) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Błąd sprawdzania stanu bazy: " + e.getMessage());
            return false;
        }
    }

    public static void initialize() {
        if (isDatabaseInitialized()) {
            System.out.println("Baza danych jest już zainicjalizowana. Pomijam skrypt.");
            return;
        }

        System.out.println("Rozpoczynam inicjalizację bazy danych...");

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Wczytaj plik ze skryptem
            InputStream is = DatabaseInitializer.class.getResourceAsStream("/database.sql");
            if (is == null) {
                System.err.println("Nie znaleziono pliku database.sql!");
                return;
            }

            try (Scanner scanner = new Scanner(is)) {
                scanner.useDelimiter("\\A"); 
                String content = scanner.hasNext() ? scanner.next() : "";
                content = content.replace("\r\n", "\n").replace("\r", "\n");

                StringBuilder currentStatement = new StringBuilder();
                Statement stmt = conn.createStatement();

                String[] lines = content.split("\n");

                for (String line : lines) {
                    if (line.trim().isEmpty() || line.trim().startsWith("--")) {
                        continue;
                    }

                    if (line.trim().equals("/")) {
                         executeStatement(stmt, currentStatement.toString());
                         currentStatement.setLength(0); 
                         continue;
                    }

                    currentStatement.append(line).append("\n");
                    
                    boolean insideTrigger = currentStatement.toString().toUpperCase().contains("CREATE OR REPLACE TRIGGER");
                    
                    if (!insideTrigger && line.trim().endsWith(";")) {
                        String sql = currentStatement.toString().trim();
                        if (sql.endsWith(";")) {
                            sql = sql.substring(0, sql.length() - 1);
                        }
                        executeStatement(stmt, sql);
                        currentStatement.setLength(0);
                    }
                }
                System.out.println("Inicjalizacja zakończona!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Błąd podczas inicjalizacji bazy: " + e.getMessage());
        }
    }

    private static void executeStatement(Statement stmt, String sql) {
        if (sql.trim().isEmpty()) return;
        try {
            System.out.println("Wykonuję SQL: " + (sql.length() > 50 ? sql.substring(0, 50) + "..." : sql));
            stmt.execute(sql);
        } catch (SQLException e) {
            if (e.getErrorCode() == 955) {
                System.out.println("Obiekt już istnieje, pomijam.");
            } else {
                System.err.println("Błąd SQL: " + e.getMessage());
            }
        }
    }
}