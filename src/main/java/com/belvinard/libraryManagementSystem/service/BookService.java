package com.belvinard.libraryManagementSystem.service;

import com.belvinard.libraryManagementSystem.data.LibraryData;
import com.belvinard.libraryManagementSystem.exception.BookAlreadyExistsException;
import com.belvinard.libraryManagementSystem.exception.BookNotFoundException;
import com.belvinard.libraryManagementSystem.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookService {

    private final LibraryData libraryData;
    private List<Book> books;
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);


    @Autowired
    public BookService(LibraryData libraryData) {
        this.libraryData = libraryData;
        this.books = new ArrayList<>(); // Initialisation de la liste
    }


    /* ===================================== Method to add book  ===================================== */

    public void addBook(Book book) {
        try {
            // Log pour afficher la valeur du nombre de copies
            logger.info("Trying to add book with ISBN {}. Number of copies: {}", book.getISBN(), book.getNumberOfCopies());

            // Vérifiez que le nombre de copies est positif avant d'ajouter
            if (book.getNumberOfCopies() <= 0) {
                logger.error("Cannot add book with ISBN {}: No copies available.", book.getISBN());
                throw new IllegalArgumentException("Error: No copies available.");
            }

            // Appel à la méthode d'ajout de livre dans LibraryData
            libraryData.addBook(book);

            // Log de l'ajout du livre
            logger.info("Book added successfully: ISBN={} Title={} Author={} Available={}",
                    book.getISBN(), book.getTitle(), book.getAuthor(), book.isAvailable());

        } catch (BookAlreadyExistsException e) {
            logger.error("Failed to add book: {}", e.getMessage());
            throw e; // Propager l'exception après l'avoir loggée
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
            // Vérifier que le livre et l'ISBN ne sont pas nuls
            if (updatedBook == null || updatedBook.getISBN() == null) {
                throw new IllegalArgumentException("Book or ISBN cannot be null");
            }

            // Trouver le livre avec l'ISBN correspondant
            boolean bookUpdated = false;
            for (int i = 0; i < books.size(); i++) {
                Book existingBook = books.get(i);
                if (existingBook.getISBN().equals(updatedBook.getISBN())) {
                    // Si un livre avec le même ISBN est trouvé, le mettre à jour
                    books.set(i, updatedBook);
                    bookUpdated = true;
                    break; // Sortir de la boucle une fois le livre mis à jour
                }
            }

            // Si aucun livre n'a été trouvé, on peut soit lancer une exception, soit l'ajouter
            if (!bookUpdated) {
                throw new IllegalArgumentException("Book with ISBN " + updatedBook.getISBN() + " not found");
            }

            // Optionnel: Message confirmant que le livre a été mis à jour
            System.out.println("Book with ISBN " + updatedBook.getISBN() + " has been updated.");

        } catch (IllegalArgumentException e) {
            // En cas d'erreur, loggez et relancez l'exception
            logger.error("Failed to update book: {}", e.getMessage());
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
    // Méthode pour effectuer une recherche linéaire parmi tous les livres
    public List<Book> linearSearchBooks(String query, String searchField) {

        // Initialiser une liste pour stocker les résultats correspondants
        List<Book> result = new ArrayList<>();

        // Parcourir tous les livres disponibles dans la collection
        for (Book book : libraryData.getAllBooks()) {
            // Vérifier le champ spécifié pour effectuer la recherche
            switch (searchField.toLowerCase()) {
                case "title": // Recherche par titre
                    if (book.getTitle().equalsIgnoreCase(query)) { // Comparer en ignorant la casse
                        result.add(book); // Ajouter le livre à la liste des résultats
                    }
                    break;

                case "author": // Recherche par auteur
                    if (book.getAuthor().equalsIgnoreCase(query)) { // Comparer en ignorant la casse
                        result.add(book); // Ajouter le livre à la liste des résultats
                    }
                    break;

                case "genre": // Recherche par genre
                    if (book.getGenre().equalsIgnoreCase(query)) { // Comparer en ignorant la casse
                        result.add(book); // Ajouter le livre à la liste des résultats
                    }
                    break;

                case "isbn": // Recherche par ISBN
                    if (book.getISBN().equalsIgnoreCase(query)) { // Comparer en ignorant la casse
                        result.add(book); // Ajouter le livre à la liste des résultats
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Invalid search field: " + searchField);
            }
        }

        // Imprimer le nombre de livres trouvés
        System.out.println("Number of books found: " + result.size());

        // Retourner la liste des résultats (peut être vide si aucun livre ne correspond)
        return result;
    }

    // ******************************************* Binary Search
    public List<Book> binarySearchBooks(String query, String searchField) {
        List<Book> books = new ArrayList<>(libraryData.getAllBooks());
        Comparator<Book> comparator;

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

        books.sort(comparator);

        Book dummyBook = new Book(query);
        int index = Collections.binarySearch(books, dummyBook, comparator);

        if (index >= 0) {
            System.out.println("Number of books found: 1");
            return List.of(books.get(index));
        } else {
            System.out.println("Number of books found: 0");
            return List.of();
        }


    }

    /* ===================================== Methods to Sort book  ===================================== */

    public void bubbleSort(List<Book> books, String sortBy) {
        Comparator<Book> comparator = getComparator(sortBy);
        int n = books.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (comparator.compare(books.get(j), books.get(j + 1)) > 0) {
                    // Swap
                    Collections.swap(books, j, j + 1);
                }
            }
        }
    }

    public void selectionSort(List<Book> books, String sortBy) {
        Comparator<Book> comparator = getComparator(sortBy);
        int n = books.size();
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (comparator.compare(books.get(j), books.get(minIndex)) < 0) {
                    minIndex = j;
                }
            }
            // Swap
            Collections.swap(books, i, minIndex);
        }
    }

    public void quickSort(List<Book> books, int low, int high, String sortBy) {
        if (low < high) {
            Comparator<Book> comparator = getComparator(sortBy);
            int pivotIndex = partition(books, low, high, comparator);

            // Tri récursif
            quickSort(books, low, pivotIndex - 1, sortBy);
            quickSort(books, pivotIndex + 1, high, sortBy);
        }
    }

    private int partition(List<Book> books, int low, int high, Comparator<Book> comparator) {
        Book pivot = books.get(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (comparator.compare(books.get(j), pivot) <= 0) {
                i++;
                Collections.swap(books, i, j);
            }
        }
        Collections.swap(books, i + 1, high);
        return i + 1;
    }

    private Comparator<Book> getComparator(String sortBy) {
        switch (sortBy.toLowerCase()) {
            case "title":
                return Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER);
            case "author":
                return Comparator.comparing(Book::getAuthor, String.CASE_INSENSITIVE_ORDER);
            case "year":
                return Comparator.comparingInt(Book::getPublicationYear);
            case "genre":
                return Comparator.comparing(Book::getGenre, String.CASE_INSENSITIVE_ORDER);
            default:
                throw new IllegalArgumentException("Invalid sort field: " + sortBy);
        }
    }



}