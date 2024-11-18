package com.belvinard.libraryManagementSystem.model;

import java.time.LocalDate;

public class BorrowHistoryNode {
    private String user;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private BorrowHistoryNode next;

    // Constructeur
    public BorrowHistoryNode(String user, LocalDate borrowDate, LocalDate returnDate) {
        this.user = user;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.next = null;
    }

    // Getters et setters
    public String getUser() {
        return user;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public BorrowHistoryNode getNext() {
        return next;
    }

    public void setNext(BorrowHistoryNode next) {
        this.next = next;
    }
}
