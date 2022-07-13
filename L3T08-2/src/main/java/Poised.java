/**
 * This program is a project management system for a small structural engineering firm called “Poised”.
 * Poised does the engineering needed to ensure the structural integrity of various buildings.
 * <p>
 * The program allows users to view, create, and update various projects.
 * @author Chris Booth
 * @version 1.0
 */

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Queue;

/**
 * This is the main class of the program and which contains all the methods used in the main method.
 */
public class Poised {

    // Static final String variables are declared to prevent the same string being written multiple times in the code.
    public static final String INPUT_ERROR = "You have input invalid information. Try Again.";
    public static final String ENTER_COMMAND = "Enter the ";
    public static final String ARCHITECT_STRING = "architect";
    public static final String CONTRACTOR_STRING = "contractor";
    public static final String CUSTOMER_STRING = "customer";
    public static final String ENGINEER_STRING = "structural engineer";
    public static final String MANAGER_STRING = "project manager";
    public static final String PERSON_NAME = "'s name: ";
    public static final String PERSON_PHONE = "'s telephone number: ";
    public static final String PERSON_EMAIL = "'s email address: ";
    public static final String PERSON_ADDRESS = "'s physical address: ";

    public static final String JOIN_TABLES = "SELECT * FROM project_info INNER JOIN pay_complete ON project_info.proj_num = pay_complete.proj_num " +
            "INNER JOIN build_info ON project_info.erf_num = build_info.erf_num " +
            "INNER JOIN architect ON project_info.architect = architect.arch_name " +
            "INNER JOIN contractor ON project_info.contractor = contractor.cont_name " +
            "INNER JOIN customer ON project_info.customer = customer.cust_name " +
            "INNER JOIN engineer ON project_info.engineer = engineer.engi_name " +
            "INNER JOIN project_manager ON project_info.project_manager = project_manager.pm_name";

    // A Static final ArrayList for Projects is created to keep track of Completed Projects to be written to a file when the project closes.
    private static final ArrayList<Project> completedProjects = new ArrayList<>();

    public static void main(String [] args){

        /*
        When the program starts, a method is called that will create an array of Project objects that are stored in an external file.
        This array will then be used throughout the program to access certain projects, etc.
         */
        ArrayList<Project> projectList = getProjectList();

        // A while loop runs until the user enters 'exit'. This while loop will be used as a menu.
        String userChoice = "";
        while (!userChoice.equals("exit")) {
            System.out.println("""
                        Select an option:
                        new - create a new project
                        view - view and update projects
                        search - search and update projects
                        exit - exit the program
                        """);

            Scanner userInput = new Scanner(System.in);
            userChoice = userInput.nextLine();

            // An enhanced switch statement is used to determine the menu item the user has selected and run the appropriate code.
            switch (userChoice) {
                /*
                If the user enters 'new', then four objects are created that are then used as parameters for the new Project object
                - this is to prevent too many parameters from being passed. The four objects are created using the relative methods below.
                */
                case "new" -> {

                    ProjectInfo newProjectInfo = inputProjectInfo(projectList);
                    Person newArchitect = inputPersonInfo(ARCHITECT_STRING);
                    Person newContractor = inputPersonInfo(CONTRACTOR_STRING);
                    Person newCustomer = inputPersonInfo(CUSTOMER_STRING);

                    Project newProject = new Project(newProjectInfo, newArchitect, newContractor, newCustomer);

                    // The Project object is printed out and added to the array of Project items.
                    System.out.println(newProject);
                    projectList.add(newProject);
                }

                /*
                If the user inputs 'view', then an enhanced for loop prints out each of the projects in the project list
                and a method called getViewOptions() is called with the list of projects passed as its argument.
                */
                case "view" -> {

                    for (Project project : projectList) {
                        System.out.println(project);
                    }

                    getViewOptions(projectList);

                }

                //If the user inputs 'search', a method is called that will find and return the desired project, before giving a list of update options.
                case "search" -> {

                    Project projectToUpdate = searchForProject(projectList);

                    // If the project is not null, then getUpdateOptions() is returned with the projectToUpdate as a parameter. Otherwise, the menu will be called again.
                    if(projectToUpdate != null){

                        getUpdateOptions(projectToUpdate);

                    }
                }

                /*
                If the user inputs 'exit', then each project in projectList is written to a file, each project in completedProjects is written to another file,
                'Goodbye' is printed and the while loop exits, ending the program.
                 */
                case "exit" -> {
                    writeProjectListToFile(projectList, "PoisedProjects.txt");
                    writeProjectListToFile(completedProjects, "CompletedProjects.txt");
                    System.out.println("Goodbye!");
                }

                // If the user inputs anything else, this message is shown and the while loop repeats.
                default -> System.out.println(INPUT_ERROR);
            }
        }
    }

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

