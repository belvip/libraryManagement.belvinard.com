package com.belvinard.libraryManagementSystem.config;

import com.belvinard.libraryManagementSystem.activity.ActivityManager;
import com.belvinard.libraryManagementSystem.console.ConsoleHandler;
import com.belvinard.libraryManagementSystem.console.UserInputHandler;
import com.belvinard.libraryManagementSystem.data.LibraryData;
import com.belvinard.libraryManagementSystem.service.BookService;
import com.belvinard.libraryManagementSystem.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Scanner;

@Configuration  // Indique que cette classe contient des configurations Spring
@ComponentScan(basePackages = "com.belvinard.libraryManagementSystem")
public class AppConfig {

    @Bean
    public LibraryData libraryData() {
        return new LibraryData();  // Crée une instance de LibraryData
    }

    @Bean
    public UserService userService() {
        return new UserService();
    }

    @Bean
    public ActivityManager activityManager() {
        return new ActivityManager();
    }

    @Bean
    public UserInputHandler userInputHandler() {
        return new UserInputHandler(userService(), activityManager(), new Scanner(System.in));
    }


    @Bean
    public BookService bookService(LibraryData libraryData) {
        return new BookService(libraryData);  // Injecte LibraryData dans BookService
    }

    @Bean
    public Scanner scanner() {
        return new Scanner(System.in);  // Crée une instance de Scanner
    }

    @Bean
    public ConsoleHandler consoleHandler() {
        return new ConsoleHandler(
                userService(),                     // Service utilisateur
                bookService(libraryData()),        // Service des livres
                userInputHandler(),                // Gestionnaire des saisies utilisateur
                activityManager(),                 // Gestionnaire des activités
                scanner()                          // Scanner
        );
    }

}