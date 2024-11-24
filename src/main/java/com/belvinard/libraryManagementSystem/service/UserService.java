package com.belvinard.libraryManagementSystem.service;

import com.belvinard.libraryManagementSystem.model.Book;
import com.belvinard.libraryManagementSystem.model.Loan;
import com.belvinard.libraryManagementSystem.model.User;
import com.belvinard.libraryManagementSystem.data.LibraryData;
import com.belvinard.libraryManagementSystem.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    private Map<String, User> users = new HashMap<>();  // Stockage des utilisateurs par leur nom d'utilisateur

    @Autowired
    private LibraryData libraryData;  // Référence à la bibliothèque

    // Ajouter un nouvel utilisateur
    public void addUser(User user) {
        if (user == null || user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new IllegalArgumentException("User or username cannot be null.");
        }

        // Vérifier si l'utilisateur existe déjà
        if (users.containsKey(user.getUsername())) {
            throw new IllegalArgumentException("User with username " + user.getUsername() + " already exists.");
        }

        users.put(user.getUsername(), user);
        System.out.println("User " + user.getUsername() + " added successfully.");
    }


    public void displayAllUsers() {
        if (users.isEmpty()) {
            System.out.println("No users available.");
            return;
        }

        // Afficher le nombre total d'utilisateurs
        System.out.println("Total number of users: " + users.size());

        // Itérer sur les entrées du Map (clé-valeur)
        for (Map.Entry<String, User> entry : users.entrySet()) {
            User user = entry.getValue();
            System.out.println("\nUser details:");
            System.out.println("Username: " + user.getUsername());
            System.out.println("Full Name: " + user.getFullName());
            System.out.println("Email: " + user.getEmail());
            System.out.println("Phone Number: " + user.getPhoneNumber());
            System.out.println("Address: " + user.getAddress());
            System.out.println("Borrow Limit: " + user.getBorrowLimit());
            System.out.println("Borrowed Books History: ");
            for (Loan loan : user.getBorrowedBooksHistory()) {
                System.out.println(loan);
            }
        }
    }



    // Récupérer un utilisateur par son nom d'utilisateur
    public User getUserByUsername(String username) throws UserNotFoundException {
        User user = users.get(username); // Supposons que `users` soit une map contenant tous les utilisateurs
        if (user == null) {
            throw new UserNotFoundException("User with username " + username + " not found.");
        }
        return user;
    }


    // Mettre à jour les informations d'un utilisateur
    public void updateUser(User user) {
        if (user == null || user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new IllegalArgumentException("User or username cannot be null.");
        }

        if (!users.containsKey(user.getUsername())) {
            throw new UserNotFoundException("User with username " + user.getUsername() + " not found.");
        }

        users.put(user.getUsername(), user);
        System.out.println("User " + user.getUsername() + " updated successfully.");
    }

    // Supprimer un utilisateur
    public void deleteUser(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }

        if (!users.containsKey(username)) {
            throw new UserNotFoundException("User with username " + username + " not found.");
        }

        users.remove(username);
        System.out.println("User " + username + " deleted successfully.");
    }



    public void borrowBook(User user, Book book) {
        // Vérifier si l'utilisateur a atteint sa limite d'emprunts
        if (user.hasReachedBorrowLimit()) {
            throw new IllegalArgumentException("Borrow limit reached.");
        }

        // Définir la date de retour (par exemple, 14 jours à partir de la date actuelle)
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, 14); // Retour prévu dans 14 jours
        Date returnDate = calendar.getTime(); // Calculer la date de retour

        // Créer un objet Loan avec la date actuelle (loanDate) et la date de retour (returnDate)
        Loan loan = new Loan(book, user, new Date(), returnDate);  // Appeler le constructeur avec 4 paramètres

        // Ajouter l'emprunt à l'historique de l'utilisateur
        user.addLoan(loan);

        // Mettre à jour l'état du livre (marquer comme emprunté)
        book.setAvailable(false);  // Supposer que la méthode setAvailable existe dans Book
        System.out.println("Book borrowed successfully.");
    }


    public boolean userExists(String username) {
        return users.containsKey(username); // Vérifie si l'utilisateur est déjà enregistré
    }

}
