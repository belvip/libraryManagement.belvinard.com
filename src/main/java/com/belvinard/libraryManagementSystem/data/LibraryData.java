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
            // Vérifier si l'ISBN existe déjà
            if (bookCollection.containsKey(book.getISBN())) {
                // Log l'exception
                logger.error("Failed to add book: A book with ISBN {} already exists.", book.getISBN());
                // Lever l'exception si l'ISBN existe déjà
                throw new BookAlreadyExistsException("A book with ISBN " + book.getISBN() + " already exists.");
            }

            // Ajouter le livre à la collection
            bookCollection.put(book.getISBN(), book);
            logger.info("Book with ISBN {} added successfully.", book.getISBN());
        }
    }

    // Méthode pour afficher les livres
    public Collection<Book> getAllBooks() {
        return bookCollection.values(); // Retourne toutes les valeurs de la map (tous les livres)
    }


     /* ========================== Mettre à jour un livre ========================== */

    // Rechercher un livre par son ISBN
     public Book getBookByISBN(String isbn) {
         if (isbn == null || isbn.isEmpty()) {
             throw new IllegalArgumentException("ISBN cannot be null or empty.");
         }

         if (!bookCollection.containsKey(isbn)) {
             throw new IllegalArgumentException("No book found with ISBN " + isbn);
         }

         return bookCollection.get(isbn);
     }

    // Méthode pour mettre à jour un livre

    // Méthode pour récupérer un livre par son ISBN
    public void updateBook(Book updatedBook) {
        if (updatedBook == null) {
            throw new IllegalArgumentException("The updated book object cannot be null.");
        }

        if (updatedBook.getISBN() == null || updatedBook.getISBN().isEmpty()) {
            throw new IllegalArgumentException("The ISBN of the updated book cannot be null or empty.");
        }

        if (!bookCollection.containsKey(updatedBook.getISBN())) {
            throw new IllegalArgumentException("No book found with ISBN " + updatedBook.getISBN());
        }

        // Mettre à jour le livre dans la collection
        bookCollection.put(updatedBook.getISBN(), updatedBook);

        // Log de confirmation
        logger.info("Book with ISBN {} updated successfully.", updatedBook.getISBN());
    }

    /* ========================== Supprimer un livre ========================== */

    public Book deleteBookByISBN(String isbn) {
        if (isbn == null || isbn.isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty.");
        }

        if (!bookCollection.containsKey(isbn)) {
            throw new IllegalArgumentException("No book found with ISBN " + isbn);
        }

        // Supprime le livre et retourne l'objet du livre supprimé
        return bookCollection.remove(isbn);
    }




}
