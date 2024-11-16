package com.belvinard.libraryManagementSystem.service;

import com.belvinard.libraryManagementSystem.data.LibraryData;
import com.belvinard.libraryManagementSystem.exception.BookAlreadyExistsException;
import com.belvinard.libraryManagementSystem.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class BookService {

    private final LibraryData libraryData;
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    @Autowired
    public BookService(LibraryData libraryData) {
        this.libraryData = libraryData;
    }


    public void addBook(Book book) {
        try {
            libraryData.addBook(book); // Appel à la méthode dans LibraryData
        } catch (BookAlreadyExistsException e) {
            logger.error("Failed to add book: {}", e.getMessage());
            // Propager l'exception après l'avoir loggée
            throw e;
        }
    }

    // Méthode pour récupérer tous les livres
    public List<Book> getAllBooks() {
        // Appel à la méthode de LibraryData pour récupérer tous les livres
        return new ArrayList<>(libraryData.getAllBooks());
    }
}
