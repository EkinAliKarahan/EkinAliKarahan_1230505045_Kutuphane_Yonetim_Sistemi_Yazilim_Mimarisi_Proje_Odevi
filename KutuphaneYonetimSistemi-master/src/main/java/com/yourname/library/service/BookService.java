package com.yourname.library.service;

import com.yourname.library.dao.BookDAO;
import com.yourname.library.model.Book;
import com.yourname.library.pattern.observer.InventorySubject;

import java.util.List;

public class BookService extends InventorySubject {
    private final BookDAO bookDAO;

    public BookService(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    public void rateBook(int userId, int bookId, int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Puan 1 ile 5 arasında olmalıdır.");
        }
        bookDAO.addRating(userId, bookId, rating);
        notifyObservers();
    }

    public void borrowBook(int bookId) {
        Book book = bookDAO.getBookById(bookId);
        if (book != null) {
            book.getState().borrow(book);
            if (bookDAO.updateBook(book)) notifyObservers();
        }
    }

    public void returnBook(int bookId) {
        Book book = bookDAO.getBookById(bookId);
        if (book != null) {
            book.getState().returnBook(book);
            if (bookDAO.updateBook(book)) notifyObservers();
        }
    }

    public void addBook(Book book) {
        if (bookDAO.addBook(book)) notifyObservers();
    }

    public void updateBook(Book book) {
        if (bookDAO.updateBook(book)) notifyObservers();
    }

    public void deleteBook(int bookId) {
        if (bookDAO.deleteBook(bookId)) notifyObservers();
    }

    public List<Book> getAllBooks() {
        return bookDAO.getAllBooks();
    }

    public Book getBookById(int id) {
        return bookDAO.getBookById(id);
    }
}