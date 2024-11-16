package com.belvinard.libraryManagementSystem.config;

import com.belvinard.libraryManagementSystem.console.ConsoleHandler;
import com.belvinard.libraryManagementSystem.data.LibraryData;
import com.belvinard.libraryManagementSystem.service.BookService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Scanner;

@Configuration  // Indique que cette classe contient des configurations Spring
public class AppConfig {

    @Bean
    public LibraryData libraryData() {
        return new LibraryData();  // Crée une instance de LibraryData
    }

    @Bean
    public BookService bookService() {
        return new BookService(libraryData());  // Injecte LibraryData dans BookService
    }

    @Bean
    public Scanner scanner() {
        return new Scanner(System.in);  // Crée une instance de Scanner
    }

    @Bean
    public ConsoleHandler consoleHandler() {
        return new ConsoleHandler(bookService(), scanner());  // Injecte BookService et Scanner dans ConsoleHandler
    }
}
