package com.belvinard.libraryManagementSystem;

import com.belvinard.libraryManagementSystem.console.ConsoleHandler;
import com.belvinard.libraryManagementSystem.model.Book;
import com.belvinard.libraryManagementSystem.service.UserService;
import com.belvinard.libraryManagementSystem.console.UserInputHandler;
import com.belvinard.libraryManagementSystem.data.LibraryData;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.belvinard.libraryManagementSystem.config.AppConfig;

/**
 * The main entry point of the Library Management System application.
 * This class initializes necessary components and starts the user interaction.
 */
public class LibraryManagementSystemApplication {

    public static void main(String[] args) {
        // Initialisation du contexte Spring à partir de la classe de configuration AppConfig
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // Récupérer les beans depuis le contexte Spring
        ConsoleHandler consoleHandler = context.getBean(ConsoleHandler.class);
        UserService userService = context.getBean(UserService.class);  // Injection de UserService
        UserInputHandler userInputHandler = context.getBean(UserInputHandler.class);  // Injection de UserInputHandler

        // Optionnel : Si tu veux ajouter des fonctionnalités liées à l'utilisateur dans la console
        // consoleHandler.setUserService(userService);
        // consoleHandler.setUserInputHandler(userInputHandler);

        /*LibraryData libraryData = new LibraryData();

        // Test : Ajouter un livre
        Book book = new Book("Title", "Author", "Genre", "12345", 2024, true);
        libraryData.addBook(book);

        // Test : Rechercher le livre
        Book foundBook = libraryData.getBookByISBN("12345");
        if (foundBook != null) {
            System.out.println("Found book: " + foundBook);
        } else {
            System.out.println("Book not found!");
        }*/

        // Démarrer l'interaction avec l'utilisateur via ConsoleHandler
        consoleHandler.start();
    }
}
