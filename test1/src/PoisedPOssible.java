import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Scanner;

public class PoisedPOssible {

    /**
     * This program is a project management system for a small structural engineering firm called “Poised”.
     * Poised does the engineering needed to ensure the structural integrity of various buildings.
     * <p>
     * The program allows users to view, create, and update various projects.
     * @author Chris Booth
     * @version 1.0
     */

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;

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

        public static void main(String [] args){

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

                        ProjectInfo newProjectInfo = inputNewProjectInfo();
                        Person newArchitect = inputNewPersonInfo(ARCHITECT_STRING);
                        Person newContractor = inputNewPersonInfo(CONTRACTOR_STRING);
                        Person newCustomer = inputNewPersonInfo(CUSTOMER_STRING);
                        Person newEngineer = inputNewPersonInfo(ENGINEER_STRING);
                        Person newManager = inputNewPersonInfo(MANAGER_STRING);

                        Project newProject = new Project(newProjectInfo, newArchitect, newContractor, newCustomer, newEngineer, newManager);

                        // The Project object is printed out and added to the array of Project items.
                        System.out.println(newProject);

                        addProject(newProject);
                    }

                /*
                If the user inputs 'view', then an enhanced for loop prints out each of the projects in the project list
                and a method called getViewOptions() is called with the list of projects passed as its argument.
                */
                    case "view" -> {

                        printAllProjects();

                        getViewOptions();

                    }

                    //If the user inputs 'search', a method is called that will find and return the desired project, before giving a list of update options.
                    case "search" -> {

                        System.out.println("Enter the name or number of the project you wish to update (Enter 'back' to exit): ");
                        String userSearchChoice = userInput.nextLine();

                        searchToUpdate(userSearchChoice);

                    }

                /*
                If the user inputs 'exit', then each project in projectList is written to a file, each project in completedProjects is written to another file,
                'Goodbye' is printed and the while loop exits, ending the program.
                 */
                    case "exit" -> System.out.println("Goodbye!");

