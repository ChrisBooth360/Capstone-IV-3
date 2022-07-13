import java.util.ArrayList;
import java.util.Arrays;

public class Task1 {

    public static void main(String[] args){



        Course history = new Course("History 101", 30, "Prof Plum");
        Course geography = new Course("Geography 201", 12, "Mrs Peacock");
        Course maths = new Course("Maths 103", 101, "Miss Scarlett");
        Course biology = new Course("Biology 301", 5, "Colonel Mustard");
        Course astrology = new Course("Astrology 204", 17, "Reverend Green");

        ArrayList<Course> courses1 = new ArrayList<>();

        courses1.add(history);
        courses1.add(geography);
        courses1.add(maths);
        courses1.add(biology);
        courses1.add(astrology);

        for()





    }

    void bubbleSort(ArrayList<Course> courseArrayList) {


        for (int i = (courseArrayList.size() - 1); i >= 0; i--) {

            for (int j = 1; j <= i; j++) {

                if (courseArrayList.get(j - 1).getNumberOfStudents() > courseArrayList.get(j).getNumberOfStudents()) {
                    Course temp = courseArrayList.get(j - 1);
                    courseArrayList.set(j - 1, courseArrayList.get(j));
                    courseArrayList.set(j, temp);
                }
            }
        }
    }

}
