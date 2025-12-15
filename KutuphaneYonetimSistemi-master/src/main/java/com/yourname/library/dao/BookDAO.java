package com.yourname.library.dao;

import com.yourname.library.model.Book;
import com.yourname.library.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookDAO {

    private static final Logger LOGGER = Logger.getLogger(BookDAO.class.getName());

    public void addRating(int userId, int bookId, int rating) {
        String sql = "INSERT INTO book_ratings (user_id, book_id, rating) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, bookId);
            ps.setInt(3, rating);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Puan eklenirken hata: " + e.getMessage(), e);
        }
    }

    public Book getBookById(int id) {
        String sql = "SELECT id, title, author, category, isbn, publisher, publish_year, status, " +
                "COALESCE((SELECT AVG(rating) FROM book_ratings WHERE book_id = books.id), 0) AS average_rating " +
                "FROM books WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return createBookFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "ID ile kitap getirilirken hata oluştu: " + id, e);
        }
        return null;
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT id, title, author, category, isbn, publisher, publish_year, status, " +
                "COALESCE((SELECT AVG(rating) FROM book_ratings WHERE book_id = books.id), 0) AS average_rating " +
                "FROM books";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                books.add(createBookFromResultSet(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Tüm kitaplar listelenirken hata oluştu.", e);
        }
        return books;
    }

    public boolean addBook(Book book) {
        String sql = "INSERT INTO books (title, author, category, isbn, publisher, publish_year, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            setBookParameters(ps, book);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Kitap eklenirken hata: " + book.getTitle(), e);
        }
        return false;
    }

    public boolean deleteBook(int bookId) {
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Kitap silinirken hata ID: " + bookId, e);
            return false;
        }
    }

    public boolean updateBook(Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, category = ?, isbn = ?, publisher = ?, publish_year = ?, status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            setBookParameters(ps, book);
            ps.setInt(8, book.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Kitap güncellenirken hata ID: " + book.getId(), e);
        }
        return false;
    }

    private void setBookParameters(PreparedStatement ps, Book book) throws SQLException {
        ps.setString(1, book.getTitle());
        ps.setString(2, book.getAuthor());
        ps.setString(3, book.getCategory());
        ps.setString(4, book.getIsbn());
        ps.setString(5, book.getPublisher());
        ps.setInt(6, book.getPublishYear());
        ps.setString(7, book.getStatus());
    }

    private Book createBookFromResultSet(ResultSet rs) throws SQLException {
        String averageRating = rs.getString("average_rating");
        String rating = averageRating != null ? String.format("%.1f", Double.parseDouble(averageRating)) : "Puan Yok";

        String status = rs.getString("status");

        Book book = new Book(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("category"),
                status,
                rating
        );

        book.setIsbn(rs.getString("isbn"));
        book.setPublisher(rs.getString("publisher"));
        book.setPublishYear(rs.getInt("publish_year"));

        return book;
    }
}