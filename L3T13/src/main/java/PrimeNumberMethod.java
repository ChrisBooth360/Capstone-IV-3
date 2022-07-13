import java.util.Scanner;

public class PrimeNumberMethod {

    public static void main(String[] args){

        Scanner input = new Scanner(System.in);

        System.out.println("Check if a number is prime. Input number: ");

        int numberToCheck = input.nextInt();

        boolean isPrime = isPrime(numberToCheck);

        if(isPrime){
            System.out.println(numberToCheck + " is prime.");
        } else {
            System.out.println(numberToCheck + " is not prime.");
        }
    }


    // This method was taken from Geeks for Geeks: https://www.geeksforgeeks.org/java-program-to-check-if-a-number-is-prime-or-not/
    public static boolean isPrime(int n){

        // A prime number cannot be one or less.
        if (n <= 1)
            return false;

        // A for loop runs over every number less that the provided integer.
        for (int i = 2; i < n; i++){
            // If the modulus the number divided by i is 0, then false is immediately returned.
            if (n % i == 0)
                return false;
        }

        // True is returned if the for loop completes and no number leaves a modulus of 0, thus proving it is prime.
        return true;
    }
}
