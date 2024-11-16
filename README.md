### Skills Learned in the First Stage for Adding a Book

- 🔹 **Basic Java Object Creation**: Learned to create Java objects with attributes, including a constructor and parameterized constructors.
- 🔹 **Input Validation**: Developed custom setters with validation checks for attributes like `title`, `author`, `ISBN`, `genre`, and `publicationYear`.
- 🔹 **Exception Handling**: Implemented `IllegalArgumentException` for input errors, including meaningful error messages for each case.
- 🔹 **Using Regular Expressions**: Leveraged regular expressions (`Pattern.matches`) to validate text inputs, such as titles, authors, and ISBNs.
- 🔹 **Implementing Business Rules**: Enforced business rules (e.g., disallow duplicate ISBNs, validate specific genres).
- 🔹 **Console-Based User Interaction**: Built a simple, user-friendly console interface with input prompts and feedback messages.
- 🔹 **Data Storage with Collections**: Utilized `ArrayList` to store book objects in memory.
- 🔹 **Formatted Output with `toString()`**: Customized the `toString()` method to display book details in a readable format.
- 🔹 **Looping and Conditionals for Menu Navigation**: Used `while` loops and `switch` cases to manage user choices and keep the application running until the user exits.
- 🔹 **Error Handling for Duplicate Entries**: Created logic to check for existing ISBNs before adding a book and display appropriate error messages.

---
# Skills Learned During the "Display All Books" Stage 📚

## General Programming Skills
- ✅ **Creating and Using Getter Methods**: Learned to expose private fields safely using public getter methods.
- ✅ **Iterating Over Collections**: Practiced iterating over a list of objects (`List<Book>`) to display each item.
- ✅ **Testing Application Features**: Verified the functionality of methods through manual testing and debugging.

## Java-Specific Skills
- 🛠 **Working with Java Collections**: Used `ArrayList` and `List` to store and retrieve objects.
- 🛠 **Implementing Methods in Classes**: Added specific methods in the `LibraryData` and `BookService` classes to support functionality.
- 🛠 **Encapsulation and Data Access**: Applied the principle of encapsulation to protect data and expose it through well-defined interfaces.

## Spring Framework Skills
- 🌱 **Defining Components**: Utilized `@Component` to define Spring-managed beans for dependency injection.
- 🌱 **Managing Dependencies**: Connected `LibraryData` and `BookService` components in a structured manner.

## Debugging and Problem-Solving Skills
- 🐛 **Resolving "Cannot Find Symbol" Errors**: Identified and fixed missing method definitions by adding the required getter.
- 🐛 **Refining Logic**: Adjusted the code to correctly retrieve and display stored books.
- 🐛 **Testing Edge Cases**: Ensured that no duplicate books with the same ISBN were displayed.

## Console-Based UI Skills
- 💻 **Designing User Menus**: Created a user-friendly console menu to display book options.
- 💻 **Output Formatting**: Enhanced readability of book details using `toString()` formatting in the `Book` class.

## Object-Oriented Programming (OOP) Concepts
- 📦 **Separation of Concerns**: Separated data handling, business logic, and presentation layers into distinct classes.
- 📦 **Reusable Methods**: Designed a reusable `displayAllBooks` method in `ConsoleHandler`.

## Communication and Validation
- 📢 **Meaningful Output Messages**: Displayed clear messages for users, such as success notifications and book details.
- 📢 **Edge Case Handling**: Ensured that the application gracefully handled invalid data.

---

# Skills Learned During the "Update Book" Stage 📚

## 🛠️ Technical Skills
- **Java Method Design**:
  - Creating methods like `updateBook` and `getBookByISBN` to manage specific functionality.
- **Java Streams**:
  - Using streams to filter and find specific data within a collection.
  - Employing methods like `filter()`, `findFirst()`, and `orElse()`.
- **Exception Handling**:
  - Throwing and handling `IllegalArgumentException` for invalid or not-found cases.
- **Looping Through Collections**:
  - Iterating through lists to find and replace specific elements.
- **Setters with Validation**:
  - Using setter methods in the `Book` class to ensure updates adhere to business rules.

## 🖥️ Practical Console UI Skills
- **User Interaction**:
  - Prompting the user for input to choose and specify what to update.
- **Input Validation**:
  - Verifying the correctness of user input and providing feedback for invalid entries.
- **Update Options**:
  - Implementing a flexible menu-driven system to allow updates for individual book attributes.

## 🧹 Code Quality and Maintenance
- **Clean Code Practices**:
  - Structuring code for readability and reusability.
  - Adding meaningful comments for better understanding.
- **Flag Implementation**:
  - Using flags like `isUpdated` to track if changes were successfully made.
