package com.yourname.library.ui;

import com.yourname.library.model.AbstractUser;
import com.yourname.library.model.Book;
import com.yourname.library.pattern.strategy.*;
import com.yourname.library.service.BookService;
import com.yourname.library.service.LoanService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SearchBooksView {
    private JFrame frame;
    private JTable bookTable;
    private JTextField queryField;
    private JComboBox<String> criteriaBox;


    private final BookService bookService;
    private final LoanService loanService;
    private final AbstractUser currentUser;

    public SearchBooksView(BookService bookService, LoanService loanService, AbstractUser currentUser) {
        this.bookService = bookService;
        this.loanService = loanService;
        this.currentUser = currentUser;

        initialize();
    }

    private void initialize() {
        frame = new JFrame("Detaylı Kitap Arama");
        frame.setSize(900, 550);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        frame.add(mainPanel);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        queryField = new JTextField(20);

        String[] searchTypes = {"Başlığa Göre", "Yazara Göre", "ISBN'e Göre", "Kategoriye Göre"};
        criteriaBox = new JComboBox<>(searchTypes);

        JButton searchButton = new JButton("Ara");

        topPanel.add(new JLabel("Arama:"));
        topPanel.add(queryField);
        topPanel.add(criteriaBox);
        topPanel.add(searchButton);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        bookTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(bookTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JButton borrowButton = new JButton("Seçili Kitabı Ödünç Al");
        borrowButton.setBackground(new Color(144, 238, 144));

        bottomPanel.add(borrowButton);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        searchButton.addActionListener(e -> performSearch());

        borrowButton.addActionListener(e -> {
            int selectedRow = bookTable.getSelectedRow();
            if (selectedRow != -1) {
                int bookId = (int) bookTable.getModel().getValueAt(selectedRow, 0);
                Book selectedBook = bookService.getBookById(bookId);

                if (selectedBook != null) {
                    try {
                        loanService.borrowBook(currentUser, selectedBook);
                        JOptionPane.showMessageDialog(frame, "Kitap başarıyla ödünç alındı!");
                        performSearch();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "Hata: " + ex.getMessage(), "Ödünç Alma Hatası", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Lütfen listeden bir kitap seçin.");
            }
        });

        fillTable(bookService.getAllBooks());
        frame.setVisible(true);
    }

    private void performSearch() {
        String query = queryField.getText();
        String criteria = (String) criteriaBox.getSelectedItem();

        if (criteria != null) {
            SearchContext ctx = switch (criteria) {
                case "Başlığa Göre" -> new SearchContext(new TitleSearchStrategy());
                case "Yazara Göre" -> new SearchContext(new AuthorSearchStrategy());
                case "ISBN'e Göre" -> new SearchContext(new ISBNSearchStrategy());
                case "Kategoriye Göre" -> new SearchContext(new CategorySearchStrategy());
                default -> null;
            };

            if (ctx != null) {
                List<Book> allBooks = bookService.getAllBooks();
                List<Book> results = query.isEmpty() ? allBooks : ctx.executeSearch(allBooks, query);
                fillTable(results);
            }
        }
    }

    private void fillTable(List<Book> books) {
        String[] columnNames = {"ID", "Başlık", "Yazar", "ISBN", "Kategori", "Durum", "Puan"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Book b : books) {
            String status = (b.getState() != null) ? b.getStatus() : "Bilinmiyor";
            Object[] row = {
                    b.getId(),
                    b.getTitle(),
                    b.getAuthor(),
                    b.getIsbn(),
                    b.getCategory(),
                    status,
                    b.getAvgRating()
            };
            model.addRow(row);
        }
        bookTable.setModel(model);
    }
}