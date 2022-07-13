import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class PoisedStoredData {


    //IN AND ABOVE MAIN CLASS

    // A Static final ArrayList for Projects is created to keep track of Completed Projects to be written to a file when the project closes.
    private static final ArrayList<Project> completedProjects = new ArrayList<>();

    /*
        When the program starts, a method is called that will create an array of Project objects that are stored in an external file.
        This array will then be used throughout the program to access certain projects, etc.
         */
    ArrayList<Project> projectList = getProjectList();


    /**
     * The getProjectList() method reads every non-blank line of the file PoisedProjects.txt and records them in a Queue.
     * This Queue is then run through the recursive method createProjectArray() in order create all the project objects and record them in an array.
     * @return returns an array of all projects from PoisedProjects.txt.
     */
    private static ArrayList<Project> getProjectList() {

        Queue<String> projectComponents = new LinkedList<>();

        /*
        A try-with-resources block is run in case the file does not exist.
        If the file does not exist, then a new Array is returned that will be written to a newly created file when the project ends.
         */
        try(FileReader projects = new FileReader("PoisedProjects.txt");
            Scanner readProjects = new Scanner(projects)){

            // A while loop runs over every line in the document and exits when there are no more lines.
            while(readProjects.hasNextLine()){

                // Each line that contains a ": " character is split and the second item is added to the Queue. Lines that do not contain this character are ignored.
                String currentLine = readProjects.nextLine();

                String[] splitLine = currentLine.split(": ");

                if(splitLine.length == 2){

                    projectComponents.offer(splitLine[1]);

                }
            }

            /*
            As there are 22 items in each project, if the modulus of the length of the Queue is 0 when given 22,
            then the correct amount of information has been added to each project and a project list array can be created
            by passing both the project and an empty array in the createArrayOfProjects method.
            If not, then an error is displayed, an empty list is returned, and the program exits.
             */
            if(projectComponents.size() % 22 == 0){

                ArrayList<Project> projectList = new ArrayList<>();

                projectList = createArrayOfProjects(projectComponents, projectList);

                return projectList;

            } else{

                System.out.println("Invalid information in source file. The program will exit until issue is resolved.");
                System.exit(0);
                return new ArrayList<>();

            }
        }
        // An IOException is declared in the catch component. If the file is not present, this error will run and an empty ArrayList is returned.
        catch(IOException e){

            System.out.println("File Not Found.");
            return new ArrayList<>();

        }
    }

    /**
     * createArrayOfProjects() is a recursive method that will loop for as long as the Queue passed through it is not empty.
     * Once the Queue is empty, then an array - whether it is empty or not - is returned.
     * @param queueOfProjectInfo The queue of all project components
     * @param arrayOfProjects The array of all projects - which will be empty at first
     * @return returns an array of all projects from Queue created in previous method OR returns the method itself to help with recursion.
     */
    private static ArrayList<Project> createArrayOfProjects(Queue<String> queueOfProjectInfo, ArrayList<Project> arrayOfProjects){

        // A try-catch block is implemented in case there is invalid information in the Queue.
        try{

            // If the Queue is empty, then the arrayOfProjects is returned and the recursion ends.
            if(queueOfProjectInfo.isEmpty()){

                return arrayOfProjects;

            }

            /*
            If the Queue list is populated, then each item of the Queue is removed one at a time, starting with the first item.
            As the project details are saved in a specific order, it is easy to assume which items will be removed first.
            Each item can then be stored and used to create the relevant objects while also setting those attributes that are not
            passed when creating a specific object.
             */
            else{

                int projectNumber = Integer.parseInt(queueOfProjectInfo.poll());
                String projectName = queueOfProjectInfo.poll();
                String buildingType = queueOfProjectInfo.poll();
                String buildingAddress = queueOfProjectInfo.poll();
                int erfNumber = Integer.parseInt(Objects.requireNonNull(queueOfProjectInfo.poll()));


                // As the totals are written with the rand sign and two decimal points, the parseDouble() method is called.
                String totalFeeString = queueOfProjectInfo.poll();
                double totalFee = parseDouble(totalFeeString);

                String totalPaidString = queueOfProjectInfo.poll();
                double totalPaid = parseDouble(totalPaidString);

                queueOfProjectInfo.poll(); // This removed item is totalOwed, which is automatically set within a ProjectInfo object and not needed here.

                // In order to get the correct format for the LocalDate object, the formatDate() method is called.
                String deadline = queueOfProjectInfo.poll();
                LocalDate deadlineDate = formatDate(deadline);

                // The complete date of the project is captured and will be set below once the Project object is created.
                String completeDate = queueOfProjectInfo.poll();

                // A ProjectInfo object is created passing some captured information above. The project number and total paid is then set underneath.
                ProjectInfo capturedProjectInfo = new ProjectInfo(projectName, buildingType, buildingAddress, erfNumber, totalFee, deadlineDate);

                capturedProjectInfo.setProjectNumber(projectNumber);
                capturedProjectInfo.setTotalPaid(totalPaid);

                // Architect's information is stored and then an Architect object is created.
                String architectName = queueOfProjectInfo.poll();
                String architectPhone = queueOfProjectInfo.poll();
                String architectEmail = queueOfProjectInfo.poll();
                String architectAddress = queueOfProjectInfo.poll();

                Person capturedArchitect = new Person(Person.Type.ARCHITECT, architectName, architectPhone, architectEmail, architectAddress);

                // Contractor's information is stored and then a Contractor object is created.
                String contractorName = queueOfProjectInfo.poll();
                String contractorPhone = queueOfProjectInfo.poll();
                String contractorEmail = queueOfProjectInfo.poll();
                String contractorAddress = queueOfProjectInfo.poll();

                Person capturedContractor = new Person(Person.Type.CONTRACTOR, contractorName, contractorPhone, contractorEmail, contractorAddress);

                // Customer's information is stored and then a Customer object is created.
                String customerName = queueOfProjectInfo.poll();
                String customerPhone = queueOfProjectInfo.poll();
                String customerEmail = queueOfProjectInfo.poll();
                String customerAddress = queueOfProjectInfo.poll();

                Person capturedCustomer = new Person(Person.Type.CUSTOMER, customerName, customerPhone, customerEmail, customerAddress);

                // A Project object is created that passes the above objects.
                Project capturedProject = new Project(capturedProjectInfo, capturedArchitect, capturedContractor, capturedCustomer);

                // If the complete date does not equal 'Incomplete', the project is marked as finalised, a complete date is set, and it is added to the list of Completed Projects.
                assert completeDate != null;
                if(!completeDate.equals("Incomplete")){

                    capturedProject.finalise = true;
                    capturedProject.projectInfo.setCompleteDate(formatDate(completeDate));
                    completedProjects.add(capturedProject);

                }

                // The Project object is added to the arrayOfProjects and the method is called again in order the capture the next Project, or to exit the recursion.
                arrayOfProjects.add(capturedProject);

                return createArrayOfProjects(queueOfProjectInfo, arrayOfProjects);

            }
        }
        // If there is any invalid information then the below information prints and the program exits.
        catch (Exception e){

            System.out.println("Invalid information in source file. The program will exit until issue is resolved.");
            System.exit(0);
            return new ArrayList<>();

        }
    }

}