    /**
     * This method gathers input from the user in order to create a new ProjectInfo object.
     * An array is passed through the method in order to help create an appropriate Project Number.
     * @param listOfProjects The list of all projects
     * @return new ProjectInfo object.
     */
    private static ProjectInfo inputProjectInfo(ArrayList<Project> listOfProjects){

        Scanner projectInfo = new Scanner(System.in);

        // A try-catch block is executed in order to make sure the user inputs the correct input.
        try{
            System.out.println("Enter the project name (Optional): ");
            String projectName = projectInfo.nextLine();

            // An enhanced for loop checks to see if the project name entered does not already exist. If it does, then an error prints and the method is called again.
            for(Project project : listOfProjects){

                if(project.projectInfo.getProjectName().equals(projectName)){

                    System.out.println("This project name already exists. Try again.");
                    return inputProjectInfo(listOfProjects);

                }
            }

            System.out.println("Enter the building type: ");
            String building = projectInfo.nextLine();

            System.out.println("Enter the building address: ");
            String buildingAddress = projectInfo.nextLine();

            System.out.println("Enter the ERF Number: ");
            int erf = projectInfo.nextInt();

            System.out.println("Enter the total fee: ");
            double fee = projectInfo.nextInt();
            projectInfo.nextLine();

            System.out.println("Enter the deadline (dd-mm-yyyy): ");
            String deadline = projectInfo.nextLine();
            LocalDate deadlineDate = formatDate(deadline);

            // If the deadline input by the user is before the current date, then this error is printed and the method is called again, thus repeating it.
            if(deadlineDate.isBefore(LocalDate.now())){

                System.out.println("The deadline cannot be set to a past date. Try Again.");
                return inputProjectInfo(listOfProjects);

            }

            // If any input information is empty (aside from the optional project name), then this error is printed and the method is called again, thus repeating it.
            if(building.isEmpty() || buildingAddress.isEmpty() || erf == 0 || fee == 0 || deadline.isEmpty()){

                System.out.println("Make sure you input all relevant information. Try Again.");
                return inputProjectInfo(listOfProjects);

            }

            // A new ProjectInfo object is created and its project number is set to the length of the list of projects plus 1.
            ProjectInfo newProjectInfo = new ProjectInfo(projectName, building, buildingAddress, erf, fee, deadlineDate);

            newProjectInfo.setProjectNumber(listOfProjects.size() + 1);

            // If all the input is valid, then the new object is returned.
            return newProjectInfo;

        } catch(Exception e){

            // If the user inputs anything invalid, then an error is printed and the method is called again, thus repeating it.
            System.out.println(INPUT_ERROR);
            return inputProjectInfo(listOfProjects);
        }
    }

