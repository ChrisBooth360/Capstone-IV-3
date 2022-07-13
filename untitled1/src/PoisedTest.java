import java.text.SimpleDateFormat;
import java.util.Date;

public class PoisedTest {

    case "date":

    Date newDeadlineDate = updateDeadline(newProject);

                    newProject.projectInfo.setDeadline(newDeadlineDate);
                    System.out.println(newProject);     // The updated project is printed.
                    break;

    // If the user inputs 'fee', then the amount paid is determined using the updateFeePaid() method, and the total paid is set.
                case "fee":

    double finalFeePaid = updateFeePaid(newProject);

                    newProject.projectInfo.setTotalPaid(finalFeePaid);
                    System.out.println(newProject);     // The updated project is printed.
                    break;

    // If the user inputs 'contact', then the updateContractor method is called, and the updated project is printed.
                case "contact":

    updateContractor(newProject);

                    System.out.println(newProject);
                    break;

                /*
                 If the user inputs 'finalise', then a nested if statement checks whether the total paid is equal to the total fee.
                 If so, then the user is told the account is settled. If not, then the createInvoice() method in the Project class is run.
                 */
                case "finalise":

                        if (newProject.projectInfo.getTotalPaid() == newProject.projectInfo.getTotalFee()) {
        System.out.println("The customer has already settled their account.");
    } else {
        System.out.println(newProject.createInvoice());
    }
                    break;

}



                        System.out.println(capturedProject);