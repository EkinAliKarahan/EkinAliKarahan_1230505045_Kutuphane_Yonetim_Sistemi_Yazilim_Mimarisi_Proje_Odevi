package com.yourname.library.pattern.decorator;

import com.yourname.library.model.Book;

public class BaseBookComponent implements BookComponent {
    private final Book book;

    public BaseBookComponent(Book book) {
        this.book = book;
    }

    @Override
    public String getDescription() {
        return String.format("Başlık: %s | Yazar: %s | Kategori: %s | ISBN: %s",
                book.getTitle(),
                book.getAuthor(),
                book.getCategory(),
                book.getIsbn() != null ? book.getIsbn() : "Yok");
    }
}