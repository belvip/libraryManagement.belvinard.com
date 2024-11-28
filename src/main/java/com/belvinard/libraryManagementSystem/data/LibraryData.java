package com.belvinard.libraryManagementSystem.data;

import com.belvinard.libraryManagementSystem.exception.BookAlreadyExistsException;
import com.belvinard.libraryManagementSystem.exception.BookNotFoundException;
import com.belvinard.libraryManagementSystem.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class LibraryData {

    private static final Logger logger = LoggerFactory.getLogger(LibraryData.class);
    private Scanner scanner = new Scanner(System.in);

    // HashMap pour stocker les livres avec l'ISBN comme clé
    private Map<String, Book> bookCollection = new HashMap<>();

    /**
     * Ajoute un livre à la collection si son ISBN n'est pas déjà utilisé.
     *
     * @param book le livre à ajouter
     * @throws BookAlreadyExistsException si un livre avec le même ISBN existe déjà
     */

    public void addBook(Book book) {
        try {
            if (book == null) {
                throw new IllegalArgumentException("Book cannot be null.");
            }

            // Vérifier si le nombre de copies est valide (doit être supérieur à 0)
            if (book.getNumberOfCopies() <= 0) {
                throw new IllegalArgumentException("Error: No copies available.");
            }

            // Afficher le nombre de copies juste avant d'ajouter le livre pour débogage
            logger.info("Number of copies for book with ISBN {}: {}", book.getISBN(), book.getNumberOfCopies());

            // Vérifier si le livre existe déjà dans la collection
            if (bookCollection.containsKey(book.getISBN())) {
                throw new BookAlreadyExistsException("A book with ISBN " + book.getISBN() + " already exists.");
            }

            // Si des copies sont disponibles, marquer comme disponible
            book.setAvailable(true);  // Livre disponible

            // Ajouter le livre à la collection avec son ISBN comme clé
            bookCollection.put(book.getISBN(), book);

            // Log de l'ajout du livre, incluant sa disponibilité
            logger.info("Book with ISBN {} added successfully. Available={}", book.getISBN(), book.isAvailable());

        } catch (IllegalArgumentException | BookAlreadyExistsException e) {
            // Gestion des erreurs prévues
            logger.error("Error occurred while adding book: {}", e.getMessage());
            throw e; // Relancer l'exception si vous voulez la propager
        } catch (Exception e) {
            // Gestion des erreurs imprévues (non anticipées)
            logger.error("Unexpected error occurred while adding book: {}", e.getMessage());
            throw new RuntimeException("Unexpected error occurred.", e); // Propager une exception générique
        }
    }

    public void borrowBook(String isbn, String username) {
        if (!bookCollection.containsKey(isbn)) {
            throw new IllegalArgumentException("Book with ISBN " + isbn + " not found.");
        }

        Book book = bookCollection.get(isbn);
        if (book.getNumberOfCopies() <= 0) {
            throw new IllegalArgumentException("No copies available for book: " + book.getTitle());
        }

        // Mettre à jour le nombre de copies et la disponibilité
        book.setNumberOfCopies(book.getNumberOfCopies() - 1);
        book.setAvailable(book.getNumberOfCopies() > 0);

        logger.info("User {} borrowed book '{}'. Remaining copies: {}", username, book.getTitle(), book.getNumberOfCopies());
    }


    public Collection<Book> getAllBooks() {
        return bookCollection.values();
    }

    public Book getBookByISBN(String isbn) {
        if (isbn == null || isbn.isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty.");
        }

        return bookCollection.get(isbn);
    }


    public void updateBook(Book updatedBook) {
        if (updatedBook == null || updatedBook.getISBN() == null) {
            throw new IllegalArgumentException("Book or ISBN cannot be null.");
        }

        if (!bookCollection.containsKey(updatedBook.getISBN())) {
            throw new BookNotFoundException("Book with ISBN " + updatedBook.getISBN() + " not found.");
        }

        // Remplacer l'ancien livre par le nouveau dans la collection
        bookCollection.put(updatedBook.getISBN(), updatedBook);

        logger.info("Book with ISBN {} updated in the collection.", updatedBook.getISBN());
    }




    public Book deleteBookByISBN(String isbn) {
        if (isbn == null || isbn.isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty.");
        }

        Book removedBook = bookCollection.remove(isbn);
        if (removedBook == null) {
            throw new IllegalArgumentException("No book found with ISBN " + isbn);
        }

        logger.info("Book with ISBN {} removed successfully.", isbn);
        return removedBook;
    }
}