import java.util.ArrayList;

public class BubbleSort {


    // A list of random words are added to an array, and they are run through a bubbleSort() method before being printed out.
    public static void main(String[] args){

        ArrayList<String> listOfWords = new ArrayList<>();

        listOfWords.add("right");
        listOfWords.add("subdued");
        listOfWords.add("trick");
        listOfWords.add("crayon");
        listOfWords.add("punishment");
        listOfWords.add("silk");
        listOfWords.add("describe");
        listOfWords.add("royal");
        listOfWords.add("prevent");
        listOfWords.add("slope");

        bubbleSort(listOfWords);

        for (String listOfWord : listOfWords) {
            System.out.print(listOfWord + "\n");
        }

    }

    // The bubbleSort() method uses the Bubble Sort algorithm to sort words alphabetically.
    public static void bubbleSort(ArrayList<String> arrayOfWords) {

        // A for loop uses the length of the array (minus one) as the control variable and counts it down with each variable.
        for (int i = (arrayOfWords.size() - 1); i >= 0; i--) {

            // A nested for loop, sets the control variable to one and increases if with each iteration until it is equal to the
            // outer control variable.
            for (int j = 1; j <= i; j++) {

                /* If the first character of the first string is greater than the first character of the second string,
                 then the strings swap positions (this is due to the fact that characters that come earlier in the
                 alphabet are less than letter that come later in the alphabet when you compare them).
                 https://javahungry.blogspot.com/2020/04/compare-characters-java.html
                */
                if (arrayOfWords.get(j - 1).charAt(0) > arrayOfWords.get(j).charAt(0)) {
                    String temp = arrayOfWords.get(j - 1);
                    arrayOfWords.set(j - 1, arrayOfWords.get(j));
                    arrayOfWords.set(j, temp);
                }
            }
        }
    }
}
