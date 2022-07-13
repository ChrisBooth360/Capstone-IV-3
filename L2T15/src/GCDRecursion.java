import java.util.Scanner;

public class GCDRecursion {

    public static void main(String[] args){
        // The user is asked to enter two integers that they want to find the GCD of.
        System.out.println("Find the Greatest Common Divisor");

        Scanner userInput = new Scanner(System.in);

        System.out.println("Enter the first integer: ");
        int number1 = userInput.nextInt();

        System.out.println("Enter the second integer: ");
        int number2 = userInput.nextInt();

        // Both numbers inputted by the user are passed through the method getGCD().
        int GCD = getGCD(number1, number2);

        // The GCD is printed.
        System.out.println("The GCD of " + number1 + " and " + number2 + " is: " + GCD);

    }
    // This recursive method determines the GCD of two numbers that are passed as its argument.
    public static int getGCD(int num1, int num2){

        // If the second number equals zero, then the first number is returned.
        if(num2 == 0){
            return num1;
        }
        /*
        If the second number is greater than 0, then it is temporarily stored in gcd.
        The modulus of num1 and num2 is found and stored in num2. num1 becomes the number stored in gcd, and
        the method is called again with the new num1 and the new num2.
         */
        else{

            int gcd = num2;
            num2 = num1 % num2;
            num1 = gcd;

            return getGCD(num1, num2);

        }
    }
}
