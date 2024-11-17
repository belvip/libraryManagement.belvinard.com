package com.belvinard.libraryManagementSystem.service;

import com.belvinard.libraryManagementSystem.data.LibraryData;
import com.belvinard.libraryManagementSystem.exception.BookAlreadyExistsException;
import com.belvinard.libraryManagementSystem.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    /* ===================================== Method to remove book  ===================================== */

    public Book deleteBookByISBN(String isbn) {
        try {
            Book removedBook = libraryData.deleteBookByISBN(isbn);
            logger.info("Book with ISBN {} removed successfully.", isbn);
            return removedBook;
        } catch (IllegalArgumentException e) {
            logger.error("Failed to remove book with ISBN {}: {}", isbn, e.getMessage());
            throw e; // Renvoyer l'exception pour qu'elle soit gérée plus haut
        }
    }

    /* ===================================== Methods to Search book  ===================================== */

    // ******************************************* Linear search
    public List<Book> linearSearchBooks(String query, String searchField) {
        List<Book> result = new ArrayList<>();
        for (Book book : libraryData.getAllBooks()) {
            switch (searchField.toLowerCase()) {
                case "title":
                    if (book.getTitle().equalsIgnoreCase(query)) {
                        result.add(book);
                    }
                    break;
                case "author":
                    if (book.getAuthor().equalsIgnoreCase(query)) {
                        result.add(book);
                    }
                    break;
                case "genre":
                    if (book.getGenre().equalsIgnoreCase(query)) {
                        result.add(book);
                    }
                    break;
                case "isbn":
                    if (book.getISBN().equalsIgnoreCase(query)) {
                        result.add(book);
                    }
                    break;
            }
        }
        return result;
    }


    // ******************************************* Binary search
    public List<Book> binarySearchBooks(String query, String searchField) {
        List<Book> books = new ArrayList<>(libraryData.getAllBooks());
        Comparator<Book> comparator;

        // Choisir le comparateur en fonction du champ
        switch (searchField.toLowerCase()) {
            case "title":
                comparator = Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER);
                break;
            case "author":
                comparator = Comparator.comparing(Book::getAuthor, String.CASE_INSENSITIVE_ORDER);
                break;
            case "genre":
                comparator = Comparator.comparing(Book::getGenre, String.CASE_INSENSITIVE_ORDER);
                break;
            case "isbn":
                comparator = Comparator.comparing(Book::getISBN, String.CASE_INSENSITIVE_ORDER);
                break;
            default:
                throw new IllegalArgumentException("Invalid search field: " + searchField);
        }

        // Trier les livres en fonction du champ
        books.sort(comparator);

        // Créer un objet "dummy" Book pour la recherche
        Book dummyBook = new Book(query);

        // Rechercher l'index de l'élément
        int index = Collections.binarySearch(books, dummyBook, comparator);

        if (index >= 0) {
            return List.of(books.get(index)); // Retourner le livre trouvé
        } else {
            return List.of(); // Aucun résultat trouvé
        }
    }




}
