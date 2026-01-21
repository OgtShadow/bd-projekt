package com.example.dbapp.dao;

import com.example.dbapp.model.Autor;
import com.example.dbapp.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AutorDAO {

    public List<Autor> findAll() throws SQLException {
        List<Autor> lista = new ArrayList<>();
        String sql = "SELECT * FROM autor ORDER BY id_autor";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Autor autor = new Autor(
                    rs.getInt("id_autor"),
                    rs.getString("imie"),
                    rs.getString("nazwisko"),
                    rs.getString("pseudonim"),
                    rs.getString("kraj")
                );
                lista.add(autor);
            }
        }
        return lista;
    }

    public void save(Autor autor) throws SQLException {
        String sql = "INSERT INTO autor (imie, nazwisko, pseudonim, kraj) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, autor.getImie());
            pstmt.setString(2, autor.getNazwisko());
            pstmt.setString(3, autor.getPseudonim());
            pstmt.setString(4, autor.getKraj());

            pstmt.executeUpdate();
        }
    }

    public void update(Autor autor) throws SQLException {
        String sql = "UPDATE autor SET imie = ?, nazwisko = ?, pseudonim = ?, kraj = ? WHERE id_autor = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, autor.getImie());
            pstmt.setString(2, autor.getNazwisko());
            pstmt.setString(3, autor.getPseudonim());
            pstmt.setString(4, autor.getKraj());
            pstmt.setInt(5, autor.getIdAutor());

            pstmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sqlLinks = "DELETE FROM autor_utwor WHERE id_autor = ?";
        String sqlAutor = "DELETE FROM autor WHERE id_autor = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false); 

            try (PreparedStatement pstmtLink = conn.prepareStatement(sqlLinks)) {
                pstmtLink.setInt(1, id);
                pstmtLink.executeUpdate();
            }

            try (PreparedStatement pstmtAutor = conn.prepareStatement(sqlAutor)) {
                pstmtAutor.setInt(1, id);
                pstmtAutor.executeUpdate();
            }

            conn.commit();
        }
    }

    public List<String> getUtworyForAutor(int autorId) throws SQLException {
        List<String> utwory = new ArrayList<>();
        String sql = "SELECT u.tytul FROM utwor u " +
                     "JOIN autor_utwor au ON u.id_utwor = au.id_utwor " +
                     "WHERE au.id_autor = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, autorId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    utwory.add(rs.getString("tytul"));
                }
            }
        }
        return utwory;
    }

    public void removeDuplicates() throws SQLException {
        // Znajdź ID duplikatów
        String duplicatesQuery = "SELECT id_autor FROM autor a1 WHERE rowid > " +
                     "(SELECT MIN(rowid) FROM autor a2 " +
                     "WHERE a1.imie = a2.imie " +
                     "AND a1.nazwisko = a2.nazwisko " +
                     "AND NVL(a1.pseudonim, ' ') = NVL(a2.pseudonim, ' ') " +
                     "AND NVL(a1.kraj, ' ') = NVL(a2.kraj, ' '))";
        
        String deleteLinks = "DELETE FROM autor_utwor WHERE id_autor IN (" + duplicatesQuery + ")";
        String deleteAuthors = "DELETE FROM autor WHERE id_autor IN (" + duplicatesQuery + ")";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (Statement stmt = conn.createStatement()) {
                int linksDeleted = stmt.executeUpdate(deleteLinks);
                int authorsDeleted = stmt.executeUpdate(deleteAuthors);
                conn.commit();
                System.out.println("Usunięto duplikatów: " + authorsDeleted + " (oraz " + linksDeleted + " powiązań)");
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }
}