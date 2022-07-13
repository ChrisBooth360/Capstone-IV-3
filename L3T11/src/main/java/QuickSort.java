// This program uses the median of 3 to discover the pivot point for quick swap. I used the below resource to help me with this:
// https://examples.javacodegeeks.com/quicksort-java-algorithm-code-example/

import java.util.Arrays;

public class QuickSort {

    public static void main(String[] args){

        // A random list of numbers is given and its length is determined.
        int[] listOfNumbers = {56, 24, 282, 1, 3, 45, 42, 33, 19, 10, 834, 123, -9, -56, 2};
        int listLength = listOfNumbers.length;

        // The original list is printed.
        System.out.println("Original list: " + Arrays.toString(listOfNumbers));

        // The quickSort() method takes in the list of numbers, the low index, and the high index point of the array.
        quickSort(listOfNumbers, 0, listLength - 1);

        // The sorted list is printed.
        System.out.println("Sorted list: " + Arrays.toString(listOfNumbers));

    }

    public static void quickSort(int[] list, int lowIndex, int highIndex) {
        // If the low index is smaller than the high index, then this if statement will run.
        if (lowIndex < highIndex) {
            // The partition method is called that will select the pivot point and move the elements around.
            int midIndex = partition(list, lowIndex, highIndex);

            // quickSort() is recursively called for the first and second halves of the list.
            quickSort(list, lowIndex, midIndex - 1);
            quickSort(list, midIndex + 1, highIndex);
        }
    }

    public static int partition(int[] list, int lowIndex, int highIndex) {

        // The pivot point is first determined using the getMedianOfThree() method that takes the list and the low and high index.
        int pivot = getMedianOfThree(list, lowIndex, highIndex);

        // The array is looped through and the items are moved up and down the array
        // so that they are in the proper spot in regard to the pivot point.
        do {
            // This while loop finds a number smaller than the pivot point (if one exists).
            while (lowIndex < highIndex && list[highIndex] >= pivot) {
                highIndex--;
            }
            if (lowIndex < highIndex) {
                // If a smaller number is found, then the numbers are switched.
                swap(list, lowIndex, highIndex);

                // This while loop looks for a number larger than the pivot point.
                while (lowIndex < highIndex && list[lowIndex] <= pivot) {
                    lowIndex++;
                }
                if (lowIndex < highIndex) {
                    // If a larger number is found, then it is switched.
                    swap(list, highIndex, lowIndex);
                }
            }
        } while (lowIndex < highIndex);

        // The pivot point is moved back into the array and its index is returned as the midIndex.
        list[lowIndex] = pivot;
        return lowIndex;
    }

    // The getMedianOfThree() method finds the middle number of the first, mid and last points of an array, and arranges
    // them so that the low index point of the list is the median, the middle index point of the list is the smallest of the three,
    // and the high index point of the list is the largest. The low index is then returned - which is the pivot point.
    public static int getMedianOfThree(int[] list, int lowIndex, int highIndex){

        // The midIndex is determined by adding the low and high index and dividing it by 2.
        int midIndex = (lowIndex + highIndex) / 2;

        // If the low index is greater than the mid-index, then the two items are swapped.
        if(list[lowIndex] > list[midIndex])
            swap(list, lowIndex, midIndex);

        // If the low index is greater than the high index, then the two items are swapped.
        if(list[lowIndex] > list[highIndex])
            swap(list, lowIndex, highIndex);

        // If the mid-index is greater than the high index, then the two items are swapped.
        if(list[midIndex] > list[highIndex])
            swap(list, midIndex, highIndex);

        // The smallest number of the low, mid and high indexes is now swapped with the median number and the median is returned as the pivot point.
        swap(list, lowIndex, midIndex);
        return list[lowIndex];

    }

    // This method swaps two numbers in the list at two different indexes.
    public static void swap(int[] list, int firstIndex, int secondIndex) {
        int temp = list[firstIndex];
        list[firstIndex] = list[secondIndex];
        list[secondIndex] = temp;
    }
}
