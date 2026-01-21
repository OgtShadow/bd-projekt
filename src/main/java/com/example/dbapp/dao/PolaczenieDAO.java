package com.example.dbapp.dao;

import com.example.dbapp.model.Polaczenie;
import com.example.dbapp.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PolaczenieDAO {

    public List<Polaczenie> findAll() throws SQLException {
        List<Polaczenie> list = new ArrayList<>();
        String sql = "SELECT a.id_autor, a.imie, a.nazwisko, u.id_utwor, u.tytul " +
                     "FROM autor_utwor au " +
                     "JOIN autor a ON au.id_autor = a.id_autor " +
                     "JOIN utwor u ON au.id_utwor = u.id_utwor";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Polaczenie(
                        rs.getInt("id_autor"),
                        rs.getInt("id_utwor"),
                        rs.getString("imie"),
                        rs.getString("nazwisko"),
                        rs.getString("tytul")
                ));
            }
        }
        return list;
    }

    public void save(int idAutor, int idUtwor) throws SQLException {
        String sql = "INSERT INTO autor_utwor (id_autor, id_utwor) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idAutor);
            pstmt.setInt(2, idUtwor);
            pstmt.executeUpdate();
        }
    }

    public void delete(int idAutor, int idUtwor) throws SQLException {
        String sql = "DELETE FROM autor_utwor WHERE id_autor = ? AND id_utwor = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idAutor);
            pstmt.setInt(2, idUtwor);
            pstmt.executeUpdate();
        }
    }
}