                    // If the user inputs anything else, this message is shown and the while loop repeats.
                    default -> System.out.println(INPUT_ERROR);
                }
            }
        }

        /**
         * This method gathers input from the user in order to create a new ProjectInfo object.
         * An array is passed through the method in order to help create an appropriate Project Number.
         * @return new ProjectInfo object.
         */
        private static ProjectInfo inputNewProjectInfo(){

            Scanner projectInfo = new Scanner(System.in);

            // A try-catch block is executed in order to make sure the user inputs the correct input.
            try{

                Connection connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/poisedpms?useSSL=false",
                        "admin",
                        "adm1n"
                );

                Statement statement = connection.createStatement();

                System.out.println("Enter the project name (Optional): ");
                String projectName = projectInfo.nextLine();

                ResultSet projects = statement.executeQuery("SELECT * FROM project_info WHERE '"+ projectName +"'IN (proj_name)");

                // https://stackoverflow.com/questions/867194/java-resultset-how-to-check-if-there-are-any-results
                if(projects.isBeforeFirst()){

                    System.out.println("This project name already exists. Try again.");
                    return inputNewProjectInfo();

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
                    return inputNewProjectInfo();

                }

                // If any input information is empty (aside from the optional project name), then this error is printed and the method is called again, thus repeating it.
                if(building.isEmpty() || buildingAddress.isEmpty() || erf == 0 || fee == 0 || deadline.isEmpty()){

                    System.out.println("Make sure you input all relevant information. Try Again.");
                    return inputNewProjectInfo();

                }

                // A new ProjectInfo object is created and its project number is set to the length of the list of projects plus 1.
                ProjectInfo newProjectInfo = new ProjectInfo(projectName, building, buildingAddress, erf, fee, deadlineDate);

                int projectNumber = 0;

                projects = statement.executeQuery("SELECT proj_num FROM project_info ORDER BY proj_num DESC LIMIT 1");

                while (projects.next()){
                    projectNumber =projects.getInt("proj_num");
                }

                newProjectInfo.setProjectNumber(projectNumber + 1);

                projects.close();
                statement.close();
                connection.close();

                // If all the input is valid, then the new object is returned.
                return newProjectInfo;

            } catch(Exception e){

                // If the user inputs anything invalid, then an error is printed and the method is called again, thus repeating it.
                System.out.println(INPUT_ERROR);
                e.printStackTrace();
                return inputNewProjectInfo();


            }
        }

        private static Project createProject(ResultSet projectInfoToCreate){

            // A try-catch block is executed in order to make sure the user inputs the correct input.
            try{

                String projectName = projectInfoToCreate.getString("proj_name");
                String building = projectInfoToCreate.getString("build_type");
                String buildingAddress = projectInfoToCreate.getString("build_address");
                int erf = projectInfoToCreate.getInt("erf_num");
                double fee = projectInfoToCreate.getDouble("total_fee");
                LocalDate deadlineDate = formatDate(projectInfoToCreate.getString("deadline"));

                // A new ProjectInfo object is created and its project number is set to the length of the list of projects plus 1.
                ProjectInfo currentProjectInfo = new ProjectInfo(projectName, building, buildingAddress, erf, fee, deadlineDate);

                currentProjectInfo.setProjectNumber(projectInfoToCreate.getInt("project_info.proj_num"));
                currentProjectInfo.setTotalPaid(projectInfoToCreate.getDouble("total_paid"));
                currentProjectInfo.getTotalOwed();

                String architectName = projectInfoToCreate.getString("arch_name");
                String architectPhone = projectInfoToCreate.getString("arch_tele");
                String architectEmail = projectInfoToCreate.getString("arch_email");
                String architectAddress = projectInfoToCreate.getString("arch_address");

                Person Architect = new Person(Person.Type.ARCHITECT, architectName, architectPhone, architectEmail, architectAddress);

                String contractorName = projectInfoToCreate.getString("cont_name");
                String contractorPhone = projectInfoToCreate.getString("cont_tele");
                String contractorEmail = projectInfoToCreate.getString("cont_email");
                String contractorAddress = projectInfoToCreate.getString("cont_address");

                Person Contractor = new Person(Person.Type.CONTRACTOR, contractorName, contractorPhone, contractorEmail, contractorAddress);

                String customerName = projectInfoToCreate.getString("cust_name");
                String customerPhone = projectInfoToCreate.getString("cust_tele");
                String customerEmail = projectInfoToCreate.getString("cust_email");
                String customerAddress = projectInfoToCreate.getString("cust_address");

                Person Customer = new Person(Person.Type.CUSTOMER, customerName, customerPhone, customerEmail, customerAddress);

                String engineerName = projectInfoToCreate.getString("engi_name");
                String engineerPhone = projectInfoToCreate.getString("engi_tele");
                String engineerEmail = projectInfoToCreate.getString("engi_email");
                String engineerAddress = projectInfoToCreate.getString("engi_address");

                Person Engineer = new Person(Person.Type.ENGINEER, engineerName, engineerPhone, engineerEmail, engineerAddress);

                String managerName = projectInfoToCreate.getString("pm_name");
                String managerPhone = projectInfoToCreate.getString("pm_tele");
                String managerEmail = projectInfoToCreate.getString("pm_email");
                String managerAddress = projectInfoToCreate.getString("pm_address");

                Person Manager = new Person(Person.Type.MANAGER, managerName, managerPhone, managerEmail, managerAddress);

                Project currentProject = new Project(currentProjectInfo, Architect, Contractor, Customer, Engineer, Manager);

                if(projectInfoToCreate.getString("finalised").equals("Y")){

                    currentProject.setFinalise(true);

                }

                return currentProject;


            } catch(Exception e){

                // If the user inputs anything invalid, then an error is printed and the method is called again, thus repeating it.
                System.out.println(INPUT_ERROR);
                e.printStackTrace();
                return createProject(projectInfoToCreate);


            }
        }

        /**
         * This method gathers input from the user in order to create a new Person object.
         * A String of the Person Type is passed through the method in order to determine which person's information is being inputted.
         * @param personType The type of person for the Person object
         * @return new Person object.
         */
        private static Person inputNewPersonInfo(String personType){

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
                    return inputNewPersonInfo(personType);

                }

                // An enhanced switch is used to create the Person object with the appropriate person type.
                return switch (personType) {
                    case ARCHITECT_STRING -> new Person(Person.Type.ARCHITECT, personName, personPhone, personEmail, personAddress);
                    case CONTRACTOR_STRING -> new Person(Person.Type.CONTRACTOR, personName, personPhone, personEmail, personAddress);
                    case CUSTOMER_STRING -> new Person(Person.Type.CUSTOMER, personName, personPhone, personEmail, personAddress);
                    case ENGINEER_STRING -> new Person(Person.Type.ENGINEER, personName, personPhone, personEmail, personAddress);
                    case MANAGER_STRING -> new Person(Person.Type.MANAGER, personName, personPhone, personEmail, personAddress);
                    default -> null;
                };

            } catch(Exception e){
                // If the user inputs anything invalid, then an error is printed and the method is called again, thus repeating it.
                System.out.println(INPUT_ERROR);
                return inputNewPersonInfo(personType);
            }
        }

        /**
         * This method allows the user to further refine the projects they would like to view.
         * It allows them to view incomplete projects and overdue projects.
         */
        private static void getViewOptions(){

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

                        printIncompleteOverdue("incomplete");

                        break;

                    // If the user inputs 'overdue', then a method called printOverdueProjects() is called.
                    case "overdue":

                        printIncompleteOverdue("overdue");

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
         * This method allows the user to search for a Project using its project name or number.
         */
        private static void searchToUpdate(String searchString){

            // The user is asked to enter a project name or number, or to enter 'back' to exit the search function.

            try{

                Connection connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/poisedpms?useSSL=false",
                        "admin",
                        "adm1n"
                );

                Statement statement = connection.createStatement();

                ResultSet countRows;

                int rowsCounted;

                if(isNumeric(searchString)){

                    int projID = Integer.parseInt(searchString);

                    countRows = statement.executeQuery("SELECT COUNT(proj_num) FROM project_info WHERE proj_num= '" + projID + "'");

                } else {

                    countRows = statement.executeQuery("SELECT COUNT(proj_num) FROM project_info WHERE proj_num= '" + searchString + "'");

                }
                countRows.next();
                rowsCounted = countRows.getInt(1);
                System.out.println("Number of records: "+rowsCounted);

                countRows.close();

                ResultSet selectedRows;

                int projID;

                if(isNumeric(searchString)){

                    projID = Integer.parseInt(searchString);

                    selectedRows = statement.executeQuery(JOIN_TABLES + " WHERE project_info.proj_num = '" + projID + "'");

                } else {

                    selectedRows = statement.executeQuery(JOIN_TABLES + " WHERE proj_name = '" + searchString + "'");

                    projID = selectedRows.getInt("proj_num");

                }

                if (rowsCounted == 0) {

                    System.out.println("No project found. Try again");

                } else if (rowsCounted == 1){

                    printProjectStrings(selectedRows);

                    Project currentProject = createProject(selectedRows);

                    getUpdateOptions(currentProject);

                } else{

                    printProjectStrings(selectedRows);

                    System.out.println("Multiple projects found. Please refine your search with the appropriate project number.");
                }

                selectedRows.close();
                statement.close();
                connection.close();

            } catch(Exception e){

                // If the user inputs anything invalid, then an error is printed and the method is called again, thus repeating it.
                e.printStackTrace();

            }
        }

        private static boolean isNumeric(String searchString) {

            if(searchString == null || searchString.equals("")) {
                System.out.println("No input detected.");
                return false;
            }

            try {
                int intValue = Integer.parseInt(searchString);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }

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

        private static void finalise(int projectToUpdateID) throws SQLException {

            try{

                Connection connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/poisedpms?useSSL=false",
                        "admin",
                        "adm1n"
                );

                Statement statement = connection.createStatement();

                ResultSet selectedRows = statement.executeQuery("SELECT * FROM project_info INNER JOIN pay_complete " +
                        "ON project_info.proj_num = pay_complete.proj_num WHERE proj_num = '" + projectToUpdateID + "'");

                if(selectedRows.getString("finalise").equals("Y")){

                    System.out.println("The project has already been finalised.");

                }
                // If the project has not been finalised, then it will be finalised.
                else {

                    statement.executeUpdate("UPDATE pay_complete SET finalised = 'Y', '" + new java.util.Date() + "' WHERE proj_num = " + projectToUpdateID + "");

                }

                // If the total paid is equal to the total fee, then an invoice is created and printed.
                if (selectedRows.getDouble("total_fee") != selectedRows.getDouble("total_paid")) {

                    System.out.println("\nCustomer Invoice\n" + selectedRows.getString("customer")
                            + "Complete Date: " + selectedRows.getDate("complete_date")
                            + "Amount owed: R" + selectedRows.getDouble("total_owed") + "\n");

                }
                // If the total fee is equal to the total paid, then this message is displayed.
                else {
                    System.out.println("The customer has already settled their account.");
                }

                selectedRows.close();
                statement.close();
                connection.close();

            } catch(Exception e){

                // If the user inputs anything invalid, then an error is printed and the method is called again, thus repeating it.
                e.printStackTrace();

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
         * This method takes a string written date (which must follow the pattern dd-MM-yyyy), formats it and parses it into a LocalDate.
         * @param stringDate string of the date to be converted to LocalDate
         * @return a LocalDate is returned
         */
        public static LocalDate formatDate(String stringDate){

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            return LocalDate.parse(stringDate, dateFormatter);

        }

        private static void addProject(Project newProject){

            try{


                Connection connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/poisedpms?useSSL=false",
                        "admin",
                        "adm1n"
                );

                Statement statement = connection.createStatement();

                statement.executeUpdate("INSERT INTO pay_complete (proj_num, total_fee, total_owed, deadline) VALUES ("
                        + newProject.projectInfo.getProjectNumber() + ", "
                        + newProject.projectInfo.getTotalFee() + ", "
                        + newProject.projectInfo.getTotalOwed() + ", '"
                        + newProject.projectInfo.getDeadline() + "')");

                statement.executeUpdate("INSERT INTO build_info VALUES ("
                        + newProject.projectInfo.getErfNumber() + ", '"
                        + newProject.projectInfo.getBuildingType() + "', '"
                        + newProject.projectInfo.getAddress() + "')");

                statement.executeUpdate("INSERT INTO architect VALUES ('"
                        + newProject.architect.getName() + "', '"
                        + newProject.architect.getPhone() + "', '"
                        + newProject.architect.getEmail() + "', '"
                        + newProject.architect.getAddress() + "')");

                statement.executeUpdate("INSERT INTO contractor VALUES ('"
                        + newProject.contractor.getName() + "', '"
                        + newProject.contractor.getPhone() + "', '"
                        + newProject.contractor.getEmail() + "', '"
                        + newProject.contractor.getAddress() + "')");

                statement.executeUpdate("INSERT INTO customer VALUES ('"
                        + newProject.customer.getName() + "', '"
                        + newProject.customer.getPhone() + "', '"
                        + newProject.customer.getEmail() + "', '"
                        + newProject.customer.getAddress() + "')");

                statement.executeUpdate("INSERT INTO engineer VALUES ('"
                        + newProject.engineer.getName() + "', '"
                        + newProject.engineer.getPhone() + "', '"
                        + newProject.engineer.getEmail() + "', '"
                        + newProject.engineer.getAddress() + "')");

                statement.executeUpdate("INSERT INTO project_manager VALUES ('"
                        + newProject.manager.getName() + "', '"
                        + newProject.manager.getPhone() + "', '"
                        + newProject.manager.getEmail() + "', '"
                        + newProject.manager.getAddress() + "')");

                statement.executeUpdate("INSERT INTO project_info VALUES ("
                        + newProject.projectInfo.getProjectNumber() + ", '"
                        + newProject.projectInfo.getProjectName() + "', "
                        + newProject.projectInfo.getErfNumber() + ", '"
                        + newProject.architect.getName() + "', '"
                        + newProject.contractor.getName() + "', '"
                        + newProject.customer.getName() + "', '"
                        + newProject.engineer.getName() + "', '"
                        + newProject.manager.getName() + "')");

                statement.close();
                connection.close();

            } catch(Exception e){

                // If the user inputs anything invalid, then an error is printed and the method is called again, thus repeating it.
                System.out.println(INPUT_ERROR);

            }
        }

        private static void printAllProjects(){

            try{

                Connection connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/poisedpms?useSSL=false",
                        "admin",
                        "adm1n"
                );

                Statement statement = connection.createStatement();

                ResultSet projectInfo = statement.executeQuery(JOIN_TABLES);

                printProjectStrings(projectInfo);

                projectInfo.close();
                statement.close();
                connection.close();

            } catch(Exception e){

                // If the user inputs anything invalid, then an error is printed and the method is called again, thus repeating it.
                e.printStackTrace();

            }
        }

        private static void printIncompleteOverdue(String typeToPrint){

            try{

                Connection connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/poisedpms?useSSL=false",
                        "admin",
                        "adm1n"
                );

                Statement statement = connection.createStatement();

                ResultSet selectedRows;

                if(typeToPrint.equals("overdue")){

                    java.util.Date today = new java.util.Date();

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

                    String dateToday = formatter.format(today);

                    selectedRows = statement.executeQuery(JOIN_TABLES + " WHERE pay_complete.deadline < '" + dateToday + "'");



                } else {

                    selectedRows = statement.executeQuery(JOIN_TABLES + " WHERE finalised='N'");

                }

                if (!selectedRows.isBeforeFirst() && typeToPrint.equals("overdue")) {

                    System.out.println("No overdue projects found.");

                } else if(!selectedRows.isBeforeFirst() && typeToPrint.equals("incomplete")) {

                    System.out.println("No incomplete projects found.");

                }else{

                    printProjectStrings(selectedRows);

                }

                selectedRows.close();
                statement.close();
                connection.close();

            } catch(Exception e){

                // If the user inputs anything invalid, then an error is printed and the method is called again, thus repeating it.
                e.printStackTrace();

            }
        }

        private static void printProjectStrings(ResultSet projectInfo) throws SQLException {
            while (projectInfo.next()) {

                String complete = "Status: Incomplete";

                if(projectInfo.getString("finalised").equals("Y")){

                    Date completeDate = projectInfo.getDate("complete_date");

                    complete = "\nComplete Date: " + completeDate;

                }

                System.out.println("Project Number: " + projectInfo.getInt("project_info.proj_num") +
                        "\nProject Name: " + projectInfo.getString("proj_name") +
                        "\nBuilding Type: " + projectInfo.getString("build_type") +
                        "\nBuilding Address: " + projectInfo.getString("build_address") +
                        "\nERF Number: " + projectInfo.getInt("erf_num") +
                        "\nTotal Fee: R" + projectInfo.getDouble("total_fee") +
                        "\nTotal Paid: R" + projectInfo.getDouble("total_paid") +
                        "\nTotal Owed: R" + projectInfo.getDouble("total_owed") +
                        "\nDeadline: " + projectInfo.getDate("deadline") +
                        "\n" + complete + "\n" +
                        "\nArchitect:" +
                        "\nName: " + projectInfo.getString("arch_name") +
                        "\nTelephone number: " + projectInfo.getString("arch_tele") +
                        "\nEmail: " + projectInfo.getString("arch_email") +
                        "\nAddress: " + projectInfo.getString("arch_address") + "\n" +
                        "\nContractor:" +
                        "\nName: " + projectInfo.getString("cont_name") +
                        "\nTelephone number: " + projectInfo.getString("cont_tele") +
                        "\nEmail: " + projectInfo.getString("cont_email") +
                        "\nAddress: " + projectInfo.getString("cont_address") + "\n" +
                        "\nCustomer:" +
                        "\nName: " + projectInfo.getString("cust_name") +
                        "\nTelephone number: " + projectInfo.getString("cust_tele") +
                        "\nEmail: " + projectInfo.getString("cust_email") +
                        "\nAddress: " + projectInfo.getString("cust_address") + "\n" +
                        "\nStructural Engineer:" +
                        "\nName: " + projectInfo.getString("engi_name") +
                        "\nTelephone number: " + projectInfo.getString("engi_tele") +
                        "\nEmail: " + projectInfo.getString("engi_email") +
                        "\nAddress: " + projectInfo.getString("engi_address") + "\n" +
                        "\nProject Manager:" +
                        "\nName: " + projectInfo.getString("pm_name") +
                        "\nTelephone number: " + projectInfo.getString("pm_tele") +
                        "\nEmail: " + projectInfo.getString("pm_email") +
                        "\nAddress: " + projectInfo.getString("pm_address") + "\n"
                );
            }
        }
    }



}