    /**
     * This method gathers input from the user in order to create a new Person object.
     * A String of the Person Type is passed through the method in order to determine which person's information is being inputted.
     * @param personType The type of person for the Person object
     * @return new Person object.
     */
    private static Person inputPersonInfo(String personType){

        Scanner personInfo = new Scanner(System.in);

        // A try-catch block is executed in order to make sure the user inputs the correct input.
        try{

            System.out.println(ENTER_COMMAND + personType + PERSON_NAME);
            String personName = personInfo.nextLine();

            System.out.println(ENTER_COMMAND + personType + PERSON_PHONE);
            String personPhone = personInfo.nextLine();

            System.out.println(ENTER_COMMAND + personType + PERSON_EMAIL);
            String personEmail = personInfo.nextLine();

            System.out.println(ENTER_COMMAND + personType + PERSON_ADDRESS);
            String personAddress = personInfo.nextLine();

            // If any input information is empty, then this error is printed and the method is called again, thus repeating it.
            if(personName.isEmpty() || personPhone.isEmpty() || personEmail.isEmpty() || personAddress.isEmpty()){

                System.out.println("Make sure you input all relevant information. Try Again.");
                return inputPersonInfo(personType);

            }

            // An enhanced switch is used to create the Person object with the appropriate person type.
            return switch (personType) {
                case ARCHITECT_STRING -> new Person(Person.Type.ARCHITECT, personName, personPhone, personEmail, personAddress);
                case CONTRACTOR_STRING -> new Person(Person.Type.CONTRACTOR, personName, personPhone, personEmail, personAddress);
                case CUSTOMER_STRING -> new Person(Person.Type.CUSTOMER, personName, personPhone, personEmail, personAddress);
                default -> null;
            };

        } catch(Exception e){
            // If the user inputs anything invalid, then an error is printed and the method is called again, thus repeating it.
            System.out.println(INPUT_ERROR);
            return inputPersonInfo(personType);
        }
    }

    /**
     * This method allows the user to further refine the projects they would like to view.
     * It allows them to view incomplete projects and overdue projects.
     * @param projectList The list of all projects
     */
    private static void getViewOptions(ArrayList<Project> projectList){

        // When getViewOptions is called, a menu is printed to refine viewing options to incomplete projects or overdue projects.
        String userViewChoice = "";
        while (!userViewChoice.equals("back")) {
            System.out.println("""
                        Select an option:
                        incomplete - view all incomplete projects
                        overdue - view all overdue projects
                        back - go back""");
            Scanner updateChoice = new Scanner(System.in);
            userViewChoice = updateChoice.nextLine();

            switch (userViewChoice) {

                // If the user inputs 'incomplete', then a method called printIncompleteProjects() is called.
                case "incomplete":

                    printIncompleteProjects(projectList);

                    break;

                // If the user inputs 'overdue', then a method called printOverdueProjects() is called.
                case "overdue":

                    printOverdueProjects(projectList);

                    break;

                // If the user inputs 'back', then the switch-case breaks and the while loop exits.
                case "back":

                    break;

                // If the user inputs anything else, this message is shown and the while loop repeats.
                default:

                    System.out.println(INPUT_ERROR);
                    break;
            }
        }
    }

    /**
     * This method prints all projects that are incomplete.
     * @param projectList The list of all projects
     */
    private static void printIncompleteProjects(ArrayList<Project> projectList) {
        // An enhanced for loop runs over each item in the project list and prints only those where 'finalise' is not true.
        for (Project project : projectList) {

            if (!project.finalise) {
                System.out.println(project);
            }

        }
    }

    /**
     * This method prints all projects that are overdue.
     * @param projectList The list of all projects
     */
    private static void printOverdueProjects(ArrayList<Project> projectList) {

        // An enhanced for loop runs over each item in the project list and prints only those where 'finalise' is not true and the deadline is before the current date.
        for (Project project : projectList) {

            LocalDate projectDeadline = project.projectInfo.deadline;

            if (!project.finalise && projectDeadline.isBefore(LocalDate.now())) {
                System.out.println(project);
            }
        }
    }

