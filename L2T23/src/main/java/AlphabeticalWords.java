import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class AlphabeticalWords {

    public static void main(String[] args){

        // A new ArrayList is created that will contain Strings.
        List<String> randomWords = new ArrayList<>();

        /*
        A try-with-resources is implemented for the opening of the file 'randomWords.txt'.
        SonarLint suggested using try-with-resources in order to make closing the file easier than when using try-catch-finally.
        FileReader was used instead of File, as it has an automatic close function. The Scanner was also passed in the block.
         */
        try(FileReader reader = new FileReader("randomWords.txt");
            Scanner readWords = new Scanner(reader)){

            // A while loop runs over every line in the document and exits when there are no more lines.
            while(readWords.hasNext()){

                /*
                 A line is added to the string 'newWord' and an if statement checks whether the first character is a letter.
                 If it is, then it is added to the ArrayList. Lines that start with a non-letter are ignored.
                 */
                String newWord = readWords.next();

                if(Character.isLetter(newWord.charAt(0))){
                    randomWords.add(newWord);
                }
            }

            // The ArrayList is sorted alphabetically.
            Collections.sort(randomWords);

            // An iterator is created and a while loop runs over each item in the list and prints it out.
            Iterator<String> randomWordIterator = randomWords.iterator();

            while(randomWordIterator.hasNext()){

                System.out.println(randomWordIterator.next());

            }

        }
        // An IOException is declared in the catch component. If the file is not present, this error will run.
        catch(IOException e){

            System.out.println("File Not Found.");

        }
    }
}
