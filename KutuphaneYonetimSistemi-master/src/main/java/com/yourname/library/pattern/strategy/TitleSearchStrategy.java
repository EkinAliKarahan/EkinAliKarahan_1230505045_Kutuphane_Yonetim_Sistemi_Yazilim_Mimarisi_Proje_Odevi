package com.yourname.library.pattern.strategy;

import com.yourname.library.model.Book;
import java.util.List;
import java.util.stream.Collectors;

public class TitleSearchStrategy implements SearchStrategy {
    @Override
    public List<Book> search(List<Book> books, String query) {
        if (query == null || query.isEmpty()) return books;
        String lowerQuery = query.toLowerCase();

        return books.stream()
                .filter(b -> b.getTitle() != null && b.getTitle().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }
}