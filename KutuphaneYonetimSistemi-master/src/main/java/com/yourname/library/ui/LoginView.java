package com.yourname.library.ui;

import com.yourname.library.dao.BookDAO;
import com.yourname.library.dao.LoanDAO;
import com.yourname.library.dao.UserDAO;
import com.yourname.library.model.AbstractUser;
import com.yourname.library.service.UserService;

import javax.swing.*;

public class LoginView {
    private JFrame frame;

    private final UserService userService;
    private final BookDAO bookDAO;
    private final LoanDAO loanDAO;
    private final UserDAO userDAO;

    public LoginView(UserService userService, BookDAO bookDAO, LoanDAO loanDAO, UserDAO userDAO) {
        this.userService = userService;
        this.bookDAO = bookDAO;
        this.loanDAO = loanDAO;
        this.userDAO = userDAO;
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Kütüphane Sistemi - Giriş");
        frame.setSize(450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        frame.add(panel);

        JLabel inputLabel = new JLabel("Giriş (Mail/TC/Okul No):");
        inputLabel.setBounds(30, 50, 160, 25);
        panel.add(inputLabel);

        JTextField inputField = new JTextField();
        inputField.setBounds(190, 50, 220, 25);
        panel.add(inputField);

        JLabel passwordLabel = new JLabel("Şifre:");
        passwordLabel.setBounds(30, 90, 80, 25);
        panel.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(190, 90, 220, 25);
        panel.add(passwordField);

        JButton loginButton = new JButton("Giriş Yap");
        loginButton.setBounds(60, 150, 130, 30);
        panel.add(loginButton);

        JButton registerButton = new JButton("Kayıt Ol");
        registerButton.setBounds(220, 150, 130, 30);
        panel.add(registerButton);

        loginButton.addActionListener(e -> {
            String credential = inputField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if(credential.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Lütfen tüm alanları doldurun.", "Uyarı", JOptionPane.WARNING_MESSAGE);
                return;
            }

            AbstractUser user = userService.login(credential, password);

            if (user != null) {
                frame.dispose();
                String userType = user.getUserType();

                if ("Student".equalsIgnoreCase(userType) || "Öğrenci".equalsIgnoreCase(userType)) {
                    new StudentView(user, bookDAO, loanDAO);
                }

                else if ("Staff".equalsIgnoreCase(userType) || "Personel".equalsIgnoreCase(userType) || "Admin".equalsIgnoreCase(userType)) {
                    new StaffView(user, bookDAO, userDAO, loanDAO);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Giriş bilgileri veya şifre hatalı!", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });


        registerButton.addActionListener(e -> new RegisterView(userService));

        frame.setVisible(true);
    }
}