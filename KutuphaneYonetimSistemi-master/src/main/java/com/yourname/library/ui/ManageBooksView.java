package com.yourname.library.ui;

import com.yourname.library.model.Book;
import com.yourname.library.pattern.observer.IInventoryObserver;
import com.yourname.library.service.BookService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class ManageBooksView implements IInventoryObserver {
    private JFrame frame;
    private JTable booksTable;
    private DefaultTableModel tableModel;
    private final BookService bookService;


    private JTextField searchField;
    private JComboBox<String> criteriaBox;
    private JButton searchBtn, clearBtn;
    private JButton addButton, updateButton, deleteButton, refreshButton;

    public ManageBooksView(BookService bookService) {
        this.bookService = bookService;
        this.bookService.registerObserver(this);
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Kitap Yönetimi (Personel)");
        frame.setSize(1000, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        frame.add(mainPanel);


        mainPanel.add(createTopPanel(), BorderLayout.NORTH);
        mainPanel.add(createCenterPanel(), BorderLayout.CENTER);
        mainPanel.add(createBottomPanel(), BorderLayout.SOUTH);

        setupListeners();

        refreshTable(bookService.getAllBooks());
        frame.setVisible(true);
    }

    private JPanel createTopPanel() {

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Arama:"));

        searchField = new JTextField(20);
        searchPanel.add(searchField);

        String[] criteria = {"Tümü", "Başlık", "Yazar", "ISBN", "Kategori"};
        criteriaBox = new JComboBox<>(criteria);
        searchPanel.add(criteriaBox);

        searchBtn = new JButton("Ara");
        clearBtn = new JButton("Temizle");
        searchPanel.add(searchBtn);
        searchPanel.add(clearBtn);

        return searchPanel;
    }

    private JScrollPane createCenterPanel() {
        String[] columns = {"ID", "Başlık", "Yazar", "Kategori", "ISBN", "Yayınevi", "Yıl", "Durum", "Puan"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        booksTable = new JTable(tableModel);
        return new JScrollPane(booksTable);
    }

    private JPanel createBottomPanel() {
        JPanel buttonPanel = new JPanel();

        addButton = createStyledButton("+ Yeni Kitap Ekle", new Color(144, 238, 144));
        updateButton = createStyledButton("Kitap Güncelle", Color.CYAN);
        deleteButton = createStyledButton("Kitap Sil", new Color(255, 102, 102));
        refreshButton = createStyledButton("Kitapları Yenile", Color.LIGHT_GRAY);

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        return buttonPanel;
    }

    private void setupListeners() {
        searchBtn.addActionListener(e -> {
            String keyword = searchField.getText().trim().toLowerCase();
            String selectedCriteria = (String) criteriaBox.getSelectedItem();
            if (!keyword.isEmpty() && selectedCriteria != null) {
                List<Book> allBooks = bookService.getAllBooks();
                refreshTable(allBooks.stream()
                        .filter(b -> isMatched(b, selectedCriteria, keyword))
                        .collect(Collectors.toList()));
            } else {
                refreshTable(bookService.getAllBooks());
            }
        });

        clearBtn.addActionListener(e -> {
            searchField.setText("");
            criteriaBox.setSelectedIndex(0);
            refreshTable(bookService.getAllBooks());
        });

        addButton.addActionListener(e -> showAddBookDialog());
        updateButton.addActionListener(e -> showUpdateBookDialog());
        refreshButton.addActionListener(e -> refreshTable(bookService.getAllBooks()));

        deleteButton.addActionListener(e -> {
            int row = booksTable.getSelectedRow();
            if (row != -1) {
                int id = (int) tableModel.getValueAt(row, 0);
                String title = (String) tableModel.getValueAt(row, 1);
                if (JOptionPane.showConfirmDialog(frame, "'" + title + "' silinsin mi?", "Onay", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    bookService.deleteBook(id);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Seçim yapınız.");
            }
        });
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        return btn;
    }

    private boolean isMatched(Book b, String criteria, String keyword) {
        String title = b.getTitle() != null ? b.getTitle().toLowerCase() : "";
        String author = b.getAuthor() != null ? b.getAuthor().toLowerCase() : "";
        String category = b.getCategory() != null ? b.getCategory().toLowerCase() : "";
        String isbn = b.getIsbn() != null ? b.getIsbn() : "";

        return switch (criteria) {
            case "Başlık" -> title.contains(keyword);
            case "Yazar" -> author.contains(keyword);
            case "ISBN" -> isbn.contains(keyword);
            case "Kategori" -> category.contains(keyword);
            default -> title.contains(keyword) || author.contains(keyword) || isbn.contains(keyword) || category.contains(keyword);
        };
    }

    @Override
    public void update() {
        refreshTable(bookService.getAllBooks());
    }

    private void refreshTable(List<Book> books) {
        tableModel.setRowCount(0);
        for (Book b : books) {
            tableModel.addRow(new Object[]{
                    b.getId(), b.getTitle(), b.getAuthor(), b.getCategory(),
                    b.getIsbn(), b.getPublisher(), b.getPublishYear(), b.getStatus(), b.getAvgRating()
            });
        }
    }

    private void showAddBookDialog() {
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField isbnField = new JTextField();
        JTextField publisherField = new JTextField();
        JTextField yearField = new JTextField();
        Object[] message = { "Başlık:", titleField, "Yazar:", authorField, "Kategori:", categoryField, "ISBN:", isbnField, "Yayınevi:", publisherField, "Yıl:", yearField };

        if (JOptionPane.showConfirmDialog(frame, message, "Yeni Kitap", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                int year = Integer.parseInt(yearField.getText());
                bookService.addBook(new Book(titleField.getText(), authorField.getText(), categoryField.getText(), isbnField.getText(), publisherField.getText(), year));
            } catch (Exception ex) { JOptionPane.showMessageDialog(frame, "Hata: " + ex.getMessage()); }
        }
    }

    private void showUpdateBookDialog() {
        int row = booksTable.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(frame, "Seçim yapın."); return; }
        int id = (int) tableModel.getValueAt(row, 0);
        Book book = bookService.getBookById(id);
        if(book != null) {
            JTextField titleField = new JTextField(book.getTitle());
            JTextField authorField = new JTextField(book.getAuthor());
            JTextField categoryField = new JTextField(book.getCategory());
            JTextField isbnField = new JTextField(book.getIsbn());
            JTextField publisherField = new JTextField(book.getPublisher());
            JTextField yearField = new JTextField(String.valueOf(book.getPublishYear()));
            Object[] message = { "Başlık:", titleField, "Yazar:", authorField, "Kategori:", categoryField, "ISBN:", isbnField, "Yayınevi:", publisherField, "Yıl:", yearField };

            if (JOptionPane.showConfirmDialog(frame, message, "Güncelle", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                book.setTitle(titleField.getText());
                book.setAuthor(authorField.getText());
                book.setCategory(categoryField.getText());
                book.setIsbn(isbnField.getText());
                book.setPublisher(publisherField.getText());
                book.setPublishYear(Integer.parseInt(yearField.getText()));
                bookService.updateBook(book);
            }
        }
    }
}