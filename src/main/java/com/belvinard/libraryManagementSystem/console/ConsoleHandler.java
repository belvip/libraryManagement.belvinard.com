

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

    private void displayMenu() {
        System.out.println("\n----- The Library Management System Portal -----");
        System.out.println(
                "\nPress 1 for Logging in before adding Book \n" +
                        "Press 2 for Adding a Book \n" +
                        "Press 3 for Displaying All Books \n" +
                        "Press 4 for Updating Book \n" +
                        "Press 5 for Removing Book \n" +
                        "Press 6 for Searching Book \n" +
                        "Press 7 for Sorting Book \n" +
                        "Press 8 for Adding User \n" +
                        "Press 9 for Displaying User \n" +
                        "Press 10 for Updating Users \n" +
                        "Press 11 for Displaying All Users \n" +
                        "Press 12 for Deleting User \n" +
                        "Press 13 for Borrowing Book \n" +
                        "Press 14 for Returning Book \n" +
                        "Press 15 for Displaying Resents Activities \n" +
                        "Press 16 for Exiting the portal\n"
        );
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
            System.out.println("\n================= List of Books =================");
            System.out.printf("%-15s %-30s %-20s %-15s %-10s %-10s %n",
                    "ISBN", "Title", "Author", "Genre", "Available", "Copies");
            System.out.println("---------------------------------------------------------------------------------------");
            for (int i = startIndex; i < endIndex; i++) {
                Book book = books.get(i);
                System.out.printf("%-15s %-30s %-20s %-15s %-10s %-10d %n",
                        book.getISBN(), book.getTitle(), book.getAuthor(), book.getGenre(),
                        book.isAvailable() ? "Yes" : "No", book.getNumberOfCopies());
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
        System.out.print("Enter the ISBN of the book to update: ");
        String isbn = scanner.nextLine();

        try {
            // Récupérer le livre existant via BookService
            Book existingBook = bookService.getBookByISBN(isbn);

            System.out.println("\nExisting book details:");
            System.out.println(existingBook);

            // Demander les nouvelles valeurs à l'utilisateur
            System.out.print("Enter new title (leave blank to keep current): ");
            String newTitle = scanner.nextLine().trim();
            System.out.print("Enter new author (leave blank to keep current): ");
            String newAuthor = scanner.nextLine().trim();
            System.out.print("Enter new genre (leave blank to keep current): ");
            String newGenre = scanner.nextLine().trim();
            System.out.print("Enter new number of copies (enter 0 to keep current): ");
            int newNumberOfCopies = scanner.nextInt();
            System.out.print("Enter new publication year (enter 0 to keep current): ");
            int newYear = scanner.nextInt();
            scanner.nextLine(); // Consommer la ligne restante

            // Mettre à jour uniquement les champs modifiés
            if (!newTitle.isEmpty()) existingBook.setTitle(newTitle);
            if (!newAuthor.isEmpty()) existingBook.setAuthor(newAuthor);
            if (!newGenre.isEmpty()) existingBook.setGenre(newGenre);
            if (newNumberOfCopies > 0) existingBook.setNumberOfCopies(newNumberOfCopies);
            if (newYear > 0) existingBook.setPublicationYear(newYear);

            // Appeler la mise à jour via BookService
            bookService.updateBook(existingBook);

            System.out.println("Book updated successfully.");
        } catch (BookNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    // ***************************** BORROW BOOK
    // Méthode pour emprunter un livre
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
            System.out.println("Book borrowed successfully.");
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
        } catch (Exception e) {
            System.err.println("Error occurred while returning the book: " + e.getMessage());
        }
    }



    /* ================================================ Method to remove books =================================== */

    private void removeBookByISBN() {
        System.out.print("Enter the ISBN of the book to remove: ");
        String isbn = scanner.nextLine();

        try {
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
            } else {
                System.out.println("The book was not removed.");
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /* ================================================ Methods to search books =================================== */

    private void searchBooks() {
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        User user;
        try {
            user = userService.getUserByUsername(username);
        } catch (UserNotFoundException e) {
            System.out.println("User not found. Please try again.");
            return;
        }

        System.out.println("Choose search type:");
        System.out.println("1. Linear Search");
        System.out.println("2. Binary Search");
        System.out.print("Enter your choice: ");
        int searchType = scanner.nextInt();
        scanner.nextLine(); // Consommer la ligne restante

        System.out.println("Search by:");
        System.out.println("1. Title");
        System.out.println("2. Author");
        System.out.println("3. Genre");
        System.out.println("4. ISBN");
        System.out.print("Enter your choice: ");
        int searchFieldChoice = scanner.nextInt();
        scanner.nextLine(); // Consommer la ligne restante

        String searchField;
        switch (searchFieldChoice) {
            case 1:
                searchField = "title";
                break;
            case 2:
                searchField = "author";
                break;
            case 3:
                searchField = "genre";
                break;
            case 4:
                searchField = "isbn";
                break;
            default:
                System.out.println("Invalid choice. Returning to menu.");
                return;
        }

        System.out.print("Enter the search query: ");
        String query = scanner.nextLine();

        // Enregistrer l'activité "Search Book" avec le champ de recherche et la requête
        Activity searchBookActivity = new Activity(
                user.getUsername(),
                "Search Book",
                "Search Field: " + searchField + ", Query: " + query
        );
        activityManager.addActivity(searchBookActivity);

        List<Book> result;

        if (searchType == 1) {
            result = bookService.linearSearchBooks(query, searchField);
        } else if (searchType == 2) {
            result = bookService.binarySearchBooks(query, searchField);
        } else {
            System.out.println("Invalid search type. Returning to menu.");
            return;
        }

        if (result.isEmpty()) {
            System.out.println("No books found matching the query.");
        } else {
            System.out.println("\nSearch Results:");
            for (Book book : result) {
                System.out.println(book);
            }
        }
    }

    /* ================================================ Methods to sort books =================================== */

    private void sortBooks() {
        // Choisir le critère de tri
        System.out.println("\nSelect sorting criterion:");
        System.out.println("1. Sort by Title");
        System.out.println("2. Sort by Author");
        System.out.println("3. Sort by Year");
        System.out.println("4. Sort by Genre");
        System.out.print("Enter your choice: ");
        int criterionChoice = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        // Déterminer le champ de tri
        String sortBy = "";
        switch (criterionChoice) {
            case 1:
                sortBy = "title";
                break;
            case 2:
                sortBy = "author";
                break;
            case 3:
                sortBy = "year";
                break;
            case 4:
                sortBy = "genre";
                break;
            default:
                System.out.println("Invalid choice. Defaulting to Title.");
                sortBy = "title";
                break;
        }

        // Choisir l'algorithme de tri
        System.out.println("\nSelect sorting algorithm:");
        System.out.println("1. Bubble Sort");
        System.out.println("2. Selection Sort");
        System.out.println("3. QuickSort");
        System.out.print("Enter your choice: ");
        int algorithmChoice = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        // Récupérer les livres depuis le service
        List<Book> books = new ArrayList<>(bookService.getAllBooks());

        // Appliquer l'algorithme de tri sélectionné
        switch (algorithmChoice) {
            case 1:
                bookService.bubbleSort(books, sortBy); // Bubble Sort
                break;
            case 2:
                bookService.selectionSort(books, sortBy); // Selection Sort
                break;
            case 3:
                bookService.quickSort(books, 0, books.size() - 1, sortBy); // Quick Sort
                break;
            default:
                System.out.println("Invalid choice. Defaulting to Bubble Sort.");
                bookService.bubbleSort(books, sortBy); // Bubble Sort par défaut
                break;
        }

        // Afficher les livres triés
        System.out.println("\nBooks sorted by " + sortBy + ":");
        for (Book book : books) {
            System.out.println(book);
            System.out.println("----------------------------------------");
        }

        // Afficher le nombre total de livres triés
        System.out.println("Total number of books sorted: " + books.size());
    }


    private Date calculateReturnDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 14); // Exemple : Le retour doit se faire dans 14 jours
        return calendar.getTime();
    }


    // ********************** Gérer les utilisateurs

    // Méthode pour ajouter un utilisateur
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

            System.out.print("Phone Number (e.g., +237696190755): ");
            String phoneNumber = scanner.nextLine();

            System.out.print("Address (e.g., Douala): ");
            String address = scanner.nextLine();

            // Créer un utilisateur avec les détails saisis
            User newUser = new User(username, password); // Constructeur initialisant le borrowLimit
            newUser.setFullName(fullName);
            newUser.setEmail(email);
            newUser.setPhoneNumber(phoneNumber);
            newUser.setAddress(address);

            System.out.println("Setting default borrow limit to: " + newUser.getBorrowLimit()); // Débogage

            // Ajouter l'utilisateur via le service
            userService.addUser(newUser);

            System.out.println("User added successfully with borrow limit: " + newUser.getBorrowLimit());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }


    // Méthode pour afficher un utilisateur
    private void displayUser() {
        System.out.print("Enter the username to display: ");
        String username = scanner.nextLine();

        try {
            // Récupérer l'utilisateur
            User user = userService.getUserByUsername(username);

            // Afficher les détails de l'utilisateur
            System.out.println("\nUser details:");
            System.out.println("Username: " + user.getUsername());
            System.out.println("Full Name: " + user.getFullName());
            System.out.println("Email: " + user.getEmail());
            System.out.println("PassWord: " + user.getPassword());
            System.out.println("Phone Number: " + user.getPhoneNumber());
            System.out.println("Address: " + user.getAddress());
            System.out.println("Borrow Limit: " + user.getBorrowLimit());
            System.out.println("Borrowed Books History: ");

            // Mettre à jour dynamiquement les statuts des prêts avant de les afficher
            List<Loan> borrowedBooksHistory = user.getBorrowedBooksHistory();
            if (borrowedBooksHistory.isEmpty()) {
                System.out.println("No books borrowed.");
            } else {
                for (Loan loan : borrowedBooksHistory) {
                    loan.updateReturnStatus(); // Mise à jour du statut du prêt
                    System.out.println(loan);
                }
            }
        } catch (UserNotFoundException e) {
            System.out.println("Error: User with username '" + username + "' not found.");
            promptRetryDisplayUser();
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    // Méthode auxiliaire pour demander si l'utilisateur veut réessayer
    private void promptRetryDisplayUser() {
        System.out.print("Would you like to try again? (y/n): ");
        String choice = scanner.nextLine();
        if (choice.equalsIgnoreCase("y")) {
            displayUser();
        } else {
            System.out.println("Returning to the main menu...");
        }
    }


    // Méthode pour mettre à jour un utilisateur
    private void updateUser() {
        System.out.print("Enter the username of the user to update: ");
        String username = scanner.nextLine();

        try {
            // Récupérer l'utilisateur par son nom d'utilisateur
            User user = userService.getUserByUsername(username);
            System.out.println("Current details: " + user);

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

            // Enregistrer l'activité "Update User" après la mise à jour
            Activity updateUserActivity = new Activity(
                    user.getUsername(),
                    "Update User",
                    "Updated information for " + user.getUsername()
            );
            activityManager.addActivity(updateUserActivity);

            System.out.println("User updated successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    // Méthode pour supprimer un utilisateur
    private void deleteUser() {
        System.out.print("Enter the username of the user to delete: ");
        String username = scanner.nextLine();

        try {
            userService.deleteUser(username);
            System.out.println("User deleted successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Afficher tous les utilisateurs
    private void displayAllUsers() {

        userService.displayAllUsers();
    }

    // Affivher les activités récentes
    public void displayRecentActivities() {
        if (activityManager == null) {
            System.out.println("Activity Manager is not available.");
            return;
        }
        activityManager.displayRecentActivities();
    }









}