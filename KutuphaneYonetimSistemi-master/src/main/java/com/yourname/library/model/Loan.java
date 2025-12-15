package com.yourname.library.model;

import java.util.Date;

public class Loan {
    private final int id;
    private final AbstractUser user;
    private final Book book;
    private final Date loanDate;
    private final Date dueDate;
    private Date actualReturnDate;
    private String status;

    public Loan(int id, AbstractUser user, Book book, Date loanDate, Date dueDate, Date actualReturnDate, String status) {
        this.id = id;
        this.user = user;
        this.book = book;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.actualReturnDate = actualReturnDate;
        this.status = status;
    }

    public Loan(int id, AbstractUser user, Book book, Date loanDate, Date dueDate) {
        this(id, user, book, loanDate, dueDate, null, "Active");
    }

    public double calculateFine() {
        Date effectiveDate = (actualReturnDate != null) ? actualReturnDate : new Date();

        if (effectiveDate.after(dueDate)) {
            long diffInMillies = Math.abs(effectiveDate.getTime() - dueDate.getTime());
            long diffInDays = java.util.concurrent.TimeUnit.DAYS.convert(diffInMillies, java.util.concurrent.TimeUnit.MILLISECONDS);

            double dailyFine = com.yourname.library.util.SystemConfig.getInstance().getDailyFine();
            return diffInDays * dailyFine;
        }
        return 0.0;
    }

    public int getId() {
        return id;
    }

    public AbstractUser getUser() {
        return user;
    }

    public Book getBook() {
        return book;
    }

    public Date getLoanDate() {
        return loanDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public Date getActualReturnDate() {
        return actualReturnDate;
    }

    public void setActualReturnDate(Date actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}