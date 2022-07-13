import java.text.SimpleDateFormat;
import java.util.Date;

// This class creates an object that contains information about the project.
public class ProjectInfo {

    // Attributes for ProjectInfo are declared.
    int projectNumber;
    String projectName;
    String buildingType;
    String address;
    int erfNumber;
    double totalFee;
    Date deadline;
    double totalPaid;
    Date completeDate;

    // The constructor takes in the relevant data that is inputted by the user.
    ProjectInfo(String projectName, String buildingType, String address, int erfNumber, double totalFee, Date deadline){

        this.projectNumber = 1;
        this.projectName = projectName;
        this.buildingType = buildingType;
        this.address = address;
        this.erfNumber = erfNumber;
        this.totalFee = totalFee;
        this.deadline = deadline;
        // this.totalPaid attribute is automatically set to 0.
        this.totalPaid = 0.00;
        // this.completeDate is automatically set to null.
        this.completeDate = null;

    }

    public void setProjectName(String newProjectName){

        projectName = newProjectName;

    }

    // Setters and getters are declared for relevant attributes that are used in methods.
    public void setDeadline(Date newDeadline){
        deadline = newDeadline;
    }

    public void setTotalPaid(double newTotalPaid){
        totalPaid = newTotalPaid;
    }

    // Various getters are set for each attribute, except for finalise and completeDate.
    public int getProjectNumber(){
        return projectNumber;
    }

    public String getProjectName(){
        return projectName;
    }

    public String getBuildingType(){
        return buildingType;
    }

    public String getAddress(){
        return address;
    }

    public int getErfNumber(){
        return erfNumber;
    }

    public double getTotalFee(){
        return totalFee;
    }

    public String getDeadline(){

        SimpleDateFormat deadlineFormat = new SimpleDateFormat("dd-MM-yyyy");

        return deadlineFormat.format(deadline);
    }

    public double getTotalPaid(){
        return totalPaid;
    }

    public String getCompleteDate(){

        /*
        The completeDate attribute is set to the current date, which is then formatted to a string.
        https://www.javatpoint.com/java-get-current-date
         */
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        this.completeDate = new Date();
        return formatter.format(completeDate);

    }

    // The toString() method returns a string with all the currently formatted information in an easy-to-read way.
    public String toString(){

        String output = "\nProject Information";
        output += "\nProject Number: " + getProjectNumber();
        output += "\nProject Name: " + getProjectName();
        output += "\nBuilding Type: " + getBuildingType();
        output += "\nBuilding Address: " + getAddress();
        output += "\nERF Number: " + getErfNumber();
        output += "\nTotal Fee: R" + String.format("%.2f", getTotalFee());
        output += "\nTotal Paid: R" + String.format("%.2f", getTotalPaid());
        output += "\nTotal Owed: R" + String.format("%.2f", (getTotalFee() - getTotalPaid()));
        output += "\nDeadline: " + getDeadline();

        return output;

    }

}
