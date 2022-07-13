import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JavaLangStringTest {
    // This test class tests the java.lang.String methods.
    @Test
    void testJavaLangString(){

        String testString = "This is a test string.";

        // This asserts that the string is 22 characters long.
        assertEquals(22, testString.length());

        // This asserts that 's' is the character at index 3.
        assertEquals('s', testString.charAt(3));

        // This asserts that "test" is the substring of the testString.
        assertEquals("test", testString.substring(10, 14));

        // This asserts that 8 is the index of 'a' in the testString.
        assertEquals(8, testString.indexOf('a'));

    }
}