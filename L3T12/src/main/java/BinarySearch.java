import java.util.ArrayList;
import java.util.Collections;

public class BinarySearch {

    public static void main(String[] args)
    {
        ArrayList<Integer> arr = new ArrayList<Integer>();

        arr.add(5);
        arr.add(10);
        arr.add(15);
        arr.add(20);
        arr.add(25);
        arr.add(30);
        arr.add(35);

        // Initializing the key to be found.
        int val = 10;

        // Printing elements of array list
        System.out.println("The elements of the arraylist are: "+arr);

        // Implementing the built-in binarySearch method from collections
        int result = Collections.binarySearch(arr,val);

        if (result == -1)
            System.out.println("Element not present");
        else
            System.out.println("The Element " + val + " is found at "
                    + "index " + result);
    }
}