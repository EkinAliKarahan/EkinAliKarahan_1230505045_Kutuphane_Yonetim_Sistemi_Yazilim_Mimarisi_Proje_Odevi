package com.yourname.library.ui;

import com.yourname.library.model.AbstractUser;
import com.yourname.library.pattern.factory.UserFactory;
import com.yourname.library.service.UserService;

import javax.swing.*;

public class RegisterView {
    private JFrame frame;
    private JTextField firstNameField, lastNameField, tcNoField, emailField, numberField, phoneField;
    private JPasswordField passwordField, confirmPasswordField;

    private final UserService userService;

    public RegisterView(UserService userService) {
        this.userService = userService;
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Kayıt Ol");
        frame.setSize(450, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        frame.add(panel);

        int y = 30;
        int gap = 40;

        addLabelAndField(panel, "Ad:", firstNameField = new JTextField(), y); y+=gap;
        addLabelAndField(panel, "Soyad:", lastNameField = new JTextField(), y); y+=gap;
        addLabelAndField(panel, "T.C. No:", tcNoField = new JTextField(), y); y+=gap;
        addLabelAndField(panel, "E-posta:", emailField = new JTextField(), y); y+=gap;
        addLabelAndField(panel, "Telefon:", phoneField = new JTextField(), y); y+=gap;
        addLabelAndField(panel, "Okul No:", numberField = new JTextField(), y); y+=gap;

        JLabel passLabel = new JLabel("Şifre:");
        passLabel.setBounds(50, y, 100, 25);
        panel.add(passLabel);
        passwordField = new JPasswordField();
        passwordField.setBounds(160, y, 200, 25);
        panel.add(passwordField);
        y+=gap;

        JLabel confirmPassLabel = new JLabel("Şifre Tekrar:");
        confirmPassLabel.setBounds(50, y, 100, 25);
        panel.add(confirmPassLabel);
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(160, y, 200, 25);
        panel.add(confirmPasswordField);
        y+=50;

        JButton registerButton = new JButton("Kayıt Ol");
        registerButton.setBounds(160, y, 120, 30);
        panel.add(registerButton);

        registerButton.addActionListener(e -> {
            String fName = firstNameField.getText().trim();
            String lName = lastNameField.getText().trim();
            String tcNo = tcNoField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String number = numberField.getText().trim();
            String pass = new String(passwordField.getPassword());
            String confirmPass = new String(confirmPasswordField.getPassword());

            if(fName.isEmpty() || lName.isEmpty() || tcNo.isEmpty() || email.isEmpty() ||
                    phone.isEmpty() || number.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Lütfen tüm alanları doldurun.", "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if(!pass.equals(confirmPass)) {
                JOptionPane.showMessageDialog(frame, "Şifreler eşleşmiyor.", "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                AbstractUser newUser = UserFactory.createUser("Student", 0, fName, lName, tcNo, email, pass, number, phone);

                if(userService.registerUser(newUser)) {
                    JOptionPane.showMessageDialog(frame, "Kayıt Başarılı! Giriş yapabilirsiniz.");
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(frame, "Kayıt sırasında bilinmeyen bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
                }

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Kayıt Hatası", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Sistem Hatası: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setVisible(true);
    }

    private void addLabelAndField(JPanel panel, String labelText, JTextField field, int y) {
        JLabel label = new JLabel(labelText);
        label.setBounds(50, y, 100, 25);
        panel.add(label);
        field.setBounds(160, y, 200, 25);
        panel.add(field);
    }
}