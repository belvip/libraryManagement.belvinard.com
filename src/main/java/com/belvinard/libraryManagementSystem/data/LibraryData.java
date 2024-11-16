package com.belvinard.libraryManagementSystem.data;

import com.belvinard.libraryManagementSystem.exception.BookAlreadyExistsException;
import com.belvinard.libraryManagementSystem.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class LibraryData {

    public static final Logger logger = LoggerFactory.getLogger(LibraryData.class);

    // HashMap pour stocker les livres, avec l'ISBN comme clé pour un accès rapide
    private Map<String, Book> bookCollection = new HashMap<>();

    /**
     * Ajoute un livre à la collection si son ISBN n'est pas déjà utilisé.
     *
     * @param book le livre à ajouter
     * @throws BookAlreadyExistsException si un livre avec le même ISBN existe déjà
     */
    public void addBook(Book book) {
        if (book != null) {
            // Vérifier si un livre avec le même ISBN existe déjà
            if (bookCollection.containsKey(book.getISBN())) {
                throw new BookAlreadyExistsException("A book with ISBN " + book.getISBN() + " already exists.");
            }

            // Ajouter le livre à la collection
            bookCollection.put(book.getISBN(), book);
            logger.info("Book with ISBN {} added successfully.", book.getISBN());
        } else {
            logger.warn("Attempted to add a null book.");
        }
    }

    /* Méthode pour afficher les livres (facultative, pour tester facilement)
    public Collection<Book> getAllBooks() {
        return bookCollection.values();
    }*/
}
