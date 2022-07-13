import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PrimeNumberMethodTest {

    @Test
    void testPrime() {

        // This class test checks is the isPrime() method is working.


        // assertTrue is used with the isPrime() method that has a known prime number parsed through it.
        // If the method does not return true, then the test fails.
        assertTrue(isPrime(11));

        // assertFalse is used with the isPrime() method that has a known non-prime number parsed through it.
        // If the method does not return false, then the test fails.
        assertFalse(isPrime(22));



    }

    // This method was taken from Geeks for Geeks: https://www.geeksforgeeks.org/java-program-to-check-if-a-number-is-prime-or-not/
    public static boolean isPrime(int n) {

        // A prime number cannot be one or less.
        if (n <= 1)
            return false;

        // A for loop runs over every number less that the provided integer.
        for (int i = 2; i < n; i++) {
            // If the modulus the number divided by i is 0, then false is immediately returned.
            if (n % i == 0)
                return false;
        }

        // True is returned if the for loop completes and no number leaves a modulus of 0, thus proving it is prime.
        return true;
    }
}