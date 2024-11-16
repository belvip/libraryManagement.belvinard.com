package com.belvinard.libraryManagementSystem.util;

import com.belvinard.libraryManagementSystem.model.Book;
import java.util.List;
import java.util.Scanner;

public class BookSortService {
    private List<Book> books;

    // Constructeur qui prend une liste de livres
    public BookSortService(List<Book> books) {

    }

    // Method to display sorting criteria options
    public static void showSortingCriteria() {
        System.out.println("Please choose a sorting criterion:");
        System.out.println("1. Sort by Title");
        System.out.println("2. Sort by Author");
        System.out.println("3. Sort by Publication Year");
        System.out.println("4. Sort by ISBN");
        System.out.println("5. Sort by Genre");
    }

    // Method to display sorting algorithm options
    public static void showSortingAlgorithms() {
        System.out.println("Please choose a sorting algorithm:");
        System.out.println("1. Bubble Sort");
        System.out.println("2. Selection Sort");
        System.out.println("3. Quick Sort");
    }

    // Method to apply the sorting based on user choice
    public static void applySorting(List<Book> books) {
        Scanner scanner = new Scanner(System.in);

        // Choose sorting criterion
        showSortingCriteria();
        int criteriaChoice = scanner.nextInt();

        // Choose sorting algorithm
        showSortingAlgorithms();
        int algorithmChoice = scanner.nextInt();

        // Apply the sorting based on the user's choices
        switch (criteriaChoice) {
            case 1: // Sort by Title
                sortBooksByTitle(books, algorithmChoice);
                break;
            case 2: // Sort by Author
                sortBooksByAuthor(books, algorithmChoice);
                break;
            case 3: // Sort by Publication Year
                sortBooksByYear(books, algorithmChoice);
                break;
            case 4: // Sort by ISBN
                sortBooksByIsbn(books, algorithmChoice);
                break;
            case 5: // Sort by Genre
                sortBooksByGenre(books, algorithmChoice);
                break;
            default:
                System.out.println("Invalid sorting criterion.");
                return;
        }

        // Display sorted books
        printBooks(books);
    }

    // Method to sort books by title
    private static void sortBooksByTitle(List<Book> books, int algorithmChoice) {
        switch (algorithmChoice) {
            case 1:
                bubbleSortByTitle(books);
                break;
            case 2:
                selectionSortByTitle(books);
                break;
            case 3:
                quickSortByTitle(books, 0, books.size() - 1);
                break;
            default:
                System.out.println("Invalid sorting algorithm.");
        }
    }

    // Method to sort books by author
    private static void sortBooksByAuthor(List<Book> books, int algorithmChoice) {
        switch (algorithmChoice) {
            case 1:
                bubbleSortByAuthor(books);
                break;
            case 2:
                selectionSortByAuthor(books);
                break;
            case 3:
                quickSortByAuthor(books, 0, books.size() - 1);
                break;
            default:
                System.out.println("Invalid sorting algorithm.");
        }
    }

    // Method to sort books by publication year
    private static void sortBooksByYear(List<Book> books, int algorithmChoice) {
        switch (algorithmChoice) {
            case 1:
                bubbleSortByYear(books);
                break;
            case 2:
                selectionSortByYear(books);
                break;
            case 3:
                quickSortByYear(books, 0, books.size() - 1);
                break;
            default:
                System.out.println("Invalid sorting algorithm.");
        }
    }

    // Method to sort books by ISBN
    private static void sortBooksByIsbn(List<Book> books, int algorithmChoice) {
        switch (algorithmChoice) {
            case 1:
                bubbleSortByIsbn(books);
                break;
            case 2:
                selectionSortByIsbn(books);
                break;
            case 3:
                quickSortByIsbn(books, 0, books.size() - 1);
                break;
            default:
                System.out.println("Invalid sorting algorithm.");
        }
    }

    // Method to sort books by genre
    private static void sortBooksByGenre(List<Book> books, int algorithmChoice) {
        switch (algorithmChoice) {
            case 1:
                bubbleSortByGenre(books);
                break;
            case 2:
                selectionSortByGenre(books);
                break;
            case 3:
                quickSortByGenre(books, 0, books.size() - 1);
                break;
            default:
                System.out.println("Invalid sorting algorithm.");
        }
    }

