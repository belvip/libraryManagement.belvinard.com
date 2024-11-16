package com.belvinard.libraryManagementSystem;

import com.belvinard.libraryManagementSystem.console.ConsoleHandler;
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

        // Récupérer l'instance de ConsoleHandler depuis le contexte Spring
        ConsoleHandler consoleHandler = context.getBean(ConsoleHandler.class);

        // Démarrer l'interaction avec l'utilisateur via ConsoleHandler
        consoleHandler.start();
    }
}
