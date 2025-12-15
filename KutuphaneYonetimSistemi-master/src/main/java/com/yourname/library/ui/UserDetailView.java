package com.yourname.library.ui;

import com.yourname.library.model.AbstractUser;
import com.yourname.library.model.Book;
import com.yourname.library.model.Loan;
import com.yourname.library.service.BookService;
import com.yourname.library.service.LoanService;
import com.yourname.library.service.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UserDetailView {
    private JFrame frame;

    private final AbstractUser targetUser;
    private final UserService userService;
    private final LoanService loanService;
    private final BookService bookService;
    private final AbstractUser currentUser;

    private DefaultTableModel activeModel, historyModel;

    private JTextField nameField, surnameField, tcNoField, emailField, phoneField, numberField;
    private JPasswordField passwordField;

    private JComboBox<String> roleComboBox;

    private JTable activeLoansTable;

    public UserDetailView(AbstractUser targetUser, UserService userService, LoanService loanService, BookService bookService, AbstractUser currentUser) {
        this.targetUser = targetUser;
        this.userService = userService;
        this.loanService = loanService;
        this.bookService = bookService;
        this.currentUser = currentUser;
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Üye İşlemleri: " + targetUser.getFirstName() + " " + targetUser.getLastName());
        frame.setSize(900, 700);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Profil ve Yetki", createProfilePanel());
        tabbedPane.addTab("Mevcut Ödünçler & İade", createActiveLoansPanel());
        tabbedPane.addTab("Ödünç Geçmişi", createHistoryPanel());

        frame.add(tabbedPane);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton borrowForUserBtn = new JButton("+ Bu Üye Adına Kitap Ver");
        borrowForUserBtn.setBackground(new Color(144, 238, 144));

        borrowForUserBtn.addActionListener(e -> showBorrowDialog());
        topPanel.add(borrowForUserBtn);
        frame.add(topPanel, BorderLayout.NORTH);

        frame.setVisible(true);
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(null);
        int y = 30; int gap = 40;

        addLabelAndField(panel, "Ad:", nameField = new JTextField(targetUser.getFirstName()), y); y+=gap;
        addLabelAndField(panel, "Soyad:", surnameField = new JTextField(targetUser.getLastName()), y); y+=gap;
        addLabelAndField(panel, "T.C. No:", tcNoField = new JTextField(targetUser.getTcNo()), y); y+=gap;
        addLabelAndField(panel, "Okul No:", numberField = new JTextField(targetUser.getNumber()), y); y+=gap;
        addLabelAndField(panel, "E-posta:", emailField = new JTextField(targetUser.getEmail()), y); y+=gap;
        addLabelAndField(panel, "Telefon:", phoneField = new JTextField(targetUser.getPhone()), y); y+=gap;

        JLabel lblPass = new JLabel("Şifre:");
        lblPass.setBounds(50, y, 100, 25);
        panel.add(lblPass);
        passwordField = new JPasswordField(targetUser.getPassword());
        passwordField.setBounds(160, y, 200, 25);
        panel.add(passwordField);
        y += gap;

        JLabel lblRole = new JLabel("Kullanıcı Rolü:");
        lblRole.setBounds(50, y, 100, 25);
        panel.add(lblRole);

        String[] roles = {"Student", "Staff", "Admin"};
        roleComboBox = new JComboBox<>(roles);
        roleComboBox.setBounds(160, y, 200, 25);

        if (targetUser.getUserType().equalsIgnoreCase("Admin")) roleComboBox.setSelectedItem("Admin");
        else if (targetUser.getUserType().equalsIgnoreCase("Staff")) roleComboBox.setSelectedItem("Staff");
        else roleComboBox.setSelectedItem("Student");

        if (!"Admin".equalsIgnoreCase(currentUser.getUserType())) {
            roleComboBox.setEnabled(false);
            roleComboBox.setToolTipText("Sadece Admin yetkisi ile değiştirilebilir.");
        }

        panel.add(roleComboBox);
        y += 50;

        JButton updateBtn = new JButton("Bilgileri ve Rolü Güncelle");
        updateBtn.setBounds(160, y, 200, 35);
        panel.add(updateBtn);

        updateBtn.addActionListener(e -> {
            targetUser.setFirstName(nameField.getText());
            targetUser.setLastName(surnameField.getText());
            targetUser.setTcNo(tcNoField.getText());
            targetUser.setNumber(numberField.getText());
            targetUser.setEmail(emailField.getText());
            targetUser.setPhone(phoneField.getText());
            targetUser.setPassword(new String(passwordField.getPassword()));

            boolean profileUpdated = userService.updateUser(targetUser);

            String selectedRole = (String) roleComboBox.getSelectedItem();
            boolean roleUpdated = true;

            if (selectedRole != null && roleComboBox.isEnabled() && !targetUser.getUserType().equalsIgnoreCase(selectedRole)) {
                roleUpdated = userService.changeUserRole(targetUser.getId(), selectedRole);
            }

            if (profileUpdated && roleUpdated) {
                JOptionPane.showMessageDialog(frame, "Güncelleme Başarılı!\nRol değişikliği bir sonraki girişte aktif olur.");
                frame.setTitle("Üye İşlemleri: " + targetUser.getFirstName() + " " + targetUser.getLastName());
            } else {
                JOptionPane.showMessageDialog(frame, "Güncelleme sırasında hata oluştu (Çakışan veri olabilir)!", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private JPanel createActiveLoansPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] cols = {"ID", "Kitap", "Alım Tarihi", "Son Teslim", "Durum", "Gecikme Cezası"};
        activeModel = new DefaultTableModel(cols, 0);

        activeLoansTable = new JTable(activeModel);

        panel.add(new JScrollPane(activeLoansTable), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton returnBtn = new JButton("Seçili Kitabı İade Al");
        JButton refreshBtn = new JButton("Yenile");
        btnPanel.add(returnBtn);
        btnPanel.add(refreshBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        refreshActiveLoans();

        returnBtn.addActionListener(e -> {
            int row = activeLoansTable.getSelectedRow();
            if (row != -1) {
                int loanId = (int) activeModel.getValueAt(row, 0);
                processReturn(loanId);
            } else { JOptionPane.showMessageDialog(frame, "Seçim yapınız."); }
        });

        refreshBtn.addActionListener(e -> refreshActiveLoans());
        return panel;
    }

    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] cols = {"Kitap", "Alım Tarihi", "İade Tarihi", "Durum"};
        historyModel = new DefaultTableModel(cols, 0);

        JTable historyTable = new JTable(historyModel);

        panel.add(new JScrollPane(historyTable), BorderLayout.CENTER);
        refreshHistory();
        return panel;
    }

    private void addLabelAndField(JPanel p, String text, JTextField field, int y) {
        JLabel l = new JLabel(text); l.setBounds(50, y, 100, 25);
        field.setBounds(160, y, 200, 25);
        p.add(l); p.add(field);
    }

    private void refreshActiveLoans() {
        activeModel.setRowCount(0);
        List<Loan> loans = loanService.getActiveLoansByUser(targetUser);
        for (Loan l : loans) {
            double fine = l.calculateFine();
            String fineTxt = (fine > 0) ? String.format("%.2f TL", fine) : "Yok";
            activeModel.addRow(new Object[]{l.getId(), l.getBook().getTitle(), l.getLoanDate(), l.getDueDate(), l.getStatus(), fineTxt});
        }
    }

    private void refreshHistory() {
        historyModel.setRowCount(0);
        List<Loan> loans = loanService.getLoanHistoryByUser(targetUser);
        for (Loan l : loans) {
            historyModel.addRow(new Object[]{l.getBook().getTitle(), l.getLoanDate(), l.getActualReturnDate(), l.getStatus()});
        }
    }

    private void processReturn(int loanId) {
        Loan loan = loanService.getAllLoans().stream().filter(l -> l.getId() == loanId).findFirst().orElse(null);
        if (loan != null) {
            double fine = loan.calculateFine();
            if (fine > 0) {
                int choice = JOptionPane.showConfirmDialog(frame, "Gecikme Cezası: " + String.format("%.2f", fine) + " TL. Tahsil edildi mi?", "Onay", JOptionPane.YES_NO_OPTION);
                if (choice != JOptionPane.YES_OPTION) return;
            }
            loanService.returnBook(loanId);
            JOptionPane.showMessageDialog(frame, "İade alındı.");
            refreshActiveLoans();
            refreshHistory();
        }
    }

    private void showBorrowDialog() {
        String query = JOptionPane.showInputDialog(frame, "Kitap Ara (Ad/Yazar/ISBN):");
        if (query != null && !query.trim().isEmpty()) {
            List<Book> results = bookService.getAllBooks().stream()
                    .filter(b -> b.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                            b.getAuthor().toLowerCase().contains(query.toLowerCase()) ||
                            (b.getIsbn() != null && b.getIsbn().contains(query)))
                    .toList();

            if (results.isEmpty()) { JOptionPane.showMessageDialog(frame, "Bulunamadı."); return; }

            Object[] choices = results.stream().map(b -> b.getId() + ": " + b.getTitle()).toArray();
            String selection = (String) JOptionPane.showInputDialog(frame, "Seçiniz:", "Kitap", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);

            if (selection != null) {
                int bookId = Integer.parseInt(selection.split(":")[0]);
                try {
                    loanService.borrowBook(targetUser, bookService.getBookById(bookId));
                    JOptionPane.showMessageDialog(frame, "Ödünç verildi.");
                    refreshActiveLoans();
                } catch (Exception ex) { JOptionPane.showMessageDialog(frame, "Hata: " + ex.getMessage()); }
            }
        }
    }
}