package com.yourname.library.pattern.decorator;

public abstract class BookDecorator implements BookComponent {
    protected BookComponent decoratedBook;

    public BookDecorator(BookComponent decoratedBook) {
        this.decoratedBook = decoratedBook;
    }

    @Override
    public String getDescription() {
        return decoratedBook.getDescription();
    }
}