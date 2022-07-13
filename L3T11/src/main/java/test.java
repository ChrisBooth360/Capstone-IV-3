import java.util.Arrays;

public class test {

    public static void main(String[] args){

        int[] listOfNumbers = {56, 24, 22, 1, 3, 45, 42, 33, 19, 10};
        int listLength = listOfNumbers.length;

        System.out.println("Original list: " + Arrays.toString(listOfNumbers));

        quickSort(listOfNumbers, 0, listLength - 1);

        System.out.println("Sorted list: " + Arrays.toString(listOfNumbers));

    }

    public static void quickSort(int[] list, int low, int high) {
        if (low < high) {
            int mid = partition(list,low,high);
            quickSort(list, low, mid - 1);
            quickSort(list, mid + 1, high);
        }
    }

    public static int partition(int[] list, int low, int high) {
// The pivot point is the first item in the subarray
        int pivot = getMedianOfThree(list, low, high);
        System.out.println("pivot: " + pivot);
// Loop through the array. Move items up or down the array so that they
// are in the proper spot with regards to the pivot point.
        do {
// can we find a number smaller than the pivot point? keep
// moving the ''high'' marker down the array until we find
// this, or until high=low
            while (low < high && list[high] >= pivot) {
                high--;
                System.out.println("highIndex: " + high);
            }
            if (low < high) {
// found a smaller number; swap it into position
                swap(list, low, high);
                System.out.println(Arrays.toString(list));
// now look for a number larger than the pivot point
                while (low < high && list[low] <= pivot) {
                    low++;
                    System.out.println("lowIndex: " + low);
                }
                if (low < high) {
// found one! move it into position
                    swap(list, high, low);
                    System.out.println(Arrays.toString(list));
                }
            }
        } while (low < high);
// move the pivot back into the array and return its index
        list[low] = pivot;
        System.out.println("pivot back: " + list[low]);
        System.out.println("returned low: " + low);
        return low;
    }
    public static void swap(int[] list, int firstIndex, int secondIndex) {
        int temp = list[firstIndex];
        list[firstIndex] = list[secondIndex];
        list[secondIndex] = temp;
    }

    public static int getMedianOfThree(int[] list, int low, int high){

        int mid = (low + high) / 2;

        if(list[low] > list[mid])
            swap(list, low, mid);

        System.out.println("low " + low + " mid " + mid + " high " + high + " " + Arrays.toString(list));

        if(list[low] > list[high])
            swap(list, low, high);

        System.out.println("low " + low + "mid " + mid + "high " + high + " " + Arrays.toString(list));

        if(list[mid] > list[high])
            swap(list, mid, high);

        System.out.println("low " + low + "mid " + mid + "high " + high + " " + Arrays.toString(list));

        swap(list, low, mid);

        System.out.println("low " + low + "mid " + mid + "high " + high + " " + Arrays.toString(list));

        System.out.println("sorted after median: " + Arrays.toString(list));

        return list[low];

    }




}
