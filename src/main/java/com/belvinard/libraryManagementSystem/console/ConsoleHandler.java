


package com.belvinard.libraryManagementSystem.console;

import com.belvinard.libraryManagementSystem.activity.Activity;
import com.belvinard.libraryManagementSystem.activity.ActivityManager;
import com.belvinard.libraryManagementSystem.data.LibraryData;
import com.belvinard.libraryManagementSystem.exception.BookAlreadyExistsException;
import com.belvinard.libraryManagementSystem.exception.BookNotFoundException;
import com.belvinard.libraryManagementSystem.exception.UserNotFoundException;
import com.belvinard.libraryManagementSystem.model.Book;
import com.belvinard.libraryManagementSystem.model.Loan;
import com.belvinard.libraryManagementSystem.model.User;
import com.belvinard.libraryManagementSystem.service.BookService;
import com.belvinard.libraryManagementSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class ConsoleHandler {

    public static List<Book> books = new ArrayList<>();
    @Autowired
    private final BookService bookService;
    @Autowired
    private UserInputHandler userInputHandler;
    @Autowired
    private UserService userService;
    @Autowired
    private LibraryData libraryData;

    private Scanner scanner = new Scanner(System.in);

    private final ActivityManager activityManager;
    private User user; // Utilisateur actuellement connecté
    private User currentUser; // Stocke l'utilisateur actuellement connecté




    @Autowired
    public ConsoleHandler(UserService userService, BookService bookService,
                          UserInputHandler userInputHandler,
                          ActivityManager activityManager,
                          Scanner scanner) {
        this.userService = userService;
        this.userInputHandler = userInputHandler;
        this.bookService = bookService;
        this.activityManager = activityManager;
        this.currentUser = null; // Initialise avec aucun utilisateur connecté

    }


    /*
     * ============================= CONSOLE =============================
     */

    public void start() {
        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getChoiceInput(); // Get the user's choice
            scanner.nextLine();  // Consume the newline character

            switch (choice) {
                case 1:
                    loginUser();  // Appeler la méthode pour connecter l'utilisateur;
                    break;
                case 2:
                    addBook();
                    break;
                case 3:
                    displayAllBooks();
                    break;
                case 4:
                    updateBook();
                    break;
                case 5:
                    removeBookByISBN();
                    break;
                case 6:
                    searchBooks();
                    break;
                case 7:
                    sortBooks();
                    break;
                case 8:
                    addUser();  // Ajouter un utilisateur
                    break;
                case 9:
                    displayUser();  // Afficher un utilisateur
                    break;
                case 10:
                    updateUser();  // Mettre à jour un utilisateur
                    break;
                case 11:
                    displayAllUsers();
                    break;
                case 12:
                    deleteUser();  // Supprimer un utilisateur
                    break;
                case 13:
                    borrowBook();  // Emprunter un livre
                    break;
                case 14:
                    returnBook();  // Emprunter un livre
                    break;
                case 15:
                    displayRecentActivities();  // Afficher les activités récentes
                    break;
                case 16:
                    running = false;
                    System.out.println("Exiting the system...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
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

    /* ^^^^^^^^^^^^^^^^^^^^^^^ Display Menu ^^^^^^^^^^^^^^^^^^^^^^^ */
    private void displayMenu() {
        System.out.println("\n===================== Library Management System Portal (Build with Spring Framework and Java) =====================");
        System.out.println();

        System.out.println("Please choose an option by entering the corresponding number:");
        System.out.println("--------------------------------------------------------------");

        // Affichage sous forme de tableau avec une largeur de colonne fixe pour les numéros et les actions
        System.out.printf("%-5s %-40s\n", "No", "Action");
        System.out.println("--------------------------------------------------------------");

        System.out.printf("%-5s %-40s\n", "1", "Login before adding a book");
        System.out.printf("%-5s %-40s\n", "2", "Add a new book");
        System.out.printf("%-5s %-40s\n", "3", "Display all books");
        System.out.printf("%-5s %-40s\n", "4", "Update book details");
        System.out.printf("%-5s %-40s\n", "5", "Remove a book");
        System.out.printf("%-5s %-40s\n", "6", "Search for a book");
        System.out.printf("%-5s %-40s\n", "7", "Sort books");
        System.out.printf("%-5s %-40s\n", "8", "Add a new user");
        System.out.printf("%-5s %-40s\n", "9", "Display user details");
        System.out.printf("%-5s %-40s\n", "10", "Update user details");
        System.out.printf("%-5s %-40s\n", "11", "Display all users");
        System.out.printf("%-5s %-40s\n", "12", "Delete a user");
        System.out.printf("%-5s %-40s\n", "13", "Borrow a book");
        System.out.printf("%-5s %-40s\n", "14", "Return a book");
        System.out.printf("%-5s %-40s\n", "15", "Display recent activities");
        System.out.printf("%-5s %-40s\n", "16", "Exit the portal");

        System.out.println("--------------------------------------------------------------");
        System.out.print("Enter your choice: ");
    }


    // SE CONNECTER EN TANT QUE Administrateur avant d'ajouter un livre
    private User loginUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            User user = userService.authenticateUser(username, password);  // Authentifier l'utilisateur
            System.out.println("Login successful. Welcome, " + user.getUsername() + "!");
            return user;
        } catch (UserNotFoundException e) {
            System.out.println("Error: " + e.getMessage());  // Afficher le message d'erreur spécifique pour l'utilisateur non trouvé
        } catch (IllegalArgumentException e) {
            System.out.println("Login failed: " + e.getMessage());  // Afficher un message pour le mot de passe incorrect
        }

        return null;  // Échec de la connexion, retourner null
    }

    /* ================================================ Method to add book =================================== */
    private void addBook() {
        // Vérifier si un utilisateur est connecté
        if (currentUser == null) {  // Vérifier si l'utilisateur est connecté
            System.out.println("You must be logged in to add a book.");
            currentUser = loginUser();  // Appeler la méthode de connexion
            if (currentUser == null) {  // Si l'utilisateur échoue à se connecter
                System.out.println("Login failed. Returning to the menu.");
                return;  // Sortir si l'utilisateur échoue à se connecter
            }
        }

        System.out.print("Enter book title: ");
        String title = scanner.nextLine();

        System.out.print("Enter book author: ");
        String author = scanner.nextLine();

        System.out.print("Enter book genre: ");
        String genre = scanner.nextLine();

        int numberOfCopies = 0;
        boolean validCopies = false;
        while (!validCopies) {
            System.out.print("Enter book copies: ");
            try {
                numberOfCopies = Integer.parseInt(scanner.nextLine());
                if (numberOfCopies <= 0) {
                    throw new NumberFormatException("Number of copies must be greater than zero.");
                }
                validCopies = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid positive number for the copies.");
            }
        }

        String isbn = "";
        boolean validIsbn = false;
        while (!validIsbn) {
            System.out.print("Enter book ISBN: ");
            isbn = scanner.nextLine();
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
                year = scanner.nextInt();
                scanner.nextLine(); // Consommer la nouvelle ligne restante
                validYear = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number for the year.");
                scanner.nextLine(); // Consommer l'entrée invalide pour éviter la boucle infinie
            }
        }

        try {
            Book book = new Book();
            book.setTitle(title);
            book.setAuthor(author);
            book.setGenre(genre);
            book.setNumberOfCopies(numberOfCopies);
            book.setISBN(isbn);
            book.setPublicationYear(year);
            bookService.addBook(book); // Ajouter le livre via le service

            // Enregistrer l'activité
            String description = "Added book: " + title + " by " + author + " (ISBN: " + isbn + ")";
            Activity addBookActivity = new Activity(currentUser.getUsername(), "Add Book", description);
            activityManager.addActivity(addBookActivity);

            System.out.println("Book added successfully.");
        } catch (BookAlreadyExistsException e) {
            System.out.println("Error: A book with ISBN " + isbn + " already exists. Please use a unique ISBN.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }


    /* ================================================ Method to display all books =================================== */

    private static final int PAGE_SIZE = 5; // Nombre de livres par page
    public void displayAllBooks() {
        List<Book> books = bookService.getAllBooks();

        if (books.isEmpty()) {
            System.out.println("No books have been added yet.");
            return;
        }

        int totalBooks = books.size();
        int totalPages = (int) Math.ceil((double) totalBooks / PAGE_SIZE);
        int currentPage = 1;

        Scanner scanner = new Scanner(System.in);
        String userInput;

        do {
            // Calcul des indices pour afficher la page actuelle
            int startIndex = (currentPage - 1) * PAGE_SIZE;
            int endIndex = Math.min(startIndex + PAGE_SIZE, totalBooks);

            // Afficher les livres de la page actuelle
            System.out.println("\n=========================== LIST OF BOOKS ===========================");
            System.out.println();

           // Afficher l'entête du tableau avec une bordure
            System.out.println("+-----------------+--------------------------------+------------------------+------------------------------+------------+------------+");
            System.out.printf("| %-15s | %-30s | %-22s | %-30s | %-10s | %-10s |\n",
                    "ISBN", "Title", "Author", "Genre", "Available", "Copies");
            System.out.println("+-----------------+--------------------------------+------------------------+------------------------------+------------+------------+");

            // Afficher les livres
            // Afficher les livres
            for (Book book : books) {
                System.out.printf("| %-15s | %-30s | %-22s | %-30s | %-10s | %-10d |\n",
                        book.getISBN(), book.getTitle(), book.getAuthor(), book.getGenre(),
                        (book.isAvailable() ? "Yes" : "No"),  // Formatage pour afficher "Yes" ou "No" pour la disponibilité
                        book.getNumberOfCopies());
            }

            // Afficher les instructions de navigation et options supplémentaires
            System.out.printf("\nPage %d of %d%n", currentPage, totalPages);
            System.out.println("Press 'n' for Next, 'p' for Previous, 'b' to Borrow a book, 'q' to Quit.");

            // Lire l'entrée utilisateur
            System.out.print("Your choice: ");
            userInput = scanner.nextLine().trim().toLowerCase();

            if (userInput.equals("n") && currentPage < totalPages) {
                currentPage++;
            } else if (userInput.equals("p") && currentPage > 1) {
                currentPage--;
            } else if (userInput.equals("b")) {
                // Emprunter un livre
                System.out.print("Enter ISBN of the book to borrow: ");
                String isbn = scanner.nextLine().trim();

                // Rechercher le livre par ISBN et emprunter
                Book bookToBorrow = books.stream()
                        .filter(book -> book.getISBN().equals(isbn))
                        .findFirst()
                        .orElse(null);

                if (bookToBorrow == null) {
                    System.out.println("Error: Book with ISBN " + isbn + " not found.");
                } else if (bookToBorrow.getNumberOfCopies() > 0) {
                    bookToBorrow.setNumberOfCopies(bookToBorrow.getNumberOfCopies() - 1);
                    if (bookToBorrow.getNumberOfCopies() == 0) {
                        bookToBorrow.setAvailable(false); // Marquer comme indisponible si plus de copies
                    }
                    System.out.println("You have successfully borrowed the book: " + bookToBorrow.getTitle());
                } else {
                    System.out.println("Error: No copies available for this book.");
                }
            } else if (!userInput.equals("q")) {
                System.out.println("Invalid choice. Please try again.");
            }

        } while (!userInput.equals("q"));

        System.out.println("Exiting book display.");
    }

    /* ================================================ Method to display with ISBN =================================== */
    // Afficher un livre par son ISBN
    private void displayBookByISBN() {
        System.out.print("Enter the ISBN of the book to display: ");
        String isbn = scanner.nextLine();

        try {
            Book book = bookService.getBookByISBN(isbn);
            System.out.println("\nBook details:");
            System.out.println(book);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    /* ================================================ Method to update books =================================== */
    private void updateBook() {
        System.out.println("\n==================== Update Book ====================");

        // Demander l'ISBN du livre à mettre à jour
        System.out.print("Enter the ISBN of the book to update: ");
        String isbn = scanner.nextLine().trim();

        try {
            // Récupérer le livre existant via BookService
            Book existingBook = bookService.getBookByISBN(isbn);

            // Afficher les détails actuels du livre
            System.out.println("\nCurrent Book Details:");
            System.out.printf("%-15s: %s%n", "Title", existingBook.getTitle());
            System.out.printf("%-15s: %s%n", "Author", existingBook.getAuthor());
            System.out.printf("%-15s: %s%n", "Genre", existingBook.getGenre());
            System.out.printf("%-15s: %d%n", "Copies", existingBook.getNumberOfCopies());
            System.out.printf("%-15s: %d%n", "Year", existingBook.getPublicationYear());

            // Demander les nouvelles valeurs à l'utilisateur
            System.out.println("\nEnter new details (leave blank to keep current):");
            System.out.print("New Title: ");
            String newTitle = scanner.nextLine().trim();
            System.out.print("New Author: ");
            String newAuthor = scanner.nextLine().trim();
            System.out.print("New Genre: ");
            String newGenre = scanner.nextLine().trim();
            System.out.print("New Number of Copies (enter 0 to keep current): ");
            int newNumberOfCopies = scanner.nextInt();
            System.out.print("New Publication Year (enter 0 to keep current): ");
            int newYear = scanner.nextInt();
            scanner.nextLine(); // Consommer la ligne restante

            // Conserver les anciennes valeurs pour l'activité
            String oldTitle = existingBook.getTitle();
            String oldAuthor = existingBook.getAuthor();
            String oldGenre = existingBook.getGenre();
            int oldNumberOfCopies = existingBook.getNumberOfCopies();
            int oldYear = existingBook.getPublicationYear();

            // Mettre à jour uniquement les champs modifiés
            if (!newTitle.isEmpty()) existingBook.setTitle(newTitle);
            if (!newAuthor.isEmpty()) existingBook.setAuthor(newAuthor);
            if (!newGenre.isEmpty()) existingBook.setGenre(newGenre);
            if (newNumberOfCopies > 0) existingBook.setNumberOfCopies(newNumberOfCopies);
            if (newYear > 0) existingBook.setPublicationYear(newYear);

            // Appeler la mise à jour via BookService
            bookService.updateBook(existingBook);

            // Afficher les détails mis à jour
            System.out.println("\nUpdated Book Details:");
            System.out.printf("%-15s: %s%n", "Title", existingBook.getTitle());
            System.out.printf("%-15s: %s%n", "Author", existingBook.getAuthor());
            System.out.printf("%-15s: %s%n", "Genre", existingBook.getGenre());
            System.out.printf("%-15s: %d%n", "Copies", existingBook.getNumberOfCopies());
            System.out.printf("%-15s: %d%n", "Year", existingBook.getPublicationYear());

            System.out.println("\nBook updated successfully!");

            // Enregistrer l'activité de mise à jour
            String description = String.format(
                    "Updated book with ISBN: %s. Changes: [Title: '%s' -> '%s', Author: '%s' -> '%s', Genre: '%s' -> '%s', Copies: %d -> %d, Year: %d -> %d]",
                    isbn,
                    oldTitle, existingBook.getTitle(),
                    oldAuthor, existingBook.getAuthor(),
                    oldGenre, existingBook.getGenre(),
                    oldNumberOfCopies, existingBook.getNumberOfCopies(),
                    oldYear, existingBook.getPublicationYear()
            );

            Activity updateActivity = new Activity("System", "Update Book", description);
            activityManager.addActivity(updateActivity);

            // Afficher les activités récentes
            activityManager.displayRecentActivities();

        } catch (BookNotFoundException e) {
            System.out.println("Error: Book not found with ISBN " + isbn);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /* ===================================== Methods to Borrow Book  ===================================== */
    private void borrowBook() {
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        User user = null;
        int attempts = 3; // Limite de tentatives pour retrouver l'utilisateur

        while (attempts > 0) {
            try {
                user = userService.getUserByUsername(username);
                break; // Sortir de la boucle si l'utilisateur est trouvé
            } catch (UserNotFoundException e) {
                System.out.println("User with username " + username + " not found.");
                attempts--;
                if (attempts > 0) {
                    System.out.print("Enter your username again: ");
                    username = scanner.nextLine();
                }
            }
        }

        // Si aucun utilisateur n'est trouvé, permettre d'en créer un nouveau
        if (user == null) {
            System.out.println("User not found after multiple attempts. You can now create a new user.");
            user = userInputHandler.createUser(username); // Création d'un nouvel utilisateur
        }

        System.out.print("Enter the ISBN of the book you want to borrow: ");
        String isbn = scanner.nextLine();

        // Récupérer le livre via la bibliothèque
        Book book = libraryData.getBookByISBN(isbn);
        if (book == null) {
            System.out.println("Error: Book with ISBN " + isbn + " not found.");
            return;
        }

        // Vérifier la disponibilité du livre
        if (!book.isAvailable()) {
            System.out.println("Sorry, the book is currently unavailable for borrowing.");
            return;
        }

        // Vérifier si l'utilisateur a atteint la limite d'emprunts
        if (user.hasReachedBorrowLimit()) {
            System.out.println("Error: Borrow limit reached.");
            return;
        }

        // Créer un nouvel emprunt
        Loan loan = new Loan(book, user, new Date(), calculateReturnDate()); // UNE SEULE DÉCLARATION ICI

        // Ajouter le prêt à l'historique de l'utilisateur
        user.addLoan(loan);
        System.out.println("Loan added to your history.");

        // Marquer le livre comme emprunté et diminuer le nombre de copies disponibles
        book.markAsBorrowed();
        System.out.println("The book has been marked as borrowed.");

        // Afficher l'état actuel de la bibliothèque
        System.out.println("\nCurrent library state:");
        libraryData.getAllBooks().forEach(currentBook -> {
            System.out.println("ISBN: " + currentBook.getISBN() + ", Copies: " + currentBook.getNumberOfCopies());
        });

        // Mettre à jour la base de données et l'historique utilisateur
        try {
            libraryData.updateBook(book); // Mettre à jour le livre dans la bibliothèque
            userService.borrowBook(user, book); // Mettre à jour l'utilisateur

            // Enregistrer l'activité d'emprunt
            String description = "Borrowed book: " + book.getTitle() + " (ISBN: " + book.getISBN() + ")";
            Activity borrowActivity = new Activity(user.getUsername(), "Borrow Book", description);
            activityManager.addActivity(borrowActivity);

            System.out.println("Book borrowed successfully.");

            // Afficher les activités récentes
            activityManager.displayRecentActivities();
        } catch (Exception e) {
            System.err.println("Error occurred while borrowing the book: " + e.getMessage());
        }

        // Vérification et affichage pour le diagnostic
        System.out.println("Borrowed Books History for " + user.getUsername() + ":");
        user.getBorrowedBooksHistory().forEach(System.out::println);
        System.out.println("Active loans: " + user.getBorrowedBooksHistory().stream()
                .filter(loanItem -> !loanItem.getReturnStatus().equals("Returned"))
                .count());
    }


    /* ================================================ Method to RETURN books =================================== */

    private void returnBook() {
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        User user = null;
        int attempts = 3; // Limite le nombre de tentatives

        // Vérification de l'utilisateur
        while (attempts > 0) {
            try {
                user = userService.getUserByUsername(username);
                break; // Si trouvé, sortir de la boucle
            } catch (UserNotFoundException e) {
                System.out.println("User with username " + username + " not found.");
                attempts--;
                if (attempts > 0) {
                    System.out.print("Enter your username again: ");
                    username = scanner.nextLine();
                }
            }
        }

        if (user == null) {
            System.out.println("User not found after multiple attempts. Returning to main menu.");
            return;
        }

        System.out.print("Enter the ISBN of the book you want to return: ");
        String isbn = scanner.nextLine();

        // Vérification de l'existence du livre
        Book book = libraryData.getBookByISBN(isbn);
        if (book == null) {
            System.out.println("Error: Book with ISBN " + isbn + " not found.");
            return;
        }

        // Vérification que l'utilisateur a bien emprunté le livre
        Loan loan = user.getLoanByBook(book);
        if (loan == null) {
            System.out.println("Error: This book was not borrowed by this user.");
            return;
        }

        // Marquer le prêt comme retourné
        loan.markAsReturned();

        // Supprimer le prêt de l'historique, ou le garder avec le statut mis à jour
        user.returnBook(loan);

        // Marquer le livre comme retourné
        book.markAsReturned();
        System.out.println("The book has been marked as returned.");


        // Mettre à jour le statut du prêt
        loan.updateReturnStatus();  // Mise à jour du statut de retour du prêt
        System.out.println("The loan status has been updated to 'Returned'.");

        // Supprimer le prêt de l'historique de l'utilisateur
        user.removeLoan(loan);
        System.out.println("The loan has been removed from your history.");

        // Mettre à jour la bibliothèque et l'historique de l'utilisateur
        try {
            libraryData.updateBook(book); // Mettre à jour le livre dans la base de données
            userService.updateUser(user); // Mettre à jour l'historique de l'utilisateur
            System.out.println("Book returned successfully.");

            // Enregistrer l'activité de retour
            String description = "Returned book: " + book.getTitle() + " (ISBN: " + book.getISBN() + ")";
            Activity returnActivity = new Activity(user.getUsername(), "Return Book", description);
            activityManager.addActivity(returnActivity);

            // Afficher les activités récentes
            activityManager.displayRecentActivities();
        } catch (Exception e) {
            System.err.println("Error occurred while returning the book: " + e.getMessage());
        }
    }



    /* ================================================ Method to remove books =================================== */

    private void removeBookByISBN() {
        System.out.print("Enter the ISBN of the book to remove: ");
        String isbn = scanner.nextLine();

        User user = null;  // Variable pour stocker l'utilisateur actuel

        try {
            // Demander le nom d'utilisateur pour récupérer l'utilisateur actuel
            System.out.print("Enter your username: ");
            String username = scanner.nextLine();

            // Récupérer l'utilisateur par son nom d'utilisateur
            user = userService.getUserByUsername(username);

            // Récupérer le livre à supprimer pour afficher ses détails
            Book book = bookService.getBookByISBN(isbn);
            System.out.println("\nBook details:");
            System.out.println(book);

            // Demander confirmation
            System.out.print("Are you sure you want to remove this book? (yes/no): ");
            String confirmation = scanner.nextLine().trim().toLowerCase();

            if (confirmation.equals("yes")) {
                bookService.deleteBookByISBN(isbn);
                System.out.println("The book has been successfully removed.");

                // Enregistrer l'activité de suppression
                String description = "Removed book " + book.getTitle() + " ISBN: " + book.getISBN();
                Activity removeActivity = new Activity(user.getUsername(), "Remove Book", description);
                activityManager.addActivity(removeActivity);

                // Afficher les activités récentes
                activityManager.displayRecentActivities();

            } else {
                System.out.println("The book was not removed.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error occurred while removing the book: " + e.getMessage());
        } catch (UserNotFoundException e) {
            System.out.println("Error: User not found. Please ensure you are logged in.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }


    /* ================================================ Methods to search books =================================== */

    private void searchBooks() {
        System.out.println("\n==================== Search Books ====================");

        // Demander le nom d'utilisateur
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        User user;
        try {
            user = userService.getUserByUsername(username);
        } catch (UserNotFoundException e) {
            System.out.println("User not found. Please try again.");
            return;
        }

        // Choix du type de recherche
        System.out.println("\nSelect search type:");
        System.out.printf("%-5s | %-15s\n", "Option", "Search Type");
        System.out.println("-----------------------------");
        System.out.printf("%-5s | %-15s\n", "1", "Linear Search");
        System.out.printf("%-5s | %-15s\n", "2", "Binary Search");
        System.out.println("-----------------------------");
        System.out.print("Enter your choice: ");
        int searchType = scanner.nextInt();
        scanner.nextLine(); // Consommer la ligne restante

        if (searchType != 1 && searchType != 2) {
            System.out.println("Invalid search type. Returning to menu.");
            return;
        }

        // Choix du champ de recherche
        System.out.println("\nSelect search field:");
        System.out.printf("%-5s | %-15s\n", "Option", "Field");
        System.out.println("-----------------------------");
        System.out.printf("%-5s | %-15s\n", "1", "Title");
        System.out.printf("%-5s | %-15s\n", "2", "Author");
        System.out.printf("%-5s | %-15s\n", "3", "Genre");
        System.out.printf("%-5s | %-15s\n", "4", "ISBN");
        System.out.println("-----------------------------");
        System.out.print("Enter your choice: ");
        int searchFieldChoice = scanner.nextInt();
        scanner.nextLine(); // Consommer la ligne restante

        String searchField;
        switch (searchFieldChoice) {
            case 1 -> searchField = "title";
            case 2 -> searchField = "author";
            case 3 -> searchField = "genre";
            case 4 -> searchField = "isbn";
            default -> {
                System.out.println("Invalid field choice. Returning to menu.");
                return;
            }
        }

        // Demander la requête de recherche
        System.out.print("\nEnter the search query: ");
        String query = scanner.nextLine();

        // Enregistrer l'activité de recherche
        Activity searchBookActivity = new Activity(
                user.getUsername(),
                "Search Book",
                "Search Field: " + searchField + ", Query: " + query
        );
        activityManager.addActivity(searchBookActivity);

        // Effectuer la recherche
        List<Book> result;
        if (searchType == 1) {
            result = bookService.linearSearchBooks(query, searchField);
        } else {
            result = bookService.binarySearchBooks(query, searchField);
        }

        // Afficher les résultats
        System.out.println("\n==================== Search Results ====================");
        if (result.isEmpty()) {
            System.out.println("No books found matching the query.");
        } else {
            System.out.printf("%-15s %-30s %-20s %-10s %-15s\n", "ISBN", "Title", "Author", "Year", "Genre");
            System.out.println("-------------------------------------------------------------------------------");
            for (Book book : result) {
                System.out.printf(
                        "%-15s %-30s %-20s %-10d %-15s\n",
                        book.getISBN(), book.getTitle(), book.getAuthor(), book.getPublicationYear(), book.getGenre()
                );
            }
        }

        // Afficher les activités récentes
        activityManager.displayRecentActivities();
    }


    /* ================================================ Methods to sort books =================================== */
    private void sortBooks() {
        System.out.println("\n==================== Sorting Books ====================");

        // Choisir le critère de tri
        System.out.println("\nSelect sorting criterion:");
        System.out.printf("%-5s | %-15s\n", "Option", "Criterion");
        System.out.println("---------------------------");
        System.out.printf("%-5s | %-15s\n", "1", "Title");
        System.out.printf("%-5s | %-15s\n", "2", "Author");
        System.out.printf("%-5s | %-15s\n", "3", "Year");
        System.out.printf("%-5s | %-15s\n", "4", "Genre");
        System.out.println("---------------------------");
        System.out.print("Enter your choice: ");
        int criterionChoice = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        // Déterminer le champ de tri
        String sortBy;
        switch (criterionChoice) {
            case 1 -> sortBy = "title";
            case 2 -> sortBy = "author";
            case 3 -> sortBy = "year";
            case 4 -> sortBy = "genre";
            default -> {
                System.out.println("Invalid choice. Defaulting to Title.");
                sortBy = "title";
            }
        }

        // Choisir l'algorithme de tri
        System.out.println("\nSelect sorting algorithm:");
        System.out.printf("%-5s | %-20s\n", "Option", "Algorithm");
        System.out.println("---------------------------------------");
        System.out.printf("%-5s | %-20s\n", "1", "Bubble Sort");
        System.out.printf("%-5s | %-20s\n", "2", "Selection Sort");
        System.out.printf("%-5s | %-20s\n", "3", "QuickSort");
        System.out.println("---------------------------------------");
        System.out.print("Enter your choice: ");
        int algorithmChoice = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        // Récupérer les livres depuis le service
        List<Book> books = new ArrayList<>(bookService.getAllBooks());

        // Appliquer le tri
        String algorithmUsed;
        switch (algorithmChoice) {
            case 1 -> {
                algorithmUsed = "Bubble Sort";
                bookService.bubbleSort(books, sortBy);
            }
            case 2 -> {
                algorithmUsed = "Selection Sort";
                bookService.selectionSort(books, sortBy);
            }
            case 3 -> {
                algorithmUsed = "Quick Sort";
                bookService.quickSort(books, 0, books.size() - 1, sortBy);
            }
            default -> {
                System.out.println("Invalid choice. Defaulting to Bubble Sort.");
                algorithmUsed = "Bubble Sort (Default)";
                bookService.bubbleSort(books, sortBy);
            }
        }

        // Afficher les résultats
        System.out.println("\n==================== Sorted Books ====================");
        System.out.printf("%-15s %-30s %-20s %-10s %-15s\n", "ISBN", "Title", "Author", "Year", "Genre");
        System.out.println("-------------------------------------------------------------------------------");
        for (Book book : books) {
            System.out.printf(
                    "%-15s %-30s %-20s %-10d %-15s\n",
                    book.getISBN(), book.getTitle(), book.getAuthor(), book.getPublicationYear(), book.getGenre()
            );
        }
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("Total number of books sorted: " + books.size());

        // Enregistrer l'activité
        String username = (user != null) ? user.getUsername() : "Unknown User";
        Activity sortBooksActivity = new Activity(
                username,
                "Sort Books",
                "Sorted by: " + sortBy + ", Algorithm: " + algorithmUsed + ", Total books: " + books.size()
        );
        activityManager.addActivity(sortBooksActivity);

        // Afficher les activités récentes
        activityManager.displayRecentActivities();
    }


    private Date calculateReturnDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 14); // Exemple : Le retour doit se faire dans 14 jours
        return calendar.getTime();
    }


    // ********************** Gérer les utilisateurs

    /* ===================================== Methods to Add User  ===================================== */
    private void addUser() {
        try {
            System.out.println("Enter user details:");

            System.out.print("Username: ");
            String username = scanner.nextLine();

            // Vérification si l'utilisateur existe déjà
            if (userService.userExists(username)) {
                System.out.println("Error: A user with this username already exists.");
                return;
            }

            System.out.print("Full Name: ");
            String fullName = scanner.nextLine();

            System.out.print("Email: ");
            String email = scanner.nextLine();

            System.out.print("Password (e.g., MyPass@k48): ");
            String password = scanner.nextLine();

            System.out.print("Phone Number (e.g., +237696790755): ");
            String phoneNumber = scanner.nextLine();

            System.out.print("Address (e.g., emana): ");
            String address = scanner.nextLine();

            // Créer un utilisateur avec les détails saisis
            User newUser = new User(username, password); // Constructeur initialisant le borrowLimit
            newUser.setFullName(fullName);
            newUser.setEmail(email);
            newUser.setPhoneNumber(phoneNumber);
            newUser.setAddress(address);

            //System.out.println("Setting default borrow limit to: " + newUser.getBorrowLimit()); // Débogage

            // Ajouter l'utilisateur via le service
            userService.addUser(newUser);

            System.out.println("User added successfully with borrow limit: " + newUser.getBorrowLimit());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }


    /* ===================================== Methods to Display Unique User  ===================================== */

    private void displayUser() {
        System.out.print("Enter the username to display: ");
        String username = scanner.nextLine();

        try {
            // Récupérer l'utilisateur
            User user = userService.getUserByUsername(username);

            // Hachage du mot de passe avant l'affichage
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String hashedPassword = passwordEncoder.encode(user.getPassword());

            // Afficher les détails de l'utilisateur
            System.out.println("\nUser details:");
            System.out.println("---------------------------------------------------");
            System.out.printf("%-15s: %s%n", "Username", user.getUsername());
            System.out.printf("%-15s: %s%n", "Full Name", user.getFullName());
            System.out.printf("%-15s: %s%n", "Email", user.getEmail());
            System.out.printf("%-15s: %s%n", "Password", hashedPassword);  // Affichage du mot de passe haché
            System.out.printf("%-15s: %s%n", "Phone Number", user.getPhoneNumber());
            System.out.printf("%-15s: %s%n", "Address", user.getAddress());
            System.out.printf("%-15s: %d%n", "Borrow Limit", user.getBorrowLimit());
            System.out.println("---------------------------------------------------");
            System.out.println("Borrowed Books History:");

            // Mettre à jour dynamiquement les statuts des prêts avant de les afficher
            List<Loan> borrowedBooksHistory = user.getBorrowedBooksHistory();
            if (borrowedBooksHistory.isEmpty()) {
                System.out.println("No books borrowed.");
            } else {
                // Afficher l'en-tête du tableau
                System.out.println("---------------------------------------------------------------");
                System.out.printf("%-20s %-15s %-15s %-15s%n", "Book Title", "Loan Date", "Return Date", "Return Status");
                System.out.println("---------------------------------------------------------------");

                // Afficher chaque prêt
                for (Loan loan : borrowedBooksHistory) {
                    loan.updateReturnStatus(); // Mise à jour du statut du prêt
                    System.out.printf(
                            "%-20s %-15s %-15s %-15s%n",
                            loan.getBook().getTitle(),
                            formatDate(loan.getLoanDate()),
                            loan.getReturnDate() != null ? formatDate(loan.getReturnDate()) : "Not Returned",
                            loan.getReturnStatus()
                    );
                }
                System.out.println("---------------------------------------------------------------");
            }
        } catch (UserNotFoundException e) {
            System.out.println("Error: User with username '" + username + "' not found.");
            promptRetryDisplayUser();
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    // Méthode utilitaire pour formater les dates
    private String formatDate(Date date) {
        if (date == null) {
            return "N/A";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(date);
    }


    private void promptRetryDisplayUser() {
        System.out.print("\nWould you like to try again? (y/n): ");
        String choice = scanner.nextLine().trim();
        if (choice.equalsIgnoreCase("y")) {
            displayUser();
        } else {
            System.out.println("Returning to the main menu...");
        }
    }


    /* ===================================== Methods to Update User ===================================== */
    // Méthode pour mettre à jour un utilisateur
    private void updateUser() {
        System.out.print("Enter the username of the user to update: ");
        String username = scanner.nextLine();

        try {
            // Récupérer l'utilisateur par son nom d'utilisateur
            User user = userService.getUserByUsername(username);

            // Récupérer les anciennes valeurs
            String oldFullName = user.getFullName();
            String oldEmail = user.getEmail();
            String oldPassword = user.getPassword();
            String oldPhoneNumber = user.getPhoneNumber();
            String oldAddress = user.getAddress();

            // Afficher les détails actuels de l'utilisateur
            System.out.println("Current details: ");
            System.out.println(user); // Vous pouvez également afficher ces détails de manière plus personnalisée si besoin.

            // Demander les nouveaux détails
            System.out.print("Enter new full name (leave blank to keep current): ");
            String fullName = scanner.nextLine();
            if (!fullName.isEmpty()) user.setFullName(fullName);

            System.out.print("Enter new email (leave blank to keep current): ");
            String email = scanner.nextLine();
            if (!email.isEmpty()) user.setEmail(email);

            System.out.print("Enter new password (leave blank to keep current): ");
            String password = scanner.nextLine();
            if (!password.isEmpty()) user.setPassword(password);

            System.out.print("Enter new phone number (leave blank to keep current): ");
            String phoneNumber = scanner.nextLine();
            if (!phoneNumber.isEmpty()) user.setPhoneNumber(phoneNumber);

            System.out.print("Enter new address (leave blank to keep current): ");
            String address = scanner.nextLine();
            if (!address.isEmpty()) user.setAddress(address);

            // Mettre à jour l'utilisateur dans le service
            userService.updateUser(user);

            // Créer un format de mise à jour plus lisible pour l'utilisateur
            StringBuilder detailsUpdated = new StringBuilder();
            detailsUpdated.append("\n=================== User Update Summary ===================\n");

            detailsUpdated.append(String.format("| %-20s | %-30s | %-30s |\n", "Field", "Old Value", "New Value"));
            detailsUpdated.append("------------------------------------------------------------\n");

            detailsUpdated.append(String.format("| %-20s | %-30s | %-30s |\n", "Full Name", oldFullName,
                    !fullName.isEmpty() ? fullName : "Unchanged"));
            detailsUpdated.append(String.format("| %-20s | %-30s | %-30s |\n", "Email", oldEmail,
                    !email.isEmpty() ? email : "Unchanged"));
            detailsUpdated.append(String.format("| %-20s | %-30s | %-30s |\n", "Password", oldPassword,
                    !password.isEmpty() ? password : "Unchanged"));
            detailsUpdated.append(String.format("| %-20s | %-30s | %-30s |\n", "Phone", oldPhoneNumber,
                    !phoneNumber.isEmpty() ? phoneNumber : "Unchanged"));
            detailsUpdated.append(String.format("| %-20s | %-30s | %-30s |\n", "Address", oldAddress,
                    !address.isEmpty() ? address : "Unchanged"));

            detailsUpdated.append("------------------------------------------------------------\n");

            // Affichage des informations mises à jour
            System.out.println(detailsUpdated.toString());

            // Enregistrer l'activité de mise à jour de l'utilisateur
            Activity updateUserActivity = new Activity(
                    user.getUsername(),
                    "Update User",
                    "Updated user details. Changes: " + detailsUpdated.toString()
            );
            activityManager.addActivity(updateUserActivity);

            System.out.println("User updated successfully.");

            // Afficher les activités récentes après la mise à jour
            activityManager.displayRecentActivities();

        } catch (UserNotFoundException e) {
            System.out.println("Error: User not found.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    /* ===================================== Methods to Delete User  ===================================== */
    // Méthode pour supprimer un utilisateur
    private void deleteUser() {
        System.out.print("Enter the username of the user to delete: ");
        String username = scanner.nextLine();

        try {
            // Récupérer les détails de l'utilisateur avant suppression pour l'activité
            User user = userService.getUserByUsername(username);

            // Supprimer l'utilisateur
            userService.deleteUser(username);
            System.out.println("User deleted successfully.");

            // Enregistrer l'activité "Delete User"
            Activity deleteUserActivity = new Activity(
                    user.getUsername(),
                    "Delete User",
                    "Deleted user with username: " + username
            );
            activityManager.addActivity(deleteUserActivity);

            // Afficher les activités récentes
            activityManager.displayRecentActivities();

        } catch (UserNotFoundException e) {
            System.out.println("Error: User not found.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /* ===================================== Methods to Display All Users ===================================== */
    // Afficher tous les utilisateurs
    private void displayAllUsers() {

        userService.displayAllUsers();
    }

    /* ===================================== Methods to Display Recent Activities  ===================================== */
    // Affivher les activités récentes
    public void displayRecentActivities() {
        if (activityManager == null) {
            System.out.println("Activity Manager is not available.");
            return;
        }
        activityManager.displayRecentActivities();
    }









}
