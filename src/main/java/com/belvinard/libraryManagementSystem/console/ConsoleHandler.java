package com.belvinard.libraryManagementSystem.console;

import com.belvinard.libraryManagementSystem.exception.UserNotFoundException;
import com.belvinard.libraryManagementSystem.model.Book;
import com.belvinard.libraryManagementSystem.model.Loan;
import com.belvinard.libraryManagementSystem.model.User;
import com.belvinard.libraryManagementSystem.service.BookService;
import com.belvinard.libraryManagementSystem.service.UserService;
import com.belvinard.libraryManagementSystem.util.BookSortService;
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
                    displayAllBooks();
                    break;
                case 3:
                    updateBook();
                    break;
                case 4:
                    removeBookByISBN();
                    break;
                case 5:
                    searchBooks();
                    break;
                case 6:
                    sortBooks();
                    break;
                case 7:
                    addUser();  // Ajouter un utilisateur
                    break;
                case 8:
                    displayUser();  // Afficher un utilisateur
                    break;
                case 9:
                    updateUser();  // Mettre à jour un utilisateur
                    break;
                case 10:
                    deleteUser();  // Supprimer un utilisateur
                    break;
                case 11:
                    borrowBook();  // Emprunter un livre
                    break;
                case 12:
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
                "\nPress 1 for Adding Book \n" +
                        "Press 2 for Displaying All Books \n" +
                        "Press 3 for Updating Book \n" +
                        "Press 4 for Removing Book \n" +
                        "Press 5 for Searching Book \n" +
                        "Press 6 for Sorting Book \n" +
                        "Press 7 for Adding User \n" +
                        "Press 8 for Displaying User \n" +
                        "Press 9 for Updating User \n" +
                        "Press 10 for Deleting User \n" +
                        "Press 11 for Borrowing Book \n" +
                        "Press 12 for Exiting the portal\n"
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

        // Vérifiez si le livre existe
        try {
            Book existingBook = bookService.getBookByISBN(isbn);
            System.out.println("\nExisting book details:");
            System.out.println(existingBook);

            System.out.print("Enter new title (leave blank to keep current): ");
            String newTitle = scanner.nextLine();
            System.out.print("Enter new author (leave blank to keep current): ");
            String newAuthor = scanner.nextLine();
            System.out.print("Enter new genre (leave blank to keep current): ");
            String newGenre = scanner.nextLine();
            System.out.print("Enter new publication year (enter 0 to keep current): ");
            int newYear = scanner.nextInt();
            scanner.nextLine(); // Consommer la ligne restante

            // Mettez à jour uniquement les champs modifiés
            if (!newTitle.isEmpty()) existingBook.setTitle(newTitle);
            if (!newAuthor.isEmpty()) existingBook.setAuthor(newAuthor);
            if (!newGenre.isEmpty()) existingBook.setGenre(newGenre);
            if (newYear != 0) existingBook.setPublicationYear(newYear);

            // Appeler la méthode de mise à jour
            bookService.updateBook(existingBook);
            System.out.println("Book updated successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
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

    //*********************** BORROW BOOK
    // Méthode pour emprunter un livre
    private void borrowBook() {
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        // Vérifier si l'utilisateur existe
        User user = userService.getUserByUsername(username);

        if (user == null) {
            System.out.println("User not found. Please create an account first.");
            return;
        }

        // Si l'utilisateur existe, demander ses autres informations
        System.out.print("Enter your full name: ");
        String fullName = scanner.nextLine();
        user.setFullName(fullName);

        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        user.setEmail(email);

        System.out.print("Enter your phone number: ");
        String phoneNumber = scanner.nextLine();
        user.setPhoneNumber(phoneNumber);

        System.out.print("Enter your address: ");
        String address = scanner.nextLine();
        user.setAddress(address);

        // Vérifier si le livre est disponible pour l'emprunt
        System.out.print("Enter the ISBN of the book you want to borrow: ");
        String isbn = scanner.nextLine();

        try {
            Book book = bookService.getBookByISBN(isbn);
            System.out.println("You have chosen to borrow the following book:");
            System.out.println(book);

            // Vérifier si le livre est disponible pour l'emprunt
            if (book.isAvailable()) {
                // Créer un objet Loan pour l'emprunt
                Loan loan = new Loan(book, user, new Date());

                // Ajouter l'emprunt à l'historique de l'utilisateur
                user.addLoan(loan);
                System.out.println("Loan added to your history.");

                // Mettre à jour l'état du livre (marquer le livre comme emprunté)
                book.markAsBorrowed();
                System.out.println("The book has been marked as borrowed.");

                // Mettre à jour l'utilisateur et le livre via les services
                userService.borrowBook(user, book);
                bookService.updateBook(book);

                System.out.println("Book borrowed successfully.");
            } else {
                System.out.println("Sorry, the book is currently unavailable for borrowing.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    // ********************** Gérer les utilisateurs

    // Méthode pour ajouter un utilisateur
    private void addUser() {
        try{
            System.out.println("Enter user details:");

            System.out.print("Username: ");
            String username = scanner.nextLine();

            System.out.print("Full Name: ");
            String fullName = scanner.nextLine();

            System.out.print("Email: ");
            String email = scanner.nextLine();

            System.out.print("PassWord(e.g : MyPass@k48) : ");
            String passWord = scanner.nextLine();

            System.out.print("Phone Number(e.g : +237696190755) : ");
            String phoneNumber = scanner.nextLine();

            System.out.print("Address( e.g : douala) : ");
            String address = scanner.nextLine();

            User newUser = new User();
            newUser.setUsername(username);
            newUser.setFullName(fullName);
            newUser.setEmail(email);
            newUser.setPassword(passWord);
            newUser.setPhoneNumber(phoneNumber);
            newUser.setAddress(address);

            // Ajouter l'utilisateur via le service
            userService.addUser(newUser);
            System.out.println("User added successfully.");
        }catch (IllegalArgumentException e){
            System.out.println("Error : " + e.getMessage());
        }
    }

    // Méthode pour afficher un utilisateur
    private void displayUser() {
        System.out.print("Enter the username to display: ");
        String username = scanner.nextLine();

        try {
            // Tente de récupérer l'utilisateur en fonction du nom d'utilisateur
            User user = userService.getUserByUsername(username);

            // Affichage des détails de l'utilisateur si trouvé
            System.out.println("\nUser details:");
            System.out.println("Username: " + user.getUsername());
            System.out.println("Full Name: " + user.getFullName());
            System.out.println("Email: " + user.getEmail());
            System.out.println("PassWord: " + user.getPassword());
            System.out.println("Phone Number: " + user.getPhoneNumber());
            System.out.println("Address: " + user.getAddress());
            System.out.println("Borrow Limit: " + user.getBorrowLimit());
            System.out.println("Borrowed Books History: ");
            for (Loan loan : user.getBorrowedBooksHistory()) {
                System.out.println(loan);
            }

        } catch (UserNotFoundException e) {
            // Gestion de l'exception spécifique UserNotFoundException
            System.out.println("Error: User with username '" + username + "' not found.");

            // Optionnel : demander à l'utilisateur de réessayer
            System.out.print("Would you like to try again? (y/n): ");
            String choice = scanner.nextLine();
            if (choice.equalsIgnoreCase("y")) {
                displayUser(); // Rappeler la méthode si l'utilisateur veut réessayer
            } else {
                System.out.println("Returning to the main menu...");
            }

        } catch (Exception e) {
            // Gestion des autres exceptions inattendues
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }



    // Méthode pour mettre à jour un utilisateur
    private void updateUser() {
        System.out.print("Enter the username of the user to update: ");
        String username = scanner.nextLine();

        try {
            User user = userService.getUserByUsername(username);
            System.out.println("Current details: " + user);

            System.out.print("Enter new full name (leave blank to keep current): ");
            String fullName = scanner.nextLine();
            if (!fullName.isEmpty()) user.setFullName(fullName);

            System.out.print("Enter new email (leave blank to keep current): ");
            String email = scanner.nextLine();
            if (!email.isEmpty()) user.setEmail(email);

            System.out.print("Enter new phone number (leave blank to keep current): ");
            String phoneNumber = scanner.nextLine();
            if (!phoneNumber.isEmpty()) user.setPhoneNumber(phoneNumber);

            System.out.print("Enter new address (leave blank to keep current): ");
            String address = scanner.nextLine();
            if (!address.isEmpty()) user.setAddress(address);

            userService.updateUser(user);
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






}