    /**
     * This method allows the user to search for a Project using its project name or number.
     * @param listOfProjects the list of all projects
     * @return a Project object
     */
    private static Project searchForProject(ArrayList<Project> listOfProjects){

        // The user is asked to enter a project name or number, or to enter 'back' to exit the search function.
        Scanner userInput = new Scanner(System.in);
        System.out.println("Enter the name or number of the project you wish to update (Enter 'back' to exit): ");
        String userSearchChoice = userInput.nextLine();

        // The index of the project in the list is returned when using getProjectIndex() method - which has the project list and userChoice as arguments.
        int projectIndex = getProjectIndex(listOfProjects, userSearchChoice);

        /*
        If the project index returned is greater than -1, then the project is printed and the chosen project is returned.
        If the user inputs 'back', then null is returned. And if -1 is returned, then the user is told to try again and the method itself is returned.
         */
        if (projectIndex > -1) {

            Project projectToUpdate = listOfProjects.get(projectIndex);

            System.out.println(projectToUpdate);

            return projectToUpdate;

        } else if(userSearchChoice.equals("back")){

            return null;

        } else {

            System.out.println("Project does not exist. Try again.");
            return searchForProject(listOfProjects);

        }
    }

    /**
     * This method searches an array of projects to determine the index of the project that matches the search criteria.
     * The search criteria include a project number or project name.
     * @param listOfProjects all list of projects
     * @param userSearchChoice the project number or project name entered by the user
     * @return project index as an integer.
     */
    private static int getProjectIndex(ArrayList<Project> listOfProjects, String userSearchChoice){

        // A for loop runs over each item in the list of projects, checking the search criteria against each one.
        for(int i = 0; i < listOfProjects.size(); i++){

            // For each project, the string value of its project number and the project name are called and stored in variables.
            String projectNumberString = String.valueOf(listOfProjects.get(i).projectInfo.projectNumber);
            String projectNameString = listOfProjects.get(i).projectInfo.projectName;

            // If the projectDetail passed in this method is equal to the string of the project number or the project name, then the index of that project is returned.
            if(userSearchChoice.equals(projectNumberString) || userSearchChoice.equals(projectNameString)){

                return i;

            }
        }

        // If the project does not exist, then -1 is returned as the index.
        return -1;
    }

    /**
     * This method lets the user choose which aspect of a project they would like to update.
     * @param projectToUpdate the project that will be updated
     */
    private static void getUpdateOptions(Project projectToUpdate){

        // When getUpdateOptions is called, a menu is printed to allow the user to input what aspect of the project they would like to update.
        boolean exit = false;
        while (!exit) {
            System.out.println("""
                        Select an option:
                        project - update project info
                        architect - update architect info
                        contractor - update contractor info
                        customer - update customer info
                        f - finalise project
                        search - search for another project
                        back - go back""");
            Scanner updateChoice = new Scanner(System.in);
            String userUpdateChoice = updateChoice.nextLine();

            switch (userUpdateChoice) {

                // If the user inputs 'project', then updateProjectInfo() method is called with the project as its argument.
                case "project":

                    updateProjectInfo(projectToUpdate);

                    break;

                // If the user inputs 'architect', 'contractor' or 'customer', then the updatePerson() method is called with the project and person type as its argument.
                case ARCHITECT_STRING, CONTRACTOR_STRING, CUSTOMER_STRING:

                    updatePerson(projectToUpdate, userUpdateChoice);

                    break;

                case "f":

                    // If the project is already finalised, then this message will be printed.
                    if(projectToUpdate.finalise){

                        System.out.println("The project has already been finalised.");

                    }
                    // If the project has not been finalised, then it will be finalised.
                    else {

                        // The project's finalise attribute is set to true and the complete date is set to the current date.
                        projectToUpdate.setFinalise(true);
                        projectToUpdate.projectInfo.setCompleteDate(LocalDate.now());

                        /*
                        The project is added to the ArrayList 'newCompletedProjects'. This will be printed to a new file when the programme ends
                        - this is to make sure it matches the total list of projects in case an error occurs.
                         */
                        completedProjects.add(projectToUpdate);

                    }

                    // If the total paid is equal to the total fee, then an invoice is created and printed.
                    if (projectToUpdate.projectInfo.getTotalPaid() != projectToUpdate.projectInfo.getTotalFee()) {

                        System.out.println(projectToUpdate.createInvoice());

                    }
                    // If the total fee is equal to the total paid, then this message is displayed.
                    else {
                        System.out.println("The customer has already settled their account.");
                    }

                    // The while loop then exits.
                    exit = true;
                    break;

                // If the user inputs 'back', then the while loop exits.
                case "back":

                    exit = true;
                    break;

                // If the user inputs any other input, then this error runs and the method repeats.
                default:

                    System.out.println(INPUT_ERROR);
                    getUpdateOptions(projectToUpdate);

                    break;
            }
        }
    }

