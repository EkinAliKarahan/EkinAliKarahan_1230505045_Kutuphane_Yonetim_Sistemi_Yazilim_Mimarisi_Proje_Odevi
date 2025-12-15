package com.yourname.library.ui;

import com.yourname.library.dao.BookDAO;
import com.yourname.library.dao.LoanDAO;
import com.yourname.library.dao.UserDAO;
import com.yourname.library.model.AbstractUser;
import com.yourname.library.pattern.observer.InventoryUIObserver;
import com.yourname.library.service.BookService;
import com.yourname.library.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class StaffView {
    private JFrame frame;

    private final AbstractUser user;
    private final BookService bookService;
    private final UserService userService;
    private final LoanDAO loanDAO;
    private final BookDAO bookDAO;
    private final InventoryUIObserver inventoryObserver;


    public StaffView(AbstractUser user, BookDAO bookDAO, UserDAO userDAO, LoanDAO loanDAO) {
        this.user = user;
        this.bookDAO = bookDAO;
        this.loanDAO = loanDAO;

        this.bookService = new BookService(bookDAO);
        this.userService = new UserService(userDAO);

        this.inventoryObserver = new InventoryUIObserver();
        this.bookService.registerObserver(this.inventoryObserver);

        initialize();
    }

    private void initialize() {
        String role = user.getUserType().equalsIgnoreCase("Admin") ? "Yönetici (Admin)" : "Personel";
        frame = new JFrame(role + " Paneli - Hoşgeldin " + user.getFirstName());

        frame.setSize(400, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                bookService.removeObserver(inventoryObserver);
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(null);
        frame.add(panel);

        int x = 100; int width = 200; int height = 40;
        int y = 40;
        int gap = 60;

        JButton myAccountBtn = new JButton("Kütüphane Hesabım");
        myAccountBtn.setBounds(x, y, width, height);
        myAccountBtn.setBackground(new Color(255, 228, 181));
        panel.add(myAccountBtn);
        y += gap;

        JButton manageBooksBtn = new JButton("Kitap Yönetimi");
        manageBooksBtn.setBounds(x, y, width, height);
        manageBooksBtn.setBackground(new Color(176, 224, 230));
        panel.add(manageBooksBtn);
        y += gap;

        JButton manageUsersBtn = new JButton("Kullanıcı Yönetimi");
        manageUsersBtn.setBounds(x, y, width, height);
        manageUsersBtn.setBackground(new Color(144, 238, 144));
        panel.add(manageUsersBtn);
        y += gap;

        JButton settingsBtn = new JButton("Sistem Ayarları");
        settingsBtn.setBounds(x, y, width, height);
        settingsBtn.setBackground(Color.LIGHT_GRAY);
        panel.add(settingsBtn);
        y += gap;

        JButton logoutBtn = new JButton("Çıkış Yap");
        logoutBtn.setBounds(x, y + 10, width, height);
        logoutBtn.setBackground(Color.PINK);
        panel.add(logoutBtn);


        myAccountBtn.addActionListener(e -> new StudentView(user, bookDAO, loanDAO));

        manageBooksBtn.addActionListener(e -> new ManageBooksView(bookService));

        manageUsersBtn.addActionListener(e -> new ManageUsersView(userService, this.user));

        settingsBtn.addActionListener(e -> new ManageConfigView());

        logoutBtn.addActionListener(e -> {
            if (inventoryObserver != null) {
                bookService.removeObserver(inventoryObserver);
            }
            frame.dispose();
        });

        frame.setVisible(true);
    }
}