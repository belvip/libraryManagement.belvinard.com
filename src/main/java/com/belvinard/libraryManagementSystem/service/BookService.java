package com.belvinard.libraryManagementSystem.service;

import com.belvinard.libraryManagementSystem.data.LibraryData;
import com.belvinard.libraryManagementSystem.exception.BookAlreadyExistsException;
import com.belvinard.libraryManagementSystem.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final LibraryData libraryData;
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    @Autowired
    public BookService(LibraryData libraryData) {
        this.libraryData = libraryData;
    }

    /**
     * Ajoute un livre dans la bibliothèque.
     * Si un conflit d'ISBN est détecté, l'erreur est capturée et loggée.
     *
     * @param book le livre à ajouter
     * @return true si le livre est ajouté, false sinon
     */
    public boolean addBook(Book book) {
        try {
            libraryData.addBook(book);
            logger.info("Book successfully added: ISBN = {}, Title = {}", book.getISBN(), book.getTitle());
            return true; // Livre ajouté avec succès
        } catch (BookAlreadyExistsException e) {
            // Gérer l'exception ici
            logger.error("Failed to add book: {}", e.getMessage());
            return false; // Livre non ajouté à cause du conflit
        }
    }
}
