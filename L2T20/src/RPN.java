import java.util.Scanner;
import java.util.logging.*;

// The class is made public to interact with StackNode.java.
public class RPN {

    // As System.out is considered not good practice, a logger is created for this class.
    static Logger logger = Logger.getLogger(RPN.class.getName());

    // The attributes are made final and placed above the class's methods. The attributes are also renamed to make them clearer.
    private final String inputEquation;
    private StackNode topNumber;


    public RPN(String inputEquation) {

        topNumber = null;
        this.inputEquation = inputEquation;

    }

    // This method was renamed to getAnswer() to be more clear and was placed higher in the class.
    public double getAnswer() {

        // a and b were renamed to number1 and number2. They are also now declared on two lines.
        double number1;
        double number2;

        // The integer declared was changed to index as index needs to exist outside the loop.
        int index;

        for (index = 0; index < inputEquation.length(); index++) {

            // If the character is a digit, then this if statement runs.
            if (Character.isDigit(inputEquation.charAt(index))) {

                /*
                A StringBuilder is declared rather than an empty string. temp is renamed to stringNumber.
                A method runs which returns the new index number. Within the function, stringNumber is updated.
                Convert stringNumber to double and added to the stack. (Double renamed to convertedNumber and method renamed to putOnStack()).
                 */
                StringBuilder stringNumber = new StringBuilder();
                index = getIndex(index, stringNumber);

                double convertedNumber = Double.parseDouble(stringNumber.toString());
                putOnStack(convertedNumber);

            }
            // The below else if statements conduct equations based on the operator inputted and the answer is added to the stack.
            else if (inputEquation.charAt(index) == '+') {

                number2 = extractNumbers();
                number1 = extractNumbers();

                putOnStack(number1 + number2);

            } else if (inputEquation.charAt(index) == '-') {

                number2 = extractNumbers();
                number1 = extractNumbers();

                putOnStack(number1 - number2);

            } else if (inputEquation.charAt(index) == '*') {

                number2 = extractNumbers();
                number1 = extractNumbers();

                putOnStack(number1 * number2);

            } else if (inputEquation.charAt(index) == '/') {

                number2 = extractNumbers();
                number1 = extractNumbers();

                putOnStack(number1 / number2);

            } else if (inputEquation.charAt(index) == '^') {

                number2 = extractNumbers();
                number1 = extractNumbers();

                putOnStack(Math.pow(number1, number2));

            }
            // If a non-recognised character, aside from a black space, is inputted, then this error is thrown.
            else if (inputEquation.charAt(index) != ' ') {

                throw new IllegalArgumentException();

            }
        }

        // val was changed to answer as it contains the result of the RPN. The answer is popped from the top of the stack
        double answer = extractNumbers();

        // If the topNumber in the stack is not null, then an exception will be thrown.
        if (topNumber != null) {
            throw new IllegalArgumentException();
        }

        // The answer is returned.
        return answer;
    }

    // This method was renamed to be more clear. And newData was changed from snake case.
    public void putOnStack(double newData) {

        topNumber = new StackNode(newData, topNumber);

    }

    // This method was renamed to be more clear, and topData was changed from snake case.
    public double extractNumbers() {

        double topData = topNumber.getData();

        topNumber = topNumber.getUnderneath();

        return topData;

    }

    // This method was extracted from the getAnswer() method as it was a nested for loop that could be its own method.
    private int getIndex(int i, StringBuilder stringNumber) {

        for (int j = 0; (j < 100) && (Character.isDigit(inputEquation.charAt(i)) || (inputEquation.charAt(i) == '.')); j++, i++) {

            stringNumber.append(inputEquation.charAt(i));

        }
        return i;
    }

    /*
    This is the main method. System.out in this method was changed to logger. As we have not studied this,
    and I struggled with this aspect a lot, I had to look up the solution. I am still unsure of how to get rid
    of all the superfluous information in the console.
    To find out more about loggers, I used both Loggly and Oracle:
    https://www.loggly.com/ultimate-guide/java-logging-basics/
    https://docs.oracle.com/en/java/javase/12/docs/api/java.logging/java/util/logging/Logger.html
    */
    public static void main(String[] args) {

        while (true) {

            Scanner userInput = new Scanner(System.in);

            logger.log(Level.INFO, "Enter RPN expression or \"quit\".");

            String equation = userInput.nextLine();

            if (equation.equals("quit")) {

                break;

            } else {

                RPN calculation = new RPN(equation);

                String output = String.format("Answer is %s %s", calculation.getAnswer(), "\n");

                logger.log(Level.INFO, output);

            }
        }
    }
}
