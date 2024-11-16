package com.belvinard.libraryManagementSystem.config;

import com.belvinard.libraryManagementSystem.data.LibraryData;
import com.belvinard.libraryManagementSystem.service.BookService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for defining Spring beans in the application.
 * This class provides the beans for the data layer and service layer.
 */
@Configuration
public class LibraryConfig {

    /**
     * Creates a bean for the LibraryData class.
     * This bean is used as the data storage component for books in the library.
     *
     * @return a new instance of LibraryData.
     */
    @Bean
    public LibraryData libraryData() {
        return new LibraryData();
    }

    /**
     * Creates a bean for the BookService class.
     * This bean is used to manage book-related business logic in the library system.
     *
     * @param libraryData the LibraryData bean to be injected into BookService
     * @return a new instance of BookService, injected with the LibraryData bean.
     */
    @Bean
    public BookService bookService(LibraryData libraryData) {
        return new BookService(libraryData);
    }
}
