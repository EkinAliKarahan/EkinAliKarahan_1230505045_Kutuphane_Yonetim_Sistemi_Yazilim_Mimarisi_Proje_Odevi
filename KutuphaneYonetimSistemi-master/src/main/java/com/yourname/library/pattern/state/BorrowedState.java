package com.yourname.library.pattern.state;

import com.yourname.library.model.Book;

public class BorrowedState implements BookState {
    @Override
    public void borrow(Book book) {
        System.out.println("HATA: Bu kitap şu an başkasında (Ödünçte). Tekrar ödünç verilemez.");
    }

    @Override
    public void returnBook(Book book) {
        System.out.println("Kitap başarıyla iade edildi. Durum 'Available' (Mevcut) olarak güncelleniyor.");
        book.setState(new AvailableState());
    }
}