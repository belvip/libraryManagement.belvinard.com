package com.belvinard.libraryManagementSystem.service;

import com.belvinard.libraryManagementSystem.model.Book;
import com.belvinard.libraryManagementSystem.model.Loan;
import com.belvinard.libraryManagementSystem.model.User;
import com.belvinard.libraryManagementSystem.data.LibraryData;
import com.belvinard.libraryManagementSystem.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    // Récupérer un utilisateur par son nom d'utilisateur
    public User getUserByUsername(String username) {
        User user = users.get(username);
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


    // Exemple d'une méthode borrowBook dans UserService
    // Méthode pour emprunter un livre
    public void borrowBook(User user, Book book) {
        // Vérifier si l'utilisateur a atteint sa limite d'emprunts
        if (user.hasReachedBorrowLimit()) {
            throw new IllegalArgumentException("Borrow limit reached.");
        }

        // Créer un objet Loan avec la date actuelle
        Loan loan = new Loan(book, user, new Date());  // Passer la date actuelle

        // Ajouter l'emprunt à l'historique de l'utilisateur
        user.addLoan(loan);

        // Mettre à jour l'état du livre (marquer comme emprunté)
        book.setAvailable(false);  // Supposer que la méthode setAvailable existe dans Book
        System.out.println("Book borrowed successfully.");
    }
}
