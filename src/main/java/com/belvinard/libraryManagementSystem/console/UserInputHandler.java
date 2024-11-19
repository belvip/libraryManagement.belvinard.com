package com.belvinard.libraryManagementSystem.console;

import com.belvinard.libraryManagementSystem.service.UserService;
import com.belvinard.libraryManagementSystem.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Scanner;

/*
* La classe UserInputHandler dans votre projet semble être responsable de la gestion de l'interaction entre
* l'utilisateur et l'application en ce qui concerne les opérations liées à un utilisateur
*
 */

@Component
public class UserInputHandler {

    @Autowired
    private UserService userService;

    private Scanner scanner = new Scanner(System.in);

    // Constructeur avec UserService et Scanner
    public UserInputHandler(UserService userService, Scanner scanner) {
        this.userService = userService;
        this.scanner = scanner;
    }

    // Demander à l'utilisateur de saisir un nom d'utilisateur et un mot de passe pour créer un nouvel utilisateur
    public User createUser(String providedUsername) {
        try {
            // Si un nom d'utilisateur est fourni, l'utiliser directement
            String username = providedUsername;
            if (username == null || username.trim().isEmpty()) {
                System.out.print("Enter username: ");
                username = scanner.nextLine();
            }

            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            System.out.print("Enter full name: ");
            String fullName = scanner.nextLine();

            System.out.print("Enter email: ");
            String email = scanner.nextLine();

            System.out.print("Enter phone number: ");
            String phoneNumber = scanner.nextLine();

            System.out.print("Enter address: ");
            String address = scanner.nextLine();

            // Créer un utilisateur avec les informations saisies
            User newUser = new User();
            newUser.setUsername(username); // Validation dans le setter
            newUser.setPassword(password);
            newUser.setFullName(fullName);
            newUser.setEmail(email);
            newUser.setPhoneNumber(phoneNumber);
            newUser.setAddress(address);

            // Utilisation d'une limite d'emprunts par défaut
            newUser.setBorrowLimit(5);

            // Ajouter l'utilisateur à la liste des utilisateurs via UserService
            userService.addUser(newUser);

            System.out.println("User created successfully!");
            return newUser; // Retourner l'utilisateur créé
        } catch (IllegalArgumentException e) {
            System.out.println("Error creating user: " + e.getMessage());
            return null; // Retourner null en cas d'échec
        }
    }

    // Demander à l'utilisateur de saisir son nom d'utilisateur pour récupérer ses informations
    public void getUserDetails() {
        System.out.print("Enter username to retrieve details: ");
        String username = scanner.nextLine();

        try {
            User user = userService.getUserByUsername(username);
            System.out.println("User details: " + user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Demander à l'utilisateur de saisir un nom d'utilisateur et un mot de passe pour la mise à jour
    public void updateUser() {
        System.out.print("Enter username to update: ");
        String username = scanner.nextLine();

        try {
            User user = userService.getUserByUsername(username);

            System.out.print("Enter new password: ");
            String newPassword = scanner.nextLine();

            user.setPassword(newPassword);  // Mettre à jour le mot de passe
            userService.updateUser(user);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Demander à l'utilisateur de saisir un nom d'utilisateur pour suppression
    public void deleteUser() {
        System.out.print("Enter username to delete: ");
        String username = scanner.nextLine();

        try {
            userService.deleteUser(username);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
