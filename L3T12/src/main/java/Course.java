import java.util.Comparator;
// A Course class is created that implements the Comparator interface.
public class Course implements Comparable<Course> {

    // Attributes are declared.
    String courseName;
    int numberOfStudents;
    String courseLecturer;

    // A constructor is declared that takes in a course name, the number of students and the course lecturer.
    Course(String courseName, int numberOfStudents, String courseLecturer){

        this.courseName = courseName;
        this.numberOfStudents = numberOfStudents;
        this.courseLecturer = courseLecturer;

    }

    // Getters and Setters are defined.
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(int numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    public String getCourseLecturer() {
        return courseLecturer;
    }

    public void setCourseLecturer(String courseLecturer) {
        this.courseLecturer = courseLecturer;
    }

    // A toString() method is defined.
    public String toString() {
        return "Course: " + courseName + "\n" +
                "Course Lecturer: " + courseLecturer + "\n" +
                "Number of students: " + numberOfStudents + "\n";

    }
    // This overriding method compareTo() allows the user to sort a list of Courses by the number of students in the course.
    // https://howtodoinjava.com/java/sort/collections-sort/
    @Override
    public int compareTo(Course o) {
        return Integer.compare(this.getNumberOfStudents(), o.getNumberOfStudents());
    }

    // A Comparator is created called 'alphabeticalCourse'.
    public static final Comparator<Course> alphabeticalCourse = new Comparator<>() {

        // This overriding method compare() is used to sort a list of Courses by the course names.
        @Override
        public int compare(Course o1, Course o2) {
            return o1.getCourseName().compareTo(o2.getCourseName());
        }
    };

}