    // Method to display sorted books
    private static void printBooks(List<Book> books) {
        for (Book book : books) {
            System.out.println(book); // Make sure the Book class has a properly defined toString() method
        }
    }

    // Sorting algorithms (Bubble Sort, Selection Sort, Quick Sort)

    // Bubble Sort for title
    private static void bubbleSortByTitle(List<Book> books) {
        for (int i = 0; i < books.size() - 1; i++) {
            for (int j = 0; j < books.size() - 1 - i; j++) {
                if (books.get(j).getTitle().compareTo(books.get(j + 1).getTitle()) > 0) {
                    Book temp = books.get(j);
                    books.set(j, books.get(j + 1));
                    books.set(j + 1, temp);
                }
            }
        }
    }

    // Bubble Sort for author
    private static void bubbleSortByAuthor(List<Book> books) {
        for (int i = 0; i < books.size() - 1; i++) {
            for (int j = 0; j < books.size() - 1 - i; j++) {
                if (books.get(j).getAuthor().compareTo(books.get(j + 1).getAuthor()) > 0) {
                    Book temp = books.get(j);
                    books.set(j, books.get(j + 1));
                    books.set(j + 1, temp);
                }
            }
        }
    }

    // Bubble Sort for publication year
    private static void bubbleSortByYear(List<Book> books) {
        for (int i = 0; i < books.size() - 1; i++) {
            for (int j = 0; j < books.size() - 1 - i; j++) {
                if (books.get(j).getPublicationYear() > books.get(j + 1).getPublicationYear()) {
                    Book temp = books.get(j);
                    books.set(j, books.get(j + 1));
                    books.set(j + 1, temp);
                }
            }
        }
    }

    // Bubble Sort for ISBN
    private static void bubbleSortByIsbn(List<Book> books) {
        for (int i = 0; i < books.size() - 1; i++) {
            for (int j = 0; j < books.size() - 1 - i; j++) {
                if (books.get(j).getISBN().compareTo(books.get(j + 1).getISBN()) > 0) {
                    Book temp = books.get(j);
                    books.set(j, books.get(j + 1));
                    books.set(j + 1, temp);
                }
            }
        }
    }

    // Bubble Sort for genre
    private static void bubbleSortByGenre(List<Book> books) {
        for (int i = 0; i < books.size() - 1; i++) {
            for (int j = 0; j < books.size() - 1 - i; j++) {
                if (books.get(j).getGenre().compareTo(books.get(j + 1).getGenre()) > 0) {
                    Book temp = books.get(j);
                    books.set(j, books.get(j + 1));
                    books.set(j + 1, temp);
                }
            }
        }
    }

