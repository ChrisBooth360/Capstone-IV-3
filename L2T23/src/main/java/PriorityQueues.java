import java.util.PriorityQueue;

public class PriorityQueues {

    public static void main(String[] args){

        // Two Priority Queues are created, populated with a variety of names, and printed out.
        PriorityQueue<String> names1 = new PriorityQueue<>();

        names1.offer("George");
        names1.offer("Jim");
        names1.offer("John");
        names1.offer("Blake");
        names1.offer("Kevin");
        names1.offer("Michael");

        System.out.println("List 1:\n" + names1 + "\n");

        PriorityQueue<String> names2 = new PriorityQueue<>();

        names2.offer("George");
        names2.offer("Katie");
        names2.offer("Kevin");
        names2.offer("Michelle");
        names2.offer("Ryan");

        System.out.println("List 2:\n" + names2 + "\n");

        // The two lists are copied into temporary PriorityQueues in order to determine the two list of names combined (minus the doubles).
        PriorityQueue<String> unionList1 = setTempList(names1);

        PriorityQueue<String> unionList2 = setTempList(names2);

        getUnion(unionList1, unionList2);

        // The two lists are copied into temporary PriorityQueues in order to determine all the non-duplicated names.
        PriorityQueue<String> differenceList1 = setTempList(names1);

        PriorityQueue<String> differenceList2 = setTempList(names2);

        getDifference(differenceList1, differenceList2);

        // The two lists are copied into temporary PriorityQueues in order to determine all the duplicated names.
        PriorityQueue<String> intersectionList1 = setTempList(names1);

        PriorityQueue<String> intersectionList2 = setTempList(names2);

        getIntersection(intersectionList1, intersectionList2);

    }

    // This method creates a temporary PriorityQueue by adding all items from the passed list into a new PriorityQueue and returning it.
    public static PriorityQueue<String> setTempList(PriorityQueue<String> originalList){

        PriorityQueue<String> storedList = new PriorityQueue<>();

        storedList.addAll(originalList);

        return storedList;

    }

    // This method takes in two temp PriorityQueues and will print the union of the two lists.
    public static void getUnion(PriorityQueue<String> list1, PriorityQueue<String> list2){

        // First, another two temporary lists are created.
        PriorityQueue<String> tempList1 = setTempList(list1);
        PriorityQueue<String> tempList2 = setTempList(list2);

        // All duplicates are removed from tempList2.
        tempList2.removeAll(tempList1);

        // All items in tempList2 are added to list1 - this will prevent any duplicates printed in the list.
        list1.addAll(tempList2);

        System.out.println("The union of the two lists are:\n" + list1 + "\n");

    }

    // This method takes in two temp PriorityQueues and will print the difference of the two lists.
    public static void getDifference(PriorityQueue<String> list1, PriorityQueue<String> list2){

        // First, another two temporary lists are created.
        PriorityQueue<String> tempList1 = setTempList(list1);
        PriorityQueue<String> tempList2 = setTempList(list2);

        // All duplicates are removed from tempList2.
        tempList2.removeAll(tempList1);

        // All duplicates are removed from list1.
        list1.removeAll(list2);

        // All items in tempList2 are added to list1 - thus excluding the two duplicated names from appearing at all.
        list1.addAll(tempList2);

        System.out.println("The difference of the two lists are:\n" + list1 + "\n");

    }

    // This method takes in two temp PriorityQueues and will print the intersection of the two lists.
    public static void getIntersection(PriorityQueue<String> list1, PriorityQueue<String> list2){

        // All items in list2 are retained in list1 - thus removing all names that don't appear in both lists.
        list1.retainAll(list2);

        System.out.println("The intersection of the two lists are:\n" + list1 + "\n");

    }
}