    /**
     * This method takes in a Project and updates the aspect of the project information that is also passed through the method.
     * @param projectToUpdate the project that will be updated
     */
    private static void updateProjectInfo(Project projectToUpdate){

        Scanner projectInfoChoice = new Scanner(System.in);
        boolean exit = false;
        while(!exit){

            System.out.println("""
                            What project information would you like to update?
                            pn - project name
                            bt - building type
                            a - address
                            erf - ERF Number
                            tf - Total Fee
                            d - deadline
                            tp - Total Paid
                            back - go back""");

            String projectAspect = projectInfoChoice.nextLine();

            // A try-catch block is run in case the user inputs invalid information.
            try{

                switch (projectAspect){

                    // Whichever aspect the user inputs, the relevant aspect will be updated. However, the project number, total owed, and the complete date cannot be updated.
                    case "pn":
                        System.out.println("Enter the new project name:");
                        String userUpdateChoice = projectInfoChoice.nextLine();

                        projectToUpdate.projectInfo.setProjectName(userUpdateChoice);

                        break;
                    case "bt":
                        System.out.println("Enter the new building type:");
                        userUpdateChoice = projectInfoChoice.nextLine();

                        projectToUpdate.projectInfo.setBuildingType(userUpdateChoice);

                        break;
                    case "a":
                        System.out.println("Enter the address:");
                        userUpdateChoice = projectInfoChoice.nextLine();

                        projectToUpdate.projectInfo.setAddress(userUpdateChoice);

                        break;

                    case "erf":
                        System.out.println("Enter the new ERF Number:");
                        int userUpdateChoice2 = projectInfoChoice.nextInt();

                        projectToUpdate.projectInfo.setErfNumber(userUpdateChoice2);

                        break;
                    case "tf":
                        System.out.println("Enter the new fee:");
                        double userUpdateChoice3 = projectInfoChoice.nextDouble();

                        projectToUpdate.projectInfo.setTotalFee(userUpdateChoice3);

                        break;

                    case "d":
                        System.out.println("The current due date is: " + projectToUpdate.projectInfo.getDeadline() + "\nEnter new due date (dd-mm-yyyy): ");
                        userUpdateChoice = projectInfoChoice.nextLine();

                        // The input is converted to a LocalDate variable and returned.
                        LocalDate newDeadlineDate = formatDate(userUpdateChoice);

                        projectToUpdate.projectInfo.setDeadline(newDeadlineDate);

                        break;
                    case "tp":
                        // When a new amount is paid, then the total paid is calculated by adding the new fee paid to the fee already paid.
                        System.out.println("The current amount paid out of R" + String.format("%.2f", projectToUpdate.projectInfo.getTotalFee())
                                + ": R" + String.format("%.2f", projectToUpdate.projectInfo.getTotalPaid()));

                        // The user inputs the amount to be paid.
                        System.out.println("Enter amount paid: ");
                        double newFeePaid = projectInfoChoice.nextDouble();

                        // The number inputted by the user is added to the amount already paid to give the total amount paid to-date.
                        double newTotalPaid = newFeePaid + projectToUpdate.projectInfo.getTotalPaid();

                        projectToUpdate.projectInfo.setTotalPaid(newTotalPaid);

                        break;
                    // If the user inputs 'back', then the switch-case is broken and the original menu is displayed.
                    case "back":

                        exit = true;

                        break;
                    // If any input is invalid, then this error will print.
                    default:

                        System.out.println(INPUT_ERROR);

                        break;
                }
            }
            // If the user inputs invalid information, then this error is printed and the method is run again.
            catch(Exception e){
                System.out.println(INPUT_ERROR);
                updateProjectInfo(projectToUpdate);
            }

            System.out.println(projectToUpdate);

        }
    }

