package org.example;

import com.yourname.library.dao.BookDAO;
import com.yourname.library.dao.LoanDAO;
import com.yourname.library.dao.UserDAO;
import com.yourname.library.service.UserService;
import com.yourname.library.ui.LoginView;
import com.yourname.library.util.DatabaseConnection;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            try {
                if (DatabaseConnection.getInstance().getConnection() == null) {
                    JOptionPane.showMessageDialog(null,
                            "Veritabanına bağlanılamadı! Lütfen XAMPP/MySQL'in çalıştığından emin olun.",
                            "Bağlantı Hatası",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                UserDAO userDAO = new UserDAO();
                BookDAO bookDAO = new BookDAO();
                LoanDAO loanDAO = new LoanDAO(userDAO, bookDAO);
                UserService userService = new UserService(userDAO);

                new LoginView(userService, bookDAO, loanDAO, userDAO);

            } catch (Exception e) {

                System.err.println("Uygulama başlatılırken kritik hata oluştu: " + e.getMessage());
                JOptionPane.showMessageDialog(null, "Beklenmeyen bir hata oluştu: " + e.getMessage());
            }
        });
    }
}