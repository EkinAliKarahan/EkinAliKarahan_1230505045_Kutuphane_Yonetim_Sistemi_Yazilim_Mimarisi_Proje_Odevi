package com.yourname.library.pattern.state;

import com.yourname.library.model.Book;

public class AvailableState implements BookState {
    @Override
    public void borrow(Book book) {
        System.out.println("Kitap ödünç verildi. Durum 'Borrowed' (Ödünç Alındı) olarak güncelleniyor.");

        book.setState(new BorrowedState());
    }

    @Override
    public void returnBook(Book book) {
        System.out.println("HATA: Bu kitap zaten kütüphanede (Mevcut). İade alınamaz.");
    }
}