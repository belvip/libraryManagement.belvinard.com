package com.belvinard.libraryManagementSystem.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class Loan {
    // Getters et Setters
    @Setter
    @Getter
    private Book book;
    @Setter
    @Getter
    private User user;
    private Date loanDate;
    //private Date borrowDate;

    // Constructeur
    public Loan(Book book, User user, Date loanDate) {
        this.book = book;
        this.user = user;
        this.loanDate = loanDate;
        // this.borrowDate = borrowDate;
    }

    public Date getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(Date loanDate) {
        this.loanDate = loanDate;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "book=" + book.getTitle() +
                ", user=" + user.getUsername() +
                ", borrowDate=" + loanDate +
                '}';
    }
}
