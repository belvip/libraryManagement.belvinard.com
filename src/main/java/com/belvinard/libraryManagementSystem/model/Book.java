package com.belvinard.libraryManagementSystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.regex.Pattern;

import static com.belvinard.libraryManagementSystem.console.ConsoleHandler.books;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    private String title;
    private String author;
    private String genre;
    private String ISBN;
    private int publicationYear;

    // Allowed genres for validation (modifiable via configuration in the future)
    private static final Set<String> ALLOWED_GENRES = Set.of(
            "front-end development", "web design", "software development",
            "full-stack development", "" +
                    ""
    );

    public Book(String fieldValue) {
        this.title = fieldValue; // Ce champ est utilisé pour la recherche.
    }



    @Override
    public String toString() {
        return String.format("Book Title --> %s%nBook Author --> %s%nBook Genre --> %s%nBook ISBN --> %s%nBook Publication Year --> %d%n",
                title, author, genre, ISBN, publicationYear);
    }

    // Setters with validation
    /**
     * Sets the genre for this object.
     *
     * @param genre The genre to set. Must not be null and must be one of the allowed genres (case-insensitive).
     *
     * @throws IllegalArgumentException if the genre is null or invalid.
     */
    public void setGenre(String genre) {
        // Check if genre is null and throw an exception if so
        if (genre == null) {
            throw new IllegalArgumentException("Genre cannot be null");
        }

        // Convert the input genre to lowercase for case-insensitive comparison
        String lowercaseGenre = genre.toLowerCase();

        // Check if the lowercase genre exists in the allowed genres (also converted to lowercase)
        if (!ALLOWED_GENRES.stream()
                .map(String::toLowerCase)
                .anyMatch(allowedGenre -> allowedGenre.equals(lowercaseGenre))) {
            throw new IllegalArgumentException(
                    "Invalid genre. It must be one of: " + String.join(", ", ALLOWED_GENRES));
        }

        // If genre is valid, assign it to the genre field
        this.genre = genre;
    }
    /* ============================================================================ */

    public void setTitle(String title) {
        validateNotNullOrEmpty(title, "Title");
        if (title.length() < 3) {
            throw new IllegalArgumentException("Title must be at least 3 characters long.");
        }
        if (!Pattern.matches("^[a-zA-Z0-9\\s]+$", title)) {
            throw new IllegalArgumentException("Title format is invalid. Only letters, numbers, and spaces are allowed.");
        }
        this.title = title;
    }

    public void setAuthor(String author) {
        validateNotNullOrEmpty(author, "Author");
        if (author.length() < 3) {
            throw new IllegalArgumentException("Author name must be at least 3 characters long.");
        }
        if (!Pattern.matches("^[a-zA-Z]+([\\s][a-zA-Z]+)*$", author)) {
            throw new IllegalArgumentException("Author name format is invalid. Only letters and spaces are allowed.");
        }
        this.author = author;
    }

    public void setISBN(String ISBN) {
        if (ISBN == null || !Pattern.matches("\\d{5}", ISBN)) {
            throw new IllegalArgumentException("ISBN must be exactly 5 digits.");
        }
        this.ISBN = ISBN;
    }

    public void setPublicationYear(int publicationYear) {
        int currentYear = java.time.Year.now().getValue();
        if (publicationYear < 1000 || publicationYear > currentYear) {
            throw new IllegalArgumentException("Publication year must be between 1000 and the current year.");
        }
        this.publicationYear = publicationYear;
    }


    // Utility method to check for non-null or empty strings
    private void validateNotNullOrEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty.");
        }
    }

    // Static method to validate if the book is in a valid state
    public static boolean isValid(Book book) {
        if (book == null) return false;
        try {
            book.setTitle(book.getTitle());
            book.setAuthor(book.getAuthor());
            book.setISBN(book.getISBN());
            book.setPublicationYear(book.getPublicationYear());
            book.setGenre(book.getGenre());
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

}
