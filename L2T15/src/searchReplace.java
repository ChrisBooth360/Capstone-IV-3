import java.util.Scanner;

public class searchReplace {

    public static void main(String[] args){
        // The user is asked to input a string, the substring they want to search for and the replacement substring.
        System.out.println("Search and Replace");

        Scanner userInput = new Scanner(System.in);

        System.out.println("Enter a string: ");
        String string = userInput.nextLine();


        System.out.println("Enter the substring you wish to find: ");
        String subString = userInput.nextLine();


        System.out.println("Enter a string to replace the given substring: ");
        String replacement = userInput.nextLine();

        // The method searchAndReplace() is called with the three strings as its argument. The result is then printed.
        String newString = searchAndReplace(string, subString, replacement);

        System.out.println(newString);

    }

    // A recursive method is declared that takes three strings as its argument.
    public static String searchAndReplace(String string, String subString, String replacement){

        // Firstly, the string is converted to lowercase in case the substring is not in the same case as the characters in the string.
        String lowerCaseString = string.toLowerCase();

        // The first index point of the substring in the string is determined.
        int startIndex = lowerCaseString.indexOf(subString);

        // If the startIndex is -1 (which it would be if the substring was not found in the string), then the string is returned.
        if(startIndex == -1){

            return string;

        }
        // If the startIndex is a positive integer, then the else statement will run.
        else{

            // The final index of the substring within the string is found by adding the starting index to the length of the substring.
            int endIndex = startIndex + subString.length();

            /*
            The string is then updated to be: the whole string from the first index up until the startIndex point,
            plus the replacement phrase, plus the string from the endIndex until the final index point of the string.
             */
            string = string.substring(0, startIndex) + replacement + string.substring(endIndex);

            // The method is then returned with the updated string, the same substring and the same replacement string.
            return searchAndReplace(string, subString, replacement);

        }

    }

}