    /**
     * In order to update a person's information, this method passes a project and the type of person to update.
     * @param projectToUpdate the project that will be updated
     * @param personType the type of person being updated - i.e. architect, contractor, customer.
     */
    private static void updatePerson(Project projectToUpdate, String personType){

        Scanner personInfoChoice = new Scanner(System.in);
        // Another menu is presented to the user to choose which information to update of the person.
        System.out.println("What " + personType + " information would you like to update?\n" +
                "all - update all " + personType + "'s information\n" +
                "name - update " + personType + "'s name\n" +
                "phone - update " + personType + "'s phone number\n" +
                "email - update " + personType + "'s email address\n" +
                "address - update " + personType + "'s address\n" +
                "back - go back");

        String updateAspect = personInfoChoice.nextLine();

        // A try-catch block is implemented in case of an input error.
        try{

            switch (updateAspect){
                // If the user selects all, then all the details of the person are requested for input.
                case "all":
                    System.out.println("Update " + personType + "'s Contact Details");

                    System.out.println(ENTER_COMMAND + "new " + personType + PERSON_NAME);
                    String newPersonName = personInfoChoice.nextLine();

                    System.out.println(ENTER_COMMAND + personType + PERSON_PHONE);
                    String newPersonPhone = personInfoChoice.nextLine();

                    System.out.println(ENTER_COMMAND + personType + PERSON_EMAIL);
                    String newPersonEmail = personInfoChoice.nextLine();

                    System.out.println(ENTER_COMMAND + personType + PERSON_ADDRESS);
                    String newPersonAddress = personInfoChoice.nextLine();

                    // A nested switch-case is run that will update all aspects for the type of person inputted.
                    switch (personType) {
                        case ARCHITECT_STRING -> {
                            projectToUpdate.getArchitect().setName(newPersonName);
                            projectToUpdate.getArchitect().setPhone(newPersonPhone);
                            projectToUpdate.getArchitect().setEmail(newPersonEmail);
                            projectToUpdate.getArchitect().setAddress(newPersonAddress);
                        }
                        case CONTRACTOR_STRING -> {
                            projectToUpdate.getContractor().setName(newPersonName);
                            projectToUpdate.getContractor().setPhone(newPersonPhone);
                            projectToUpdate.getContractor().setEmail(newPersonEmail);
                            projectToUpdate.getContractor().setAddress(newPersonAddress);
                        }
                        case CUSTOMER_STRING -> {
                            projectToUpdate.getCustomer().setName(newPersonName);
                            projectToUpdate.getCustomer().setPhone(newPersonPhone);
                            projectToUpdate.getCustomer().setEmail(newPersonEmail);
                            projectToUpdate.getCustomer().setAddress(newPersonAddress);
                        }
                        default -> System.out.println(INPUT_ERROR);
                    }

                    break;

                // The below cases update either the person's name, phone, email or address. A nested switch-case runs in each case to update the relevant person.
                case "name":

                    System.out.println(ENTER_COMMAND + "new " + personType + PERSON_NAME);
                    newPersonName = personInfoChoice.nextLine();

                    switch (personType) {
                        case ARCHITECT_STRING -> projectToUpdate.getArchitect().setName(newPersonName);
                        case CONTRACTOR_STRING -> projectToUpdate.getContractor().setName(newPersonName);
                        case CUSTOMER_STRING -> projectToUpdate.getCustomer().setName(newPersonName);
                        default -> System.out.println(INPUT_ERROR);
                    }

                    break;

                case "phone":

                    System.out.println(ENTER_COMMAND + personType + PERSON_PHONE);
                    newPersonPhone = personInfoChoice.nextLine();

                    switch (personType) {
                        case ARCHITECT_STRING -> projectToUpdate.getArchitect().setPhone(newPersonPhone);
                        case CONTRACTOR_STRING -> projectToUpdate.getContractor().setPhone(newPersonPhone);
                        case CUSTOMER_STRING -> projectToUpdate.getCustomer().setPhone(newPersonPhone);
                        default -> System.out.println(INPUT_ERROR);
                    }

                    break;

                case "email":

                    System.out.println(ENTER_COMMAND + personType + PERSON_EMAIL);
                    newPersonEmail = personInfoChoice.nextLine();

                    switch (personType) {
                        case ARCHITECT_STRING -> projectToUpdate.getArchitect().setEmail(newPersonEmail);
                        case CONTRACTOR_STRING -> projectToUpdate.getContractor().setEmail(newPersonEmail);
                        case CUSTOMER_STRING -> projectToUpdate.getCustomer().setEmail(newPersonEmail);
                        default -> System.out.println(INPUT_ERROR);
                    }

                    break;

                case "address":

                    System.out.println(ENTER_COMMAND + personType + PERSON_ADDRESS);
                    newPersonAddress = personInfoChoice.nextLine();

                    switch (personType) {
                        case ARCHITECT_STRING -> projectToUpdate.getArchitect().setAddress(newPersonAddress);
                        case CONTRACTOR_STRING -> projectToUpdate.getContractor().setAddress(newPersonAddress);
                        case CUSTOMER_STRING -> projectToUpdate.getCustomer().setAddress(newPersonAddress);
                        default -> System.out.println(INPUT_ERROR);
                    }

                    break;

                // If the user inputs 'back', then the main update menu is called again.
                case "back":

                    break;

                // If the user inputs anything else, then an input error is displayed and the method is called again.
                default:

                    System.out.println(INPUT_ERROR);
                    updatePerson(projectToUpdate, personType);
                    break;
            }
        }catch(Exception e){
            System.out.println(INPUT_ERROR);
        }

        System.out.println(projectToUpdate);

    }

