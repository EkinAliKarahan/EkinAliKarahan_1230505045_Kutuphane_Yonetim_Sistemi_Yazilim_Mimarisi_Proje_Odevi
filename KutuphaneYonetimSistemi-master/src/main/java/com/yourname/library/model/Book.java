package com.yourname.library.model;

import com.yourname.library.pattern.state.AvailableState;
import com.yourname.library.pattern.state.BookState;
import com.yourname.library.pattern.state.BorrowedState;


public class Book extends AbstractBook {

    private BookState state;
    private final String avgRating;

    public Book(int id, String title, String author, String category, String status, String avgRating) {
        super(id, title, author, category, null, null, 0);
        this.avgRating = avgRating != null ? avgRating : "Puan Yok";
        setStatus(status);
    }

    public Book(String title, String author, String category, String isbn, String publisher, int publishYear) {
        super(0, title, author, category, isbn, publisher, publishYear);
        this.state = new AvailableState();
        this.avgRating = "Puan Yok";
    }

    public BookState getState() {
        return state;
    }

    public void setState(BookState state) {
        this.state = state;
    }

    public String getStatus() {
        if (state instanceof BorrowedState) return "Borrowed";

        return "Available";
    }

    public void setStatus(String status) {
        if (status == null) {
            this.state = new AvailableState();
            return;
        }

        switch (status.toLowerCase()) {
            case "borrowed":
            case "ödünç alındı":
                this.state = new BorrowedState();
                break;
            // Lost (Kayıp) durumları silindi
            default:
                this.state = new AvailableState();
                break;
        }
    }

    public String getAvgRating() {
        return avgRating;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", author='" + getAuthor() + '\'' +
                ", category='" + getCategory() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", rating='" + avgRating + '\'' +
                '}';
    }
}