- **Error Messaging**:
  - Customizing error messages to provide clear and actionable feedback.

## 🧠 Conceptual Knowledge
- **Object-Oriented Programming (OOP)**:
  - Leveraging encapsulation to modify specific attributes of a book.
- **Business Logic Separation**:
  - Delegating update operations to the service layer (`BookService`) while keeping the console layer focused on user interaction.

## 💡 Problem-Solving Strategies
- **Handling Edge Cases**:
  - Accounting for invalid or no input scenarios during updates.
  - Ensuring that update operations do not proceed for invalid data.

By mastering these skills, you've built a robust and user-friendly update functionality in your library management system! 🎉


---

## Skills Learned in Implementing Book Search Functionality 📚🔍

### 1. **Searching by Different Attributes** 🏷️
   - Ability to implement search functionality by different book attributes:
     - **Title** 🏷️
     - **Author** ✍️
     - **ISBN** 📘

### 2. **Implementing Linear Search** 🔄
   - Understanding how to implement **linear search** to find books based on a search query.
   - **Linear search** checks every book sequentially to match the search criteria.
   
### 3. **Implementing Binary Search** 🔢
   - Learning to apply **binary search** for faster search through sorted book data.
   - **Binary search** splits the data in half to reduce the search space efficiently.

### 4. **Handling Case Insensitivity** 🔠
   - Implementing **case-insensitive** searches to ensure the search is not affected by upper/lowercase differences.

### 5. **Sorting Data for Binary Search** 📊
   - Sorting book data before applying binary search based on the selected attribute (e.g., title, author, or ISBN).
   - Understanding the importance of sorted data for binary search efficiency.

### 6. **Input Validation** 📝
   - Validating user input for **search type** (title, author, ISBN) and **search query**.
   - Ensuring that invalid inputs are handled properly to avoid errors.

### 7. **Displaying Search Results** 💻
   - Showing results to users with proper formatting for clarity.
   - Informing users when no books match the search query.

### 8. **Improving User Interface** 🖥️
   - Enhancing the user interface to allow users to choose between **linear search** and **binary search**.
   - Making the system more interactive and user-friendly by providing clear options.

### 9. **Debugging Search Functionality** 🐞
   - Troubleshooting issues with search results and refining the logic to ensure accuracy.
   - Handling edge cases, such as empty search results or invalid search criteria.

---

🔧 **Tools & Techniques Used:**
   - **Java Collections** (List, ArrayList) 🗂️
   - **Sorting Algorithms** 🧑‍💻
   - **String Matching** and **Comparison** 🔍
   - **Conditional Statements** for flow control 🛠️

---

## Skills Learned in Implementing Sorting Functionality 📝

1. **Understanding Sorting Algorithms 🔄**
   - **Bubble Sort**: A simple comparison-based sorting algorithm where adjacent elements are swapped if they are in the wrong order. Used for sorting books by title, author, or publication year.
   - **Selection Sort**: Selects the smallest or largest element in the unsorted part and swaps it with the element at the correct position. Used for sorting books by title, author, or publication year.
   - **QuickSort**: A divide-and-conquer algorithm that selects a pivot and recursively sorts the elements into two sublists. Efficient for sorting books by title, author, or publication year.

2. **Implementing Sorting by Multiple Criteria 📖**
   - Sorting books by **Title**: Implemented sorting algorithms to organize books alphabetically by title.
   - Sorting books by **Author**: Implemented sorting algorithms to organize books alphabetically by author.
   - Sorting books by **Publication Year**: Implemented sorting algorithms to organize books chronologically by publication year.

3. **Understanding Algorithm Complexity ⏳**
   - Bubble Sort and Selection Sort have **O(n^2)** time complexity, making them inefficient for large lists.
   - QuickSort, with an average case of **O(n log n)**, is more efficient for larger datasets.

4. **Implementing Custom Sorting Methods ⚙️**
   - You learned how to implement sorting methods within a `SortBooks` class, organizing code to keep sorting logic separate from other business logic in the application.

5. **Interacting with Lists in Java 📋**
   - You worked with Java `List<Book>` objects and used `get()`, `set()`, and other list operations to implement the sorting functionality.
   
6. **Handling User Inputs for Sorting 📲**
   - Enabled users to choose sorting criteria (title, author, publication year) and select the sorting algorithm to be applied.

7. **Improving User Experience 🎮**
   - Added flexibility in how the user interacts with the system, providing different sorting options and improving the overall functionality of the library system.

8. **Testing Sorting Mechanisms 🧪**
   - Tested different sorting methods to ensure correctness and performance, ensuring that the books were being sorted as expected.




