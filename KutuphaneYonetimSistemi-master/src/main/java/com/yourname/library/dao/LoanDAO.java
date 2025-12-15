package com.yourname.library.dao;

import com.yourname.library.model.Loan;
import com.yourname.library.model.AbstractUser;
import com.yourname.library.model.Book;
import com.yourname.library.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoanDAO {
    private static final Logger LOGGER = Logger.getLogger(LoanDAO.class.getName());

    private final UserDAO userDAO;
    private final BookDAO bookDAO;

    public LoanDAO(UserDAO userDAO, BookDAO bookDAO) {
        this.userDAO = userDAO;
        this.bookDAO = bookDAO;
    }

    public LoanDAO() {
        this.userDAO = new UserDAO();
        this.bookDAO = new BookDAO();
    }

    private static class LoanRawData {
        int id;
        int userId;
        int bookId;
        java.util.Date borrowDate;
        java.util.Date returnDate;
        java.util.Date actualReturnDate;
        String status;

        public LoanRawData(int id, int userId, int bookId, java.util.Date borrowDate, java.util.Date returnDate, java.util.Date actualReturnDate, String status) {
            this.id = id;
            this.userId = userId;
            this.bookId = bookId;
            this.borrowDate = borrowDate;
            this.returnDate = returnDate;
            this.actualReturnDate = actualReturnDate;
            this.status = status;
        }
    }

    public void addLoan(Loan loan) {
        String sql = "INSERT INTO loans (user_id, book_id, borrow_date, return_date, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, loan.getUser().getId());
            ps.setInt(2, loan.getBook().getId());
            ps.setDate(3, new java.sql.Date(loan.getLoanDate().getTime()));
            ps.setDate(4, new java.sql.Date(loan.getDueDate().getTime()));
            ps.setString(5, "Active");

            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ödünç eklenirken hata: " + e.getMessage(), e);
        }
    }

    public List<Loan> getAllLoans() {
        List<Loan> loans = new ArrayList<>();
        List<LoanRawData> rawDataList = new ArrayList<>();

        String sql = "SELECT id, user_id, book_id, borrow_date, return_date, actual_return_date, status FROM loans";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while(rs.next()) {
                rawDataList.add(new LoanRawData(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("book_id"),
                        rs.getDate("borrow_date"),
                        rs.getDate("return_date"),
                        rs.getDate("actual_return_date"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Tüm ödünçler listelenirken hata", e);
        }

        for (LoanRawData data : rawDataList) {
            AbstractUser user = userDAO.getUserById(data.userId);
            Book book = bookDAO.getBookById(data.bookId);

            if (user != null && book != null) {

                java.util.Date actualReturn = (data.actualReturnDate != null) ? new java.util.Date(data.actualReturnDate.getTime()) : null;

                Loan loan = new Loan(
                        data.id,
                        user,
                        book,
                        new java.util.Date(data.borrowDate.getTime()),
                        new java.util.Date(data.returnDate.getTime()),
                        actualReturn,
                        data.status
                );
                loans.add(loan);
            }
        }

        return loans;
    }

    public Loan getLoanById(int id) {
        String sql = "SELECT id, user_id, book_id, borrow_date, return_date, actual_return_date, status FROM loans WHERE id = ?";

        LoanRawData data = null;

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    data = new LoanRawData(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getInt("book_id"),
                            rs.getDate("borrow_date"),
                            rs.getDate("return_date"),
                            rs.getDate("actual_return_date"),
                            rs.getString("status")
                    );
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "ID ile ödünç getirilirken hata: " + id, e);
        }

        if (data != null) {
            AbstractUser user = userDAO.getUserById(data.userId);
            Book book = bookDAO.getBookById(data.bookId);

            if (user != null && book != null) {
                java.util.Date actualReturn = (data.actualReturnDate != null) ? new java.util.Date(data.actualReturnDate.getTime()) : null;
                return new Loan(data.id, user, book, new java.util.Date(data.borrowDate.getTime()), new java.util.Date(data.returnDate.getTime()), actualReturn, data.status);
            }
        }
        return null;
    }

    public void updateLoan(Loan loan) {
        String sql = "UPDATE loans SET user_id=?, book_id=?, borrow_date=?, return_date=?, actual_return_date=?, status=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, loan.getUser().getId());
            ps.setInt(2, loan.getBook().getId());
            ps.setDate(3, new java.sql.Date(loan.getLoanDate().getTime()));
            ps.setDate(4, new java.sql.Date(loan.getDueDate().getTime()));

            if (loan.getActualReturnDate() != null) {
                ps.setDate(5, new java.sql.Date(loan.getActualReturnDate().getTime()));
            } else {
                ps.setNull(5, Types.DATE);
            }

            ps.setString(6, loan.getStatus());
            ps.setInt(7, loan.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ödünç güncellenirken hata ID: " + loan.getId(), e);
        }
    }
}