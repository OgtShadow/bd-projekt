package com.example.dbapp.dao;

import com.example.dbapp.model.Utwor;
import com.example.dbapp.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtworDAO {

    public List<Utwor> findAll() throws SQLException {
        List<Utwor> lista = new ArrayList<>();
        String sql = "SELECT * FROM utwor ORDER BY id_utwor";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Utwor utwor = new Utwor(
                    rs.getInt("id_utwor"),
                    rs.getString("tytul"),
                    rs.getInt("rok_wydania"),
                    rs.getInt("dlugosc_sekundy"),
                    rs.getString("gatunek")
                );
                lista.add(utwor);
            }
        }
        return lista;
    }

    public void save(Utwor utwor) throws SQLException {
        String sql = "INSERT INTO utwor (tytul, rok_wydania, dlugosc_sekundy, gatunek) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, utwor.getTytul());
            pstmt.setInt(2, utwor.getRokWydania());
            pstmt.setInt(3, utwor.getDlugoscSekundy());
            pstmt.setString(4, utwor.getGatunek());

            pstmt.executeUpdate();
        }
    }

    public void update(Utwor utwor) throws SQLException {
        String sql = "UPDATE utwor SET tytul = ?, rok_wydania = ?, dlugosc_sekundy = ?, gatunek = ? WHERE id_utwor = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, utwor.getTytul());
            pstmt.setInt(2, utwor.getRokWydania());
            pstmt.setInt(3, utwor.getDlugoscSekundy());
            pstmt.setString(4, utwor.getGatunek());
            pstmt.setInt(5, utwor.getIdUtwor());

            pstmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sqlLinks = "DELETE FROM autor_utwor WHERE id_utwor = ?";
        String sqlUtwor = "DELETE FROM utwor WHERE id_utwor = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false); 

            try (PreparedStatement pstmtLink = conn.prepareStatement(sqlLinks)) {
                pstmtLink.setInt(1, id);
                pstmtLink.executeUpdate();
            }

            try (PreparedStatement pstmtUtwor = conn.prepareStatement(sqlUtwor)) {
                pstmtUtwor.setInt(1, id);
                pstmtUtwor.executeUpdate();
            }

            conn.commit();
        }
    }

    public List<String> getAutorzyForUtwor(int utworId) throws SQLException {
        List<String> autorzy = new ArrayList<>();
        String sql = "SELECT a.imie, a.nazwisko FROM autor a " +
                     "JOIN autor_utwor au ON a.id_autor = au.id_autor " +
                     "WHERE au.id_utwor = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, utworId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    autorzy.add(rs.getString("imie") + " " + rs.getString("nazwisko"));
                }
            }
        }
        return autorzy;
    }

    public void removeDuplicates() throws SQLException {
        String sql = "DELETE FROM utwor u1 WHERE rowid > " +
                     "(SELECT MIN(rowid) FROM utwor u2 " +
                     "WHERE u1.tytul = u2.tytul " +
                     "AND u1.rok_wydania = u2.rok_wydania " +
                     "AND NVL(u1.dlugosc_sekundy, 0) = NVL(u2.dlugosc_sekundy, 0) " +
                     "AND NVL(u1.gatunek, ' ') = NVL(u2.gatunek, ' '))";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            int count = stmt.executeUpdate(sql);
            System.out.println("Usunięto duplikatów utworów: " + count);
        }
    }
}