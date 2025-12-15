package com.yourname.library.pattern.decorator;

public class RateableBookDecorator extends BookDecorator {
    private double rating = 0;

    public RateableBookDecorator(BookComponent decoratedBook) {
        super(decoratedBook);
    }

    public void setRatingFromDB(String dbRating) {
        try {
            if (dbRating != null && !dbRating.equals("Puan Yok")) {
                String cleaned = dbRating.replace(',', '.').replaceAll("[^0-9.]", "");
                this.rating = Double.parseDouble(cleaned);
            }
        } catch (NumberFormatException e) {
            this.rating = 0;
        }
    }

    @Override
    public String getDescription() {
        return super.getDescription() + String.format(" | Puan: %.1f", rating);
    }
}