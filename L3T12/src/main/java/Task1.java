import java.util.ArrayList;
import java.util.Collections;

public class Task1 {

    public static void main(String[] args){

        // A new ArrayList is created for Course objects and five courses are added to the list.
        ArrayList<Course> courses1 = new ArrayList<>();

        courses1.add(new Course("History 101", 30, "Prof Plum"));
        courses1.add(new Course("Geography 201", 12, "Mrs Peacock"));
        courses1.add(new Course("Maths 103", 101, "Miss Scarlett"));
        courses1.add(new Course("Biology 301", 5, "Colonel Mustard"));
        courses1.add(new Course("Astrology 204", 17, "Reverend Green"));

        // Collections.sort is used to sort the list by number of students. This is done by using the Comparator interface (see the Course class for more information).
        Collections.sort(courses1);

        // The sorted list is printed out.
        System.out.println("Sorted by number of students: ");
        printList(courses1);

        // Collections.swap is used to swap the first and second objects, and the list is printed out.
        Collections.swap(courses1, 0, 1);

        System.out.println("First and second elements swapped: ");
        printList(courses1);

        // A second array for Course objects is created.
        ArrayList<Course> courses2 = new ArrayList<>();

        // The addAll() function is used to copy all the courses from courses1 list to courses2 list, and courses2 is printed out.
        // https://www.tutorialspoint.com/java/util/arraylist_addall_index.htm
        courses2.addAll(courses1);

        System.out.println("Courses2 after using addAll(): ");
        printList(courses2);

        // Collections.copy is used to copy courses from courses1 to courses2. But as they are all already added, the courses2 list remains the same.
        Collections.copy(courses2, courses1);

        System.out.println("Courses2 after using copy(): ");
        printList(courses2);

        // Two new courses are added to courses2 and the list is sorted alphabetically by course using an overriding Comparator.
        courses2.add(new Course("Java 101", 55, "Dr. P Green"));
        courses2.add(new Course("Advanced Programming", 93, "Prof. M Milton"));

        courses2.sort(Course.alphabeticalCourse);

        System.out.println("Courses2 sorted alphabetically: ");
        printList(courses2);

        // A custom binarySearch() is used to find the Course object with the course name 'Java 101'. This is then printed (if the element exists).
        int courseIndex = binarySearch(courses2, "Java 101");

        if(courseIndex == -1){

            System.out.println("Could not find course.");

        } else {

            System.out.println("Searching for Java 101:");
            System.out.println(courses2.get(courseIndex));

        }

        // Collections.disjoint is used to determine whether courses1 and courses2 contain any common elements. The result is then printed below using an if statement.
        boolean disjoint = Collections.disjoint(courses1, courses2);

        if(disjoint){

            System.out.println("The two lists of courses have no common elements.\n");

        } else{

            System.out.println("The two lists of courses have common elements.\n");

        }

        // The sort function is used with Collections.reverseOrder() to give the Course with the largest number of students first. This is then printed.
        courses2.sort(Collections.reverseOrder());

        System.out.println("The course with the most students: ");
        System.out.println(courses2.get(0));

        // The sort function is called again to give the Course with the least number of students first. This is then printed.
        Collections.sort(courses2);
        System.out.println("The course with the least students: ");
        System.out.println(courses2.get(0));


    }

    // This method uses a for loop to print out all the Course elements of a list.
    private static void printList(ArrayList<Course> courses1) {
        for(Course course : courses1){

            System.out.println(course);

        }
    }

    // A custom binarySearch() method is created that takes an ArrayList of Courses and a course name as parameters.
    // An algorithm developed from this webpage is edited slightly in order to find the course name.
    // https://books.trinket.io/thinkjava2/chapter12.html
    public static int binarySearch(ArrayList<Course> courseList, String courseName) {
        int low = 0;
        int high = courseList.size() - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            int comp = courseList.get(mid).getCourseName().compareTo(courseName);

            if (comp == 0) {
                return mid;
            } else if (comp < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }
}
