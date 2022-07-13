import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
// This program takes input from the user in the form of names and chores and randomly assigns chores to the names.
public class Chores {

    public static void main(String[] args) {

        System.out.println("Chore Randomiser - make sure your enter an equal number of names and chores");

        // A list of names and a list of chores is created using the makeList() function.
        List<String> listOfNames = makeList("name");

        List<String> listOfChores = makeList("chore");

        // If the two lists are equal, then the list of chores is shuffled and the names are printed out paired with a chore.
        if(listOfChores.size() == listOfNames.size()){

            Collections.shuffle(listOfChores); // Collections.shuffle() is used to shuffle the list of chores.

            for (int i = 0; i < listOfNames.size(); i++){

                System.out.println(listOfNames.get(i) + " will " + listOfChores.get(i));

            }
        }
        // If the list of chores is longer than the list of names, then this error prints and the program exits.
        else if(listOfChores.size() > listOfNames.size()){

            System.out.println("You entered more chores than people.");

        }
        // If the list of names is longer than the list of chores, then this error prints and the program exits.
        else{

            System.out.println("You entered more names than chores.");

        }
    }

    // This method has the user input a series of information and create an ArrayList from that information.
    public static ArrayList<String> makeList(String typeOfList){

        Scanner input = new Scanner(System.in);

        ArrayList<String> list = new ArrayList<>();

        // A while loop allows the user to input as many names/chores that they like until they input -1.
        String userInput = "";
        while (!userInput.equals("-1")){

            System.out.println("Enter " + typeOfList + " (type -1 to stop entering " + typeOfList + "s): ");

            userInput = input.nextLine();

            list.add(userInput);

        }

        // The last element of the list (which would be -1) is removed from the list and the list is returned.
        int lastIndex = list.size() - 1;
        list.remove(lastIndex);

        return list;
    }
}
