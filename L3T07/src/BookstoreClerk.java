import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

// This program will be used by a bookstore clerk to view, update and delete books in a database.
public class BookstoreClerk {

    public static void main(String[] args) {

        // When the program starts, all rows in the table books are printed.
        try{
            // Connection to the ebookstore database, via the jdbc:mysql: channel on localhost (this PC)
            // Use username "clerk", password "bookstore".
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/ebookstore?useSSL=false",
                    "clerk",
                    "bookstore"
            );

            Statement statement = connection.createStatement();

            // All rows and columns are selected in this ResultSet.
            ResultSet allRows = statement.executeQuery("SELECT * FROM books");

            // The printRows() function is used here to print all rows of the books table.
            printRows(allRows);

            allRows.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("Problems connecting to the database.");
            e.printStackTrace();
        }

        // The while loop presents the user with a menu.
        String userChoice = "";
        while (!userChoice.equals("0")) {
            System.out.println("""
                        Select an option:
                        1. Enter book
                        2. Update book
                        3. Delete book
                        4. Search book
                        0. Exit
                        """);

            Scanner userInput = new Scanner(System.in);

            // As the user will need to input data, a try-catch block is implemented in case of an input mismatch.
            try{

                userChoice = userInput.nextLine();

                // The chooseInput() method is used to identify the user's choice and run the appropriate code. It passes the user's input and the Scanner as parameters.
                chooseInput(userChoice, userInput);

            }catch (Exception e){
                // If the user inputs any invalid data, this code is run and the while statement repeats.
                System.out.println("Invalid Input. Try again.");
            }
        }
    }

    // This method prints all the rows from a given ResultSet.
    public static void printRows(ResultSet bookRows) throws SQLException{

        while (bookRows.next()) {
            System.out.println(
                    bookRows.getInt("id") + ", "
                            + bookRows.getString("title") + ", "
                            + bookRows.getString("author") + ", "
                            + bookRows.getString("genre") + ", "
                            + bookRows.getInt("qty")
            );
        }
    }

    //This method is used to identify the user's choice and run the appropriate code. It passes the user's input and the Scanner as parameters.
    public static void chooseInput(String userChoice, Scanner userInput) {

        // An enhanced switch statement is used to determine the menu item the user has selected and run the appropriate code.
        switch (userChoice) {
            // If the user inputs '1', then the user inputs the details of the new book and the addBook() function is run.
            case "1" -> {

                System.out.println("Title of the book: ");
                String newBookTitle = userInput.nextLine();

                System.out.println("Author of the book: ");
                String newBookAuthor = userInput.nextLine();

                System.out.println("Genre of the book: ");
                String newBookGenre = userInput.nextLine();

                System.out.println("Quantity of books to order: ");
                int newBookQuantity = userInput.nextInt();

                // The below lines replace all ' in the input with '' - this is to prevent a SQL syntax error from occurring.
                newBookTitle = newBookTitle.replace("'", "''");
                newBookAuthor = newBookAuthor.replace("'", "''");
                newBookGenre = newBookGenre.replace("'", "''");

                // The addBook() method takes the new elements as its parameters.
                addBook(newBookTitle, newBookAuthor, newBookGenre, newBookQuantity);

                System.out.println("New book added!");

            }

            /*
            If the user inputs '2', then the user is able to search for a book by its ID, Title, Author or Genre.
            The user is then able to update the appropriate row in the table.
             */
            case "2" -> {

                System.out.println("Search book to update by ID, Title, Author or Genre: ");
                String bookToSearch = userInput.nextLine();

                // This replacement of ' by '' prevents SQL syntax errors.
                bookToSearch = bookToSearch.replace("'", "''");

                // The searchBooks() method is used inside a new ArrayList that stores the IDs of the search results.
                ArrayList<Integer> searchResults = searchBooks(bookToSearch);

                // If the ArrayList has more than one item, then the user must input the ID of the specific book they'd like to update.
                if(searchResults.size() > 1){
                    System.out.println("Input ID of entry to update: ");
                    int bookToUpdate = userInput.nextInt();

                    // The updateBook() method passes the inputted book's unique ID number.
                    updateBook(bookToUpdate);

                }
                // If the ArrayList has only one item, then the item in the array is passed in the updateBook() method.
                else if(searchResults.size() == 1){

                    updateBook(searchResults.get(0));

                }
                // If the ArrayList is empty, then the below is printed and the switch statement is broken.
                else{
                    System.out.println("No results found. Please try again.");
                    break;
                }

                // If the book is updated with no errors, then this statement is displayed.
                System.out.println("Book successfully updated.");

            }

            // If the user inputs '3', then the user searches for the book and is given the option to delete the item.
            case "3" -> {

                System.out.println("Search book to delete by ID, Title, Author or Genre: ");
                String bookToSearch = userInput.nextLine();

                // This replacement of ' by '' prevents SQL syntax errors.
                bookToSearch = bookToSearch.replace("'", "''");

                // The searchBooks() method is used inside a new ArrayList that stores the IDs of the search results.
                ArrayList<Integer> searchResults = searchBooks(bookToSearch);

                // If the array is empty, then the user is told the item does not exist and the switch statement breaks.
                if (searchResults.isEmpty()){

                    System.out.println("No results found. Please try again.");
                    break;

                }

                // The user is asked if they are sure they want to delete the item.
                System.out.println("Are you sure you want to delete? y/n");
                String deleteCheck = userInput.nextLine();

                /*
                If the array is greater than 1 item and the user confirms the deletion,
                then they are asked to input the unique ID of the specific book to delete and the row is deleted.
                 */
                if(searchResults.size() > 1 && deleteCheck.equals("y")){

                    System.out.println("Input ID of entry to update: ");
                    int bookToDelete = userInput.nextInt();
                    deleteBook(bookToDelete);

                }
                /*
                If the array is equal to 1 and the user confirms the deletion, then the item in the array
                (which is the specific ID of book) is passed through the deleteBook() method and deleted.
                 */
                else if(searchResults.size() == 1 && deleteCheck.equals("y")){

                    deleteBook(searchResults.get(0));

                }
                // If the user cancels the deletion, then the message below is printed.
                else if(deleteCheck.equals("n")){

                    System.out.println("Item not deleted.");

                }
                // If the user inputs an invalid option, then the below prints.
                else{

                    System.out.println("Invalid input. Try again.");

                }

            }

            // If the user inputs '4', then the user is able to search for a book(s) by inputting the ID, Title, Author or Genre.
            case "4" -> {

                System.out.println("Search by ID, Title, Author or Genre: ");
                String bookToSearch = userInput.nextLine();

                // This replacement of ' by '' prevents SQL syntax errors.
                bookToSearch = bookToSearch.replace("'", "''");

                // The searchBooks() method is used inside a new ArrayList that stores the IDs of the search results.
                ArrayList<Integer> searchResults = searchBooks(bookToSearch);

                // If the array is empty, then the below is printed.

                if(searchResults.isEmpty()){
                    System.out.println("No results found. Please try again.");
                }

            }
            // If the user inputs '0', then the program exits.
            case "0" -> System.out.println("Goodbye!");

            // If the user inputs anything else, this message is shown and the while loop repeats.
            default -> System.out.println("Input Error. Try Again");
        }
    }

    // The addBook() method passes all details about a book (minus the id) and adds it to the 'books' table in the ebookstore database.
    public static void addBook(String title, String author, String genre, int quantity){

        try{
            // Connection to the ebookstore database, via the jdbc:mysql: channel on localhost (this PC)
            // Use username "clerk", password "bookstore".
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/ebookstore?useSSL=false",
                    "clerk",
                    "bookstore"
            );
            // Create a direct line to the database for running our queries
            Statement statement = connection.createStatement();

            // The last row of the 'books' table is stored in ResultSet. Reference: https://www.tutorialspoint.com/how-to-select-last-row-in-mysql
            ResultSet finalBookID = statement.executeQuery("SELECT * FROM books ORDER BY id DESC LIMIT 1");

            // As the id for the new book is determined based on the last row of the table, the initial book id is set to 3000;
            int bookID = 3000;

            // This while statement updates the 'bookID' variable and stores the final id. If the table is empty, then 'bookID' remains 3000.
            while (finalBookID.next()) {
                bookID = finalBookID.getInt("id");
            }

            // One is added to the final ID in order to create a new, unique book ID.
            bookID += 1;

            // Details for the new book are inserted into the 'books' table.
            statement.executeUpdate("INSERT INTO books VALUES ('"+bookID+"', '"+title+"', '"+author+"', '"+genre+"', '"+quantity+"')");

            // The new final ID is stored in 'finalBookID' and then the entire new entry is printed.
            finalBookID = statement.executeQuery("SELECT * FROM books ORDER BY id DESC LIMIT 1");

            printRows(finalBookID);

            finalBookID.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("Problems connecting to the database.");
            e.printStackTrace();
        }
    }

    // This method is used to search for book(s) using either ID, Title, Author or Genre. And ArrayList is returned and the book element to search is passed.
    public static ArrayList<Integer> searchBooks(String bookElementToSearch){

        // An empty ArrayList is declared that will store the ID(s) of the found book(s).
        ArrayList<Integer> listOfIDs = new ArrayList<>();

        try {
            // Connection to the ebookstore database, via the jdbc:mysql: channel on localhost (this PC)
            // Use username "clerk", password "bookstore".
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/ebookstore?useSSL=false",
                    "clerk",
                    "bookstore"
            );
            // Create a direct line to the database for running our queries
            Statement statement = connection.createStatement();

            // executeQuery: runs a SELECT statement where the book element is searched in each of the columns (excluding quantity).
            ResultSet booksFound = statement.executeQuery("SELECT * FROM books WHERE '"+ bookElementToSearch +"' IN (id, title, author, genre)");

            // The results are printed and the ID of each is added to the ArrayList.
            while (booksFound.next()) {
                System.out.println(
                        booksFound.getInt("id") + ", "
                                + booksFound.getString("title") + ", "
                                + booksFound.getString("author") + ", "
                                + booksFound.getString("genre") + ", "
                                + booksFound.getInt("qty")
                );

                listOfIDs.add(booksFound.getInt("id"));

            }

            booksFound.close();
            statement.close();
            connection.close();

            // The list of IDs are returned.
            return listOfIDs;

        } catch (SQLException e) {
            System.out.println("Problems connecting to the database.");
            e.printStackTrace();

        }

        // If the try-catch block doesn't run, an empty list is still returned.
        return listOfIDs;

    }

    // This updateBook() method passes an item's ID number in order to update the entry.
    public static void updateBook(int idNumber){

        Scanner userInput = new Scanner(System.in);
        // A new menu is presented for the user to choose which element to update.
        System.out.println("""
                        Enter element to update:
                        a. Title
                        b. Author
                        c. Genre
                        d. Quantity
                        0. Back
                        """);

        String bookElement = userInput.nextLine();

        try{
            // Connection to the ebookstore database, via the jdbc:mysql: channel on localhost (this PC)
            // Use username "clerk", password "bookstore".
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/ebookstore?useSSL=false",
                    "clerk",
                    "bookstore"
            );
            // Create a direct line to the database for running our queries
            Statement statement = connection.createStatement();

            // A switch statement runs that, depending on the user's input, updates different parts of an entry - excluding its unique ID (which is used to find the entry).
            switch (bookElement) {
                case "a" -> {

                    System.out.println("Enter new title of the book: ");
                    String newBookTitle = userInput.nextLine();

                    newBookTitle = newBookTitle.replace("'", "''");

                    statement.executeUpdate("UPDATE books SET title =  '"+ newBookTitle +"' WHERE id = '"+ idNumber +"'");


                }
                case "b" -> {

                    System.out.println("Enter new author of the book: ");
                    String newBookAuthor = userInput.nextLine();

                    newBookAuthor = newBookAuthor.replace("'", "''");

                    statement.executeUpdate("UPDATE books SET author =  '"+ newBookAuthor +"' WHERE id = '"+ idNumber +"'");


                }
                case "c" -> {

                    System.out.println("Enter new genre: ");
                    String newBookGenre = userInput.nextLine();

                    newBookGenre = newBookGenre.replace("'", "''");

                    statement.executeUpdate("UPDATE books SET genre =  '"+ newBookGenre +"' WHERE id = '"+ idNumber +"'");


                }
                case "d" -> {

                    System.out.println("Enter new quantity: ");
                    int newBookQuantity = userInput.nextInt();

                    statement.executeUpdate("UPDATE books SET qty =  '"+ newBookQuantity +"' WHERE id = '"+ idNumber +"'");


                }
                // If the user chooses '0', then they are taken back to the main menu.
                case "0" -> System.out.println("Going back");

                default -> System.out.println("Invalid choice.");

            }

            // The updated entry is stored in the ResultSet and then printed using printRows().
            ResultSet updateRow = statement.executeQuery("SELECT * FROM books WHERE id = '"+ idNumber +"'");
            printRows(updateRow);

            updateRow.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("Problems connecting to the database.");
            e.printStackTrace();
        }
    }

    // This method is used to delete an entry using its ID.
    public static void deleteBook(int idNumber){

        try {
            // Connection to the ebookstore database, via the jdbc:mysql: channel on localhost (this PC)
            // Use username "clerk", password "bookstore".
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/ebookstore?useSSL=false",
                    "clerk",
                    "bookstore"
            );
            // Create a direct line to the database for running our queries
            Statement statement = connection.createStatement();

            // Entry is deleted and the below statement is printed confirming the deletion.
            statement.executeUpdate("DELETE FROM books WHERE id='" + idNumber +"'");
            System.out.println("Row deleted");

            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("Problems connecting to the database.");
            e.printStackTrace();
        }
    }
}
