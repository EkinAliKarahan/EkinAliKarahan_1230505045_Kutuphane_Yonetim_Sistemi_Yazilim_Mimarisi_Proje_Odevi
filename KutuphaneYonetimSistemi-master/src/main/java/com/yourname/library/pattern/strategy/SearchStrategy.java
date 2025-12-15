package com.yourname.library.pattern.strategy;

import com.yourname.library.model.Book;
import java.util.List;

public interface SearchStrategy {
    List<Book> search(List<Book> books, String query);
}