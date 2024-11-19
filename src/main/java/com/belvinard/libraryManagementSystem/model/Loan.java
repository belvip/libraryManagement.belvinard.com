package com.belvinard.libraryManagementSystem.model;

import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Loan {
    // Attributs
    @Setter
    @Getter
    private Book book;
    @Setter
    @Getter
    private User user;
    private Date loanDate;
    // Getter et setter pour la date de retour
    @Setter
    @Getter
    private Date returnDate;  // Nouvel attribut pour la date de retour
    @Getter
    private String returnStatus;  // Nouvel attribut pour le statut de retour



    // Constructeur
    public Loan(Book book, User user, Date loanDate, Date returnDate) {
        this.book = book;
        this.user = user;
        this.loanDate = loanDate;
        this.returnDate = returnDate; // Initialisation de la date de retour
        this.returnStatus = getReturnStatus(); // Initialiser returnStatus
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

    private String getReturnStatus() {
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
}