    // Selection Sort for title
    private static void selectionSortByTitle(List<Book> books) {
        int n = books.size();
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (books.get(j).getTitle().compareTo(books.get(minIdx).getTitle()) < 0) {
                    minIdx = j;
                }
            }
            Book temp = books.get(i);
            books.set(i, books.get(minIdx));
            books.set(minIdx, temp);
        }
    }

    // Selection Sort for author
    private static void selectionSortByAuthor(List<Book> books) {
        int n = books.size();
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (books.get(j).getAuthor().compareTo(books.get(minIdx).getAuthor()) < 0) {
                    minIdx = j;
                }
            }
            Book temp = books.get(i);
            books.set(i, books.get(minIdx));
            books.set(minIdx, temp);
        }
    }

    // Selection Sort for publication year
    private static void selectionSortByYear(List<Book> books) {
        int n = books.size();
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (books.get(j).getPublicationYear() < books.get(minIdx).getPublicationYear()) {
                    minIdx = j;
                }
            }
            Book temp = books.get(i);
            books.set(i, books.get(minIdx));
            books.set(minIdx, temp);
        }
    }

    // Selection Sort for ISBN
    private static void selectionSortByIsbn(List<Book> books) {
        int n = books.size();
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (books.get(j).getISBN().compareTo(books.get(minIdx).getISBN()) < 0) {
                    minIdx = j;
                }
            }
            Book temp = books.get(i);
            books.set(i, books.get(minIdx));
            books.set(minIdx, temp);
        }
    }

    // Quick Sort for title
    private static void quickSortByTitle(List<Book> books, int low, int high) {
        if (low < high) {
            int pi = partitionByTitle(books, low, high);
            quickSortByTitle(books, low, pi - 1);
            quickSortByTitle(books, pi + 1, high);
        }
    }

    private static int partitionByTitle(List<Book> books, int low, int high) {
        Book pivot = books.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (books.get(j).getTitle().compareTo(pivot.getTitle()) < 0) {
                i++;
                Book temp = books.get(i);
                books.set(i, books.get(j));
                books.set(j, temp);
            }
        }
        Book temp = books.get(i + 1);
        books.set(i + 1, books.get(high));
        books.set(high, temp);
        return i + 1;
    }

    // Quick Sort for author
    private static void quickSortByAuthor(List<Book> books, int low, int high) {
        if (low < high) {
            int pi = partitionByAuthor(books, low, high);
            quickSortByAuthor(books, low, pi - 1);
            quickSortByAuthor(books, pi + 1, high);
        }
    }

    private static int partitionByAuthor(List<Book> books, int low, int high) {
        Book pivot = books.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (books.get(j).getAuthor().compareTo(pivot.getAuthor()) < 0) {
                i++;
                Book temp = books.get(i);
                books.set(i, books.get(j));
                books.set(j, temp);
            }
        }
        Book temp = books.get(i + 1);
        books.set(i + 1, books.get(high));
        books.set(high, temp);
        return i + 1;
    }

    // Quick Sort for publication year
    private static void quickSortByYear(List<Book> books, int low, int high) {
        if (low < high) {
            int pi = partitionByYear(books, low, high); // Partition en fonction de l'année
            quickSortByYear(books, low, pi - 1);
            quickSortByYear(books, pi + 1, high);
        }
    }

    private static int partitionByYear(List<Book> books, int low, int high) {
        Book pivot = books.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (books.get(j).getPublicationYear() < pivot.getPublicationYear()) {  // Comparaison par année
                i++;
                Book temp = books.get(i);
                books.set(i, books.get(j));
                books.set(j, temp);
            }
        }
        Book temp = books.get(i + 1);
        books.set(i + 1, books.get(high));
        books.set(high, temp);
        return i + 1;
    }

    // Quick Sort for ISBN
    private static void quickSortByIsbn(List<Book> books, int low, int high) {
        if (low < high) {
            int pi = partitionByIsbn(books, low, high); // Partition en fonction de l'ISBN
            quickSortByIsbn(books, low, pi - 1);
            quickSortByIsbn(books, pi + 1, high);
        }
    }

    private static int partitionByIsbn(List<Book> books, int low, int high) {
        Book pivot = books.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (books.get(j).getISBN().compareTo(pivot.getISBN()) < 0) {  // Comparaison par ISBN
                i++;
                Book temp = books.get(i);
                books.set(i, books.get(j));
                books.set(j, temp);
            }
        }
        Book temp = books.get(i + 1);
        books.set(i + 1, books.get(high));
        books.set(high, temp);
        return i + 1;
    }

    // Selection Sort for genre
    private static void selectionSortByGenre(List<Book> books) {
        int n = books.size();
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (books.get(j).getGenre().compareTo(books.get(minIdx).getGenre()) < 0) {  // Comparaison par genre
                    minIdx = j;
                }
            }
            Book temp = books.get(i);
            books.set(i, books.get(minIdx));
            books.set(minIdx, temp);
        }
    }

    // Quick Sort for genre
    private static void quickSortByGenre(List<Book> books, int low, int high) {
        if (low < high) {
            int pi = partitionByGenre(books, low, high);  // Partition en fonction du genre
            quickSortByGenre(books, low, pi - 1);
            quickSortByGenre(books, pi + 1, high);
        }
    }

    private static int partitionByGenre(List<Book> books, int low, int high) {
        Book pivot = books.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (books.get(j).getGenre().compareTo(pivot.getGenre()) < 0) {  // Comparaison par genre
                i++;
                Book temp = books.get(i);
                books.set(i, books.get(j));
                books.set(j, temp);
            }
        }
        Book temp = books.get(i + 1);
        books.set(i + 1, books.get(high));
        books.set(high, temp);
        return i + 1;
    }



}
