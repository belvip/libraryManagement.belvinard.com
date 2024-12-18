package com.belvinard.libraryManagementSystem.model;

import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Loan {
    // Attributs
    @Setter
    @Getter
    private Book book;
    @Setter
    @Getter
    private User user;
    @Getter
    @Setter
    private Date loanDate;
    // Getter et setter pour la date de retour
    @Setter
    @Getter
    private Date returnDate;  // Nouvel attribut pour la date de retour
    @Getter
    private String returnStatus;  // Nouvel attribut pour le statut de retour
    private List<Loan> loans;  // Liste des prêts de l'utilisateur



    // Constructeur
    public Loan(Book book, User user, Date loanDate, Date returnDate) {
        this.book = book;
        this.user = user;
        this.loanDate = loanDate;
        this.returnDate = returnDate; // Initialisation de la date de retour
        this.returnStatus = getReturnStatus(); // Initialiser returnStatus
    }

    // Méthode pour obtenir un prêt par livre
    public Loan getLoanByBook(Book book) {
        for (Loan loan : loans) {
            if (loan.getBook().equals(book)) {
                return loan;  // Retourner le prêt correspondant au livre
            }
        }
        return null;  // Si le livre n'a pas été emprunté, retourner null
    }

    // Méthode pour ajouter un prêt à l'historique de l'utilisateur
    public void addLoan(Loan loan) {
        if (loans == null) {
            loans = new ArrayList<>();
        }
        loans.add(loan);
    }

    // Méthode pour supprimer un prêt de l'historique de l'utilisateur
    public void removeLoan(Loan loan) {
        if (loans != null) {
            loans.remove(loan);
        }
    }


    // Méthode toString mise à jour pour inclure la date de retour

    @Override
    public String toString() {
        return String.format("Loan {book ----> '%s', user ----> '%s', loanDate ----> '%s', returnDate ----> '%s', status ----> '%s'}",
                book.getTitle(),
                user.getUsername(),
                formatDate(loanDate),
                formatDate(returnDate),
                returnStatus);  // Utilisation de returnStatus
    }

    private String formatDate(Date date) {
        if (date == null) {
            return "N/A";  // Si la date est nulle, afficher "N/A"
        }
        // Formater la date en un format lisible (ex: "Tue Nov 19 09:01:55 WAT 2024")
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        return sdf.format(date);
    }

    public String getReturnStatus() {
        if (returnDate == null) {
            return "No return date set";
        }

        Date currentDate = new Date();
        long diffInMillis = returnDate.getTime() - currentDate.getTime();
        long diffInDays = diffInMillis / (1000 * 60 * 60 * 24); // Conversion de millisecondes en jours

        if (diffInDays < 0) {
            return "Returned late"; // Le livre est en retard
        } else if (diffInDays <= 3) {
            return "Return imminent"; // Le retour est imminent (moins de 3 jours)
        } else {
            return "On time"; // Le retour est à temps
        }
    }

    // Exemple de méthode pour mettre à jour automatiquement le statut
    public void updateReturnStatus() {
        if (returnDate == null) {
            this.returnStatus = "No return date set";
            return;
        }

        Date currentDate = new Date();

        if (currentDate.after(returnDate)) {
            this.returnStatus = "Returned late"; // Retour en retard
        } else {
            this.returnStatus = "Returned"; // Retour à temps
        }
    }


    public void markAsReturned() {
        this.returnStatus = "Returned"; // Statut explicite
    }


}