package com.yourname.library.pattern.strategy;

import com.yourname.library.model.Book;
import java.util.List;

public class SearchContext {
    private final SearchStrategy strategy;

    public SearchContext(SearchStrategy strategy) {
        this.strategy = strategy;
    }

    public List<Book> executeSearch(List<Book> books, String query) {
        if (strategy == null) {
            return books;
        }
        return strategy.search(books, query);
    }
}