    /**
     * This method writes project information to external files.
     * @param projectList the list of all projects
     * @param fileName name of the file to be written to
     */
    private static void writeProjectListToFile(ArrayList<Project> projectList, String fileName) {

        // A try-with-resources block is implemented to make sure the file closes, even if an IOException occurs.
        try(Formatter updatedProjectList = new Formatter(fileName)){

            // An enhanced for loop writes all the projects to the relevant file.
            for (Project project : projectList) {

                updatedProjectList.format(String.valueOf(project));

            }
        }
        // An IOException is declared in the catch component. If the file is not present, this error will run.
        catch(IOException e){

            System.out.println("File Not Found.");

        }
    }

    /**
     * This method takes a string that is set with rands (e.g. R20.00) and removes the 'R' and decimal point,
     * thus allowing it to be parsed into a double.
     * @param totalFeeString a string version of the total fee
     * @return a double of the total fee
     */
    private static double parseDouble(String totalFeeString) {

        totalFeeString = totalFeeString.substring(1, totalFeeString.length() - 3);

        return Double.parseDouble(totalFeeString);
    }

    /**
     * This method takes a string written date (which must follow the pattern dd-MM-yyyy), formats it and parses it into a LocalDate.
     * @param stringDate string of the date to be converted to LocalDate
     * @return a LocalDate is returned
     */
    public static LocalDate formatDate(String stringDate){

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        return LocalDate.parse(stringDate, dateFormatter);

    }
}

