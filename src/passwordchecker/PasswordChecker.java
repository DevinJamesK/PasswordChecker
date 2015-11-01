/**
 * Devin Kloos 
 * Software Testing 
 * Project 01 
 * November 2nd, 2015
 */

// Remove this line to run the .java on its own without the other files.
package passwordchecker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class PasswordChecker {

    //Global Variables
    public static String lastPasswd = "";

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {

        //consoleTesting();
        
        fileTesting("passwords.txt");

    } // end of main --------------------------------------------------------

    public static String getInput(Scanner in) {
        System.out.println("Please enter a password: ");
        String passwd = in.nextLine();
        return passwd;
    }

    /**
     * Call this method when you want to run the program using consol input.
     */
    public static void consoleTesting() {
        Scanner in = new Scanner(System.in);
        String repeat = "y";

        while (repeat.charAt(0) == 'y') {
            String passwd = getInput(in);
            System.out.println();
            verifyPassword(passwd);
            System.out.println();
            System.out.println("Would you like to try again? (y/n) ");
            repeat = in.nextLine().toLowerCase();
            System.out.println();
        }
    }

    /**
     * Call this method when you want to run the program using file input.
     *
     * @param pathToFile
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void fileTesting(String pathToFile)
            throws FileNotFoundException, IOException {

        // Creating new file scanner
        File file = new File(pathToFile);
        Scanner in = new Scanner(file);

        for (int i = 1; in.hasNext(); i++) {
            // Get next password from file
            String passwd = in.nextLine();
            // Formating for readable output
            System.out.println();
            System.out.println("Test Case: " + i + " ");
            System.out.println("Input: " + passwd);

            // Checks to make sure inputed password meets requirements
            verifyPassword(passwd);
        }
    }

    /**
     * verifyPassword is the heavy lifting method of this program.
     * verifyPassword will check to if the inputed password meets the
     * requirements and if not tells the user why the enter password has failed.
     *
     * @param passwd
     */
    public static void verifyPassword(String passwd) {

        // Each variable checks if that requirment as been passed.
        boolean lenPass = checkLength(passwd);
        boolean allowedPass = checkAllowed(passwd);
        boolean numPass = checkNums(passwd);
        boolean specialPass = checkSpecials(passwd);
        boolean capPass = checkCaps(passwd);
        boolean lowerPass = checkLower(passwd);

        // Inilized true so when we don't check, it will pass (fist time though)
        boolean similarPass = true;

        // We don't want to check if the password is similar to a 
        // previous password if this is the first password entered.
        if (!"".equals(lastPasswd)) {

            /**
             * checkSimilar returns true if the passwords are too similar so we
             * have to not it with '!'. (If the passwords are NOT too similar
             * then that requirement PASSED)
             */
            similarPass = !checkSimilar(passwd);
        }
        
        // If all the requirments passed (equal true) then we accept the passwd
        if (allowedPass && lenPass && numPass
                && specialPass && capPass && lowerPass && similarPass) {
            System.out.println("Password Accepted!");
            lastPasswd = passwd;
        } 
        
        else {
            System.out.println("Password Rejected.");
            // If pass is false we tell the user why their password failed
            if (!allowedPass) {
                System.out.println("Please, only use numbers, letters, and "
                        + "accepted special characters: "
                        + "!@#$%&*(){}[]<>;:.,/|\\~?_-+=");
            }
            if (!lenPass) {
                System.out.println("Password must be atleast 9 characters and "
                        + "24 characters or less.");
            }
            if (!specialPass) {
                System.out.println("Password must contain at least two "
                        + "special characters: !@#$%&*(){}[]<>;:.,/|\\~?_-+=");
            }
            if (!numPass) {
                System.out.println("Password must contain at "
                        + "least two numbers.");
            }
            if (!capPass) {
                System.out.println("Password must contain at least two "
                        + "uppercase characters.");
            }
            if (!lowerPass) {
                System.out.println("Password must contain at least two "
                        + "lowercase characters.");
            }
            if (!similarPass) {
                System.out.println("Password too similar to last password.");
            }
        }
    }

    // password checks ------------------------------------------------------
    public static boolean checkLength(String passwd) {
        return (passwd.length() <= 24) && (passwd.length() >= 9);
    }

    /**
     * Checks every char of a given string to make sure that it is either a
     * number, special, capital, or lowercase char. If a space or any other char
     * that is not one of the accepted types is found this function will return
     * false.
     *
     * @param passwd
     * @return
     */
    public static boolean checkAllowed(String passwd) {
        boolean pass = true;
        char curr;
        for (int i = 0; i < passwd.length(); i++) {
            curr = passwd.charAt(i);
            if (isSpace(curr) || !isNumber(curr) && !isSpecial(curr)
                    && !isCapital(curr) && !isLower(curr)) {
                pass = false;
            }
        }
        return pass;
    }

    /**
     * Checks that there are two or more numbers in the given string and returns
     * true if there are. This is done by looping through each char in the the
     * string and counting up how many numbers are found.
     *
     * @param passwd
     * @return
     */
    public static boolean checkNums(String passwd) {
        int numCount = 0;
        for (int i = 0; i < passwd.length(); i++) {
            if (isNumber(passwd.charAt(i))) {
                numCount++;
            }
        }
        return (numCount >= 2);
    }

    public static boolean checkSpecials(String passwd) {
        int specialCount = 0;
        for (int i = 0; i < passwd.length(); i++) {
            if (isSpecial(passwd.charAt(i))) {
                specialCount++;
            }
        }
        return (specialCount >= 2);
    }

    public static boolean checkCaps(String passwd) {
        int capCount = 0;
        for (int i = 0; i < passwd.length(); i++) {
            if (isCapital(passwd.charAt(i))) {
                capCount++;
            }
        }
        return (capCount >= 2);
    }

    public static boolean checkLower(String passwd) {
        int lowerCount = 0;
        for (int i = 0; i < passwd.length(); i++) {
            if (isLower(passwd.charAt(i))) {
                lowerCount++;
            }
        }
        return (lowerCount >= 2);
    }

    /**
     * Checks if the inputed password is too similar to the last accepted
     * password and returns true if it is.
     *
     * @param passwd
     * @return
     */
    public static boolean checkSimilar(String passwd) {
        boolean similar = false;

        String prevPassword = lastPasswd.toLowerCase();
        String password = passwd.toLowerCase();
        String substr;
        String substrRev;
        String lastSubstr;
        String lastSubstrRev;

        for (int i = 0; i < passwd.length() - 5; i++) {
            substr = password.substring(i, i + 5);
            substrRev = reverseString(substr);

            for (int j = 0; j < prevPassword.length() - 5; j++) {
                lastSubstr = prevPassword.substring(j, j + 5);
                lastSubstrRev = reverseString(lastSubstr);

                if (substr.equals(lastSubstr) || substr.equals(lastSubstrRev)
                        || substrRev.equals(lastSubstr)
                        || substrRev.equals(lastSubstrRev)) {
                    similar = true;
                }
            }
        }
        return similar;
    }

    /**
     * Returns a reversed input string.
     *
     * @param str
     * @return
     */
    public static String reverseString(String str) {
        String reversed = "";
        for (int i = str.length() - 1; i >= 0; i--) {
            reversed += str.charAt(i);
        }
        return reversed;
    }

    // char checks -----------------------------------------------------------
    /**
     * Checks if a given char is a number by using ASCII comparisons. The '0'
     * char is evaluated by its ASCII number 48 and '9' has an ASCII number of
     * 57 so if the input char ASCII number is between 48 and 57 then it must be
     * a number and we return true.
     *
     * Google image search ASCII table for more info.
     *
     * @param c
     * @return
     */
    public static boolean isNumber(char c) {
        // checks using ascii vales
        return ((c >= '0') && (c <= '9'));
    }

    public static boolean isCapital(char c) {
        // checks using ascii vales
        return ((c >= 'A') && (c <= 'Z'));
    }

    public static boolean isLower(char c) {
        // checks using ascii vales
        return ((c >= 'a') && (c <= 'z'));
    }

    // Not needed because if its not one of the other checks its a non accepeted
    // char anyways.
    public static boolean isSpace(char c) {
        // checks using ascii vales
        return (c == ' ');
    }

    /**
     * Checks if a given char is a special char and returns true if it is.
     * Strings are actually character arrays and thus can be treated as such.
     * You can only compare two strings though so we must convert the char to a
     * string using Character.toString(c)
     *
     * @param c a character
     * @return
     */
    public static boolean isSpecial(char c) {
        String specialChars = "!@#$%&*(){}[]<>;:.,/|\\~?_-+=";
        return specialChars.contains(Character.toString(c));
    }
}
