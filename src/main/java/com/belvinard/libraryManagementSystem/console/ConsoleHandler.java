package com.belvinard.libraryManagementSystem.console;

import com.belvinard.libraryManagementSystem.model.Book;
import com.belvinard.libraryManagementSystem.service.BookService;
import com.belvinard.libraryManagementSystem.util.BookSortService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

@Component
public class ConsoleHandler {

    public static List<Book> books = new ArrayList<>();
    @Autowired
    private final BookService bookService;
    private final BookSortService bookSortService;
    private Scanner scanner = new Scanner(System.in);

    public ConsoleHandler(BookService bookService, Scanner scanner) {
        this.bookService = bookService;
        this.bookSortService = new BookSortService(books);
    }

    /*
     * ============================= Console starts =============================
     */

    public void start() {
        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getChoiceInput(); // Get the user's choice
            scanner.nextLine();  // Consume the newline character

            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    displayAllBooks();  // Afficher tous les livres
                    break;
                case 7:
                    running = false;
                    System.out.println("Exiting the system...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    return;
            }
        }
    }

    private int getChoiceInput() {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.nextLine();  // Clear invalid input
            // return -1;  // Return an invalid choice to prompt re-entry
            return scanner.nextInt();
        }
    }

    private void displayMenu() {
        System.out.println("\n----- The Library Management System Portal -----");
        System.out.println(
                "\nPress 1 for Adding Book \n" +
                        "Press 2 for Displaying All Books \n" +
                        "Press 3 for Updating Book \n" +
                        "Press 4 for Removing Book \n" +
                        "Press 5 for Searching Book \n" +
                        "Press 6 for Sorting Book \n" +
                        "Press 7 for Exiting the portal\n"
        );
        System.out.print("Enter your choice: ");
    }

    /* ================================================ Method to add book =================================== */
    private void addBook() {
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();

        System.out.print("Enter book author: ");
        String author = scanner.nextLine();

        System.out.print("Enter book genre: ");
        String genre = scanner.nextLine();

        String isbn = "";
        boolean validIsbn = false;
        while (!validIsbn) {
            System.out.print("Enter book ISBN: ");
            isbn = scanner.nextLine();
            // Vérifier que l'ISBN n'est pas vide
            if (isbn.isEmpty()) {
                System.out.println("ISBN cannot be empty. Please enter a valid ISBN.");
            } else {
                validIsbn = true;
            }
        }

        int year = 0;
        boolean validYear = false;
        while (!validYear) {
            System.out.print("Enter publication year: ");
            try {
                year = scanner.nextInt();  // Attente d'un entier pour l'année
                scanner.nextLine();  // Consommer la nouvelle ligne restante
                validYear = true;  // L'année est valide, on sort de la boucle
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number for the year.");
                scanner.nextLine();  // Consommer l'entrée invalide pour éviter la boucle infinie
            }
        }

        System.out.println("\n========= Details Book entered : ========== \n");
        System.out.println("Book Title: " + title);
        System.out.println("Book Author: " + author);
        System.out.println("Book Genre: " + genre);
        System.out.println("Book ISBN: " + isbn);
        System.out.println("Book Year: " + year);

        try {
            Book book = new Book();
            book.setTitle(title);
            book.setAuthor(author);
            book.setGenre(genre);
            book.setISBN(isbn);
            book.setPublicationYear(year);
            bookService.addBook(book);  // Ajouter le livre via le service
            System.out.println("Book added successfully.");
        } catch (com.belvinard.libraryManagementSystem.exception.BookAlreadyExistsException e) {
            // Si l'ISBN est déjà utilisé
            System.out.println("Error: A book with ISBN " + isbn + " already exists. Please use a unique ISBN.");
        } catch (IllegalArgumentException e) {
            // Autre exception
            System.out.println("Error: " + e.getMessage());
        }
    }

   /* private void addBook() {
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();
        System.out.print("Enter book author: ");
        String author = scanner.nextLine();
        System.out.print("Enter book genre: ");
        String genre = scanner.nextLine();
        System.out.print("Enter book ISBN: ");
        String isbn = scanner.nextLine();
        System.out.print("Enter publication year: ");
        int year = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.println("\n========= Details Book entered : ========== \n");
        System.out.println("Book Title: " + title);
        System.out.println("Book Author: " + author);
        System.out.println("Book Genre: " + genre);
        System.out.println("Book ISBN: " + isbn);
        System.out.println("Book Year: " + year);

        try {
            // Créer un objet Book
            Book book = new Book();
            book.setTitle(title);
            book.setAuthor(author);
            book.setGenre(genre);
            book.setISBN(isbn);
            book.setPublicationYear(year);

            // Ajouter le livre via le service
            bookService.addBook(book); // Cela appelle la méthode dans BookService

            // Ce message ne sera affiché que si l'exception n'est pas lancée
            System.out.println("Book added successfully.");
        } catch (com.belvinard.libraryManagementSystem.exception.BookAlreadyExistsException e) {
            // Si l'ISBN est déjà utilisé
            System.out.println("Error: A book with ISBN " + isbn + " already exists. Please use a unique ISBN.");
        } catch (IllegalArgumentException e) {
            // Autre exception
            System.out.println("Error: " + e.getMessage());
        }
    } */

    /* ================================================ Method to display all books =================================== */

    public void displayAllBooks(){
        List<Book> books = bookService.getAllBooks();

        if(books.isEmpty()){
            System.out.println("No books have been added yet.");
        }else {
            System.out.println("\n================= List of Books =================");
            for (Book book : books){
                System.out.println(book);
                System.out.println("-----------------------------------------------------");
            }
        }

        // Afficher le nombre de livres dans la liste
        System.out.println("\nNumber of books in the list: " + books.size());
    }

}
