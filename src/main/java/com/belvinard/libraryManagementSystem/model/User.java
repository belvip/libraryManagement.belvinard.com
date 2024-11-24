package com.belvinard.libraryManagementSystem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    private String username;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String password;  // Mot de passe
    private String address;
    private List<Loan> borrowedBooksHistory = new ArrayList<>(); // Historique des emprunts
    @Setter
    private int borrowLimit; // Limite d'emprunts
    private List<Loan> loans;  // Liste des prêts de l'utilisateur

    // Constructeur avec seulement username et password
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.borrowLimit = 5;  // Limite d'emprunt par défaut
        System.out.println("User created with borrow limit: " + this.borrowLimit); // Pour débogage
        this.borrowedBooksHistory = new ArrayList<>();
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

    // Méthode pour supprimer un prêt de l'historique de l'utilisateur
    public void removeLoan(Loan loan) {
        if (loans != null) {
            loans.remove(loan);
        }
    }


    // Méthode pour ajouter un emprunt à l'historique ; Méthode pour ajouter un prêt à l'historique de l'utilisateur
    public void addLoan(Loan loan) {

        if (loans == null) {
            loans = new ArrayList<>();
        }
        loans.add(loan);

        borrowedBooksHistory.add(loan);
    }

    // Méthode pour vérifier si l'utilisateur a atteint sa limite d'emprunts
    public boolean hasReachedBorrowLimit() {

        long activeLoans = borrowedBooksHistory.stream()
                .filter(loan -> !loan.getReturnStatus().equals("Returned"))
                .count();
        return activeLoans >= borrowLimit;
        /*int activeLoans = 0;

        // Compter uniquement les prêts qui ne sont pas marqués comme "Returned"
        for (Loan loan : borrowedBooksHistory) {
            if (!"Returned".equals(loan.getReturnStatus())) {
                activeLoans++;
            }
        }

        // Comparer les prêts actifs avec la limite d'emprunt
        return activeLoans >= borrowLimit;*/
    }


    // Validation de la chaîne (non null et non vide)
    private void validateNotNullOrEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty.");
        }
    }

    // Validation pour le format de l'email
    private void validateEmailFormat(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!Pattern.matches(emailRegex, email)) {
            throw new IllegalArgumentException("Invalid email format.");
        }
    }

    // Setters avec validation
    public void setUsername(String username) {
        validateNotNullOrEmpty(username, "Username");
        if (username.length() < 3) {
            throw new IllegalArgumentException("Username must be at least 3 characters long.");
        }
        this.username = username;
    }

    public void setFullName(String fullName) {
        validateNotNullOrEmpty(fullName, "Full Name");
        if (fullName.length() < 3) {
            throw new IllegalArgumentException("Full Name must be at least 3 characters long.");
        }
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        validateNotNullOrEmpty(email, "Email");
        validateEmailFormat(email);
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        validateNotNullOrEmpty(phoneNumber, "Phone Number");

        // Regex pour vérifier le format d'un numéro camerounais valide
        // Le numéro doit commencer par +237 ou 237, suivi de 9 chiffres
        if (!Pattern.matches("^\\+237[0-9]{9}$|^237[0-9]{9}$", phoneNumber)) {
            throw new IllegalArgumentException("Phone number must be a valid Cameroon number starting with +237 or 237, followed by 9 digits.");
        }

        this.phoneNumber = phoneNumber;
    }


    public void setAddress(String address) {
        validateNotNullOrEmpty(address, "Address");

        // Vérifier que l'adresse est d'une longueur minimale
        if (address.length() < 5) {
            throw new IllegalArgumentException("Address must be at least 5 characters long.");
        }

        // Validation pour s'assurer que l'adresse contient uniquement un mot (lettres et éventuellement un tiret)
        String addressRegex = "^[a-zA-Z]+$";  // Permet uniquement des lettres, sans espaces ni chiffres
        if (!address.matches(addressRegex)) {
            throw new IllegalArgumentException("Address must only contain letters, with no spaces or numbers.");
        }

        this.address = address;
    }

    // Setter pour le mot de passe avec validation (par exemple, longueur minimale)
    public void setPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty or null.");
        }

        // Vérifier la longueur minimale du mot de passe
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long.");
        }

        // Regex pour valider le mot de passe :
        // - Au moins une lettre majuscule
        // - Au moins un chiffre
        // - Peut contenir des caractères spéciaux
        String passwordRegex = "(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}";

        if (!password.matches(passwordRegex)) {
            throw new IllegalArgumentException("Password must contain at least one uppercase letter, one number, and one special character.");
        }

        this.password = password;
    }


    // Méthode pour retourner un livre
    public void returnBook(Loan loan) {
        if (borrowedBooksHistory.contains(loan)) {
            loan.markAsReturned(); // Statut mis à jour
            borrowedBooksHistory.remove(loan); // Facultatif selon vos besoins
        } else {
            System.out.println("Loan not found in history.");
        }
    }


}