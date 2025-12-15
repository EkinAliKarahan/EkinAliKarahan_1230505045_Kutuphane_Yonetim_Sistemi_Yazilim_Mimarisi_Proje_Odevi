package com.yourname.library.ui;

import com.yourname.library.dao.BookDAO;
import com.yourname.library.dao.LoanDAO;
import com.yourname.library.dao.UserDAO;
import com.yourname.library.model.AbstractUser;
import com.yourname.library.service.BookService;
import com.yourname.library.service.LoanService;
import com.yourname.library.service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class ManageUsersView {
    private JFrame frame;
    private JTable usersTable;
    private DefaultTableModel tableModel;

    private final UserService userService;
    private final AbstractUser currentUser;

    private JTextField searchField;
    private JComboBox<String> criteriaBox;
    private JButton searchBtn, clearBtn;
    private JButton addUserBtn, detailsBtn, deleteBtn, refreshBtn;

    public ManageUsersView(UserService userService, AbstractUser currentUser) {
        this.userService = userService;
        this.currentUser = currentUser;
        initUserInterface();
    }

    private void initUserInterface() {
        frame = new JFrame("Kullanıcı Yönetimi");
        frame.setSize(1000, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        frame.add(mainPanel);

        mainPanel.add(setupFilterPanel(), BorderLayout.NORTH);
        mainPanel.add(setupUserTable(), BorderLayout.CENTER);
        mainPanel.add(setupActionButtons(), BorderLayout.SOUTH);

        configureEvents();

        refreshTable(userService.getAllUsers());
        frame.setVisible(true);
    }

    private JPanel setupFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Kullanıcı Filtreleme"));

        JLabel lblInfo = new JLabel("Hızlı Ara:");
        lblInfo.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblInfo);

        searchField = new JTextField(15);
        panel.add(searchField);

        String[] criteria = {"Tümü", "Ad", "Soyad", "TC No", "E-posta", "Okul No"};
        criteriaBox = new JComboBox<>(criteria);
        criteriaBox.setBackground(Color.WHITE);
        panel.add(criteriaBox);

        searchBtn = new JButton("Ara");
        searchBtn.setToolTipText("Kriterlere göre filtrele");
        panel.add(searchBtn);

        clearBtn = new JButton("Temizle");
        panel.add(clearBtn);

        return panel;
    }

    private JScrollPane setupUserTable() {
        String[] columns = {"ID", "Ad", "Soyad", "TC No", "E-posta", "Tip", "Okul No", "Telefon"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        usersTable = new JTable(tableModel);
        usersTable.setFillsViewportHeight(true);
        return new JScrollPane(usersTable);
    }

    private JPanel setupActionButtons() {
        JPanel btnPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        btnPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        addUserBtn = generateButton("+ Yeni Kullanıcı Ekle", new Color(144, 238, 144));
        detailsBtn = generateButton("Detaylar & İşlemler", Color.CYAN);
        deleteBtn = generateButton("Kullanıcı Sil", new Color(255, 102, 102));
        refreshBtn = generateButton("Tabloyu Yenile", Color.LIGHT_GRAY);

        btnPanel.add(addUserBtn);
        btnPanel.add(detailsBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(refreshBtn);

        return btnPanel;
    }

    private JButton generateButton(String title, Color color) {
        JButton button = new JButton(title);
        button.setBackground(color);
        button.setFont(new Font("Arial", Font.PLAIN, 12));
        return button;
    }

    private void configureEvents() {
        searchBtn.addActionListener(e -> {
            String keyword = searchField.getText().trim().toLowerCase();
            String selectedCriteria = (String) criteriaBox.getSelectedItem();
            if (!keyword.isEmpty() && selectedCriteria != null) {
                List<AbstractUser> filtered = userService.getAllUsers().stream()
                        .filter(u -> checkMatch(u, selectedCriteria, keyword))
                        .collect(Collectors.toList());
                refreshTable(filtered);
            } else {
                refreshTable(userService.getAllUsers());
            }
        });

        clearBtn.addActionListener(e -> {
            searchField.setText("");
            criteriaBox.setSelectedIndex(0);
            refreshTable(userService.getAllUsers());
        });

        addUserBtn.addActionListener(e -> new RegisterView(userService));

        detailsBtn.addActionListener(e -> {
            int row = usersTable.getSelectedRow();
            if (row != -1) {
                int userId = (int) tableModel.getValueAt(row, 0);
                UserDAO tempUserDAO = new UserDAO();
                AbstractUser selectedUser = tempUserDAO.getUserById(userId);
                if (selectedUser != null) {
                    BookDAO bDAO = new BookDAO();
                    LoanDAO lDAO = new LoanDAO(tempUserDAO, bDAO);
                    new UserDetailView(selectedUser, userService, new LoanService(lDAO, new BookService(bDAO)), new BookService(bDAO), currentUser);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Lütfen bir kullanıcı seçin.");
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = usersTable.getSelectedRow();
            if (row != -1) {
                int id = (int) tableModel.getValueAt(row, 0);
                if (JOptionPane.showConfirmDialog(frame, "Kullanıcıyı silmek istiyor musunuz?", "Onay", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    userService.deleteUser(id);
                    refreshTable(userService.getAllUsers());
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Seçim yapınız.");
            }
        });

        refreshBtn.addActionListener(e -> {
            searchField.setText("");
            criteriaBox.setSelectedIndex(0);
            refreshTable(userService.getAllUsers());
        });
    }

    private boolean checkMatch(AbstractUser u, String criteria, String keyword) {
        String fName = u.getFirstName().toLowerCase();
        String lName = u.getLastName().toLowerCase();
        String tc = u.getTcNo() != null ? u.getTcNo() : "";
        String mail = u.getEmail().toLowerCase();
        String num = u.getNumber() != null ? u.getNumber() : "";

        return switch (criteria) {
            case "Ad" -> fName.contains(keyword);
            case "Soyad" -> lName.contains(keyword);
            case "TC No" -> tc.contains(keyword);
            case "E-posta" -> mail.contains(keyword);
            case "Okul No" -> num.contains(keyword);
            default -> fName.contains(keyword) || lName.contains(keyword) || tc.contains(keyword) || mail.contains(keyword) || num.contains(keyword);
        };
    }

    private void refreshTable(List<AbstractUser> users) {
        tableModel.setRowCount(0);
        for (AbstractUser u : users) {
            tableModel.addRow(new Object[]{
                    u.getId(), u.getFirstName(), u.getLastName(),
                    u.getTcNo(), u.getEmail(), u.getUserType(),
                    u.getNumber(), u.getPhone()
            });
        }
    }
}