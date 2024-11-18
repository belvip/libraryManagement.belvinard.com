package com.belvinard.libraryManagementSystem.data;

import com.belvinard.libraryManagementSystem.exception.BookAlreadyExistsException;
import com.belvinard.libraryManagementSystem.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class LibraryData {

    private static final Logger logger = LoggerFactory.getLogger(LibraryData.class);

    // HashMap pour stocker les livres avec l'ISBN comme clé
    private Map<String, Book> bookCollection = new HashMap<>();

    /**
     * Ajoute un livre à la collection si son ISBN n'est pas déjà utilisé.
     *
     * @param book le livre à ajouter
     * @throws BookAlreadyExistsException si un livre avec le même ISBN existe déjà
     */
    public void addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null.");
        }

        // Vérifier si le livre existe déjà
        if (bookCollection.containsKey(book.getISBN())) {
            logger.error("Failed to add book: A book with ISBN {} already exists.", book.getISBN());
            throw new BookAlreadyExistsException("A book with ISBN " + book.getISBN() + " already exists.");
        }

        // Ajouter le livre à la collection
        bookCollection.put(book.getISBN(), book);
        logger.info("Book with ISBN {} added successfully.", book.getISBN());
    }

    /**
     * Récupère tous les livres de la collection.
     *
     * @return une collection de tous les livres
     */
    public Collection<Book> getAllBooks() {
        return bookCollection.values();
    }

    /**
     * Recherche un livre par son ISBN.
     *
     * @param isbn l'ISBN du livre à rechercher
     * @return le livre trouvé ou null si non trouvé
     */
    public Book getBookByISBN(String isbn) {
        if (isbn == null || isbn.isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty.");
        }

        return bookCollection.get(isbn);
    }

    /**
     * Met à jour un livre dans la collection.
     *
     * @param updatedBook le livre mis à jour
     */
    public void updateBook(Book updatedBook) {
        if (updatedBook == null || updatedBook.getISBN() == null || updatedBook.getISBN().isEmpty()) {
            throw new IllegalArgumentException("Updated book or ISBN cannot be null.");
        }

        // Vérifier si le livre existe dans la collection
        if (!bookCollection.containsKey(updatedBook.getISBN())) {
            throw new IllegalArgumentException("No book found with ISBN " + updatedBook.getISBN());
        }

        // Mettre à jour le livre dans la collection
        bookCollection.put(updatedBook.getISBN(), updatedBook);
        logger.info("Book with ISBN {} updated successfully.", updatedBook.getISBN());
    }

    /**
     * Supprime un livre de la collection par son ISBN.
     *
     * @param isbn l'ISBN du livre à supprimer
     * @return le livre supprimé
     */
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
