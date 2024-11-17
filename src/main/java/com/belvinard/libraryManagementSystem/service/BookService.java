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

    /* ===================================== Method to add book  ===================================== */

    public void addBook(Book book) {
        try {
            libraryData.addBook(book); // Appel à la méthode dans LibraryData
        } catch (BookAlreadyExistsException e) {
            logger.error("Failed to add book: {}", e.getMessage());
            // Propager l'exception après l'avoir loggée
            throw e;
        }
    }

    /* ===================================== Method to display all books  ===================================== */
    // Méthode pour récupérer tous les livres
    public List<Book> getAllBooks() {
        // Appel à la méthode de LibraryData pour récupérer tous les livres
        return new ArrayList<>(libraryData.getAllBooks());
    }

    /* ===================================== Method to update book  ===================================== */
    public void updateBook(Book updatedBook) {
        try {
            libraryData.updateBook(updatedBook);
        } catch (IllegalArgumentException e){
            logger.error("Failed to update book : {}", e.getMessage());
            throw e;
        }
    }

    public Book getBookByISBN(String isbn) {
        try {
            return libraryData.getBookByISBN(isbn);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to find book with ISBN {}: {}", isbn, e.getMessage());
            throw e; // Vous pouvez relancer l'exception pour la gérer plus haut
        }
    }

}
