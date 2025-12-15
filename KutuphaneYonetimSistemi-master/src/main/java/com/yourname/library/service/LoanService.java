package com.yourname.library.service;

import com.yourname.library.dao.LoanDAO;
import com.yourname.library.model.AbstractUser;
import com.yourname.library.model.Book;
import com.yourname.library.model.Loan;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LoanService {
    private final LoanDAO loanDAO;
    private final BookService bookService;

    public LoanService(LoanDAO loanDAO, BookService bookService) {
        this.loanDAO = loanDAO;
        this.bookService = bookService;
    }

    public void borrowBook(AbstractUser user, Book book) {

        if (!"Available".equalsIgnoreCase(book.getStatus()) && !"Mevcut".equalsIgnoreCase(book.getStatus())) {
            throw new IllegalStateException("Bu kitap şu an ödünç alınamaz!");
        }

        int currentLoans = getActiveLoansByUser(user).size();
        int limit = com.yourname.library.util.SystemConfig.getInstance().getMaxBorrowLimit();

        if (currentLoans >= limit) {
            throw new IllegalStateException("Maksimum ödünç alma sınırına (" + limit + " kitap) ulaştınız! Lütfen önce kitap iade edin.");
        }

        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);

        int days = com.yourname.library.util.SystemConfig.getInstance().getLoanPeriodDays();
        cal.add(Calendar.DAY_OF_YEAR, days);
        Date dueDate = cal.getTime();

        Loan loan = new Loan(0, user, book, now, dueDate);
        loanDAO.addLoan(loan);
        bookService.borrowBook(book.getId());
    }

    public void returnBook(int loanId) {
        Loan loan = loanDAO.getLoanById(loanId);

        if (loan != null) {
            loan.setActualReturnDate(new Date());
            loan.setStatus("Returned");

            bookService.returnBook(loan.getBook().getId());

            loanDAO.updateLoan(loan);
        }
    }

    public List<Loan> getAllLoans() {
        return loanDAO.getAllLoans();
    }

    public List<Loan> getActiveLoansByUser(AbstractUser user) {
        List<Loan> allLoans = loanDAO.getAllLoans();

        return allLoans.stream()
                .filter(l -> l.getUser().getId() == user.getId() && "Active".equals(l.getStatus()))
                .toList();
    }

    public List<Loan> getLoanHistoryByUser(AbstractUser user) {
        List<Loan> allLoans = loanDAO.getAllLoans();
        return allLoans.stream()
                .filter(l -> l.getUser().getId() == user.getId())
                .toList();
    }
}