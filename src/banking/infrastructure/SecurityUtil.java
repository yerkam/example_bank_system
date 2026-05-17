package banking.infrastructure;

public class SecurityUtil {
/**
     * Hashes the given text using a secure hashing algorithm and returns the hashed value.
     * @param text The plain text to be hashed.
     * @return The hashed text as a string.
     */
    public static String hashText(String text){
        String hashedText = ""; // Placeholder for the actual hashing logic
        char[] chars = text.toCharArray();
        for (char c : chars) {
            int i = (int) c;
            i -= 15; // Simple encryption logic for demonstration purposes (not secure)
            hashedText += (char) i;
        }
        return hashedText;
    }

    /**
     * Verifies if the input password, when hashed, matches the stored hashed password.
     * @param inputPassword The plain text password entered by the user.
     * @param storedHashedPassword The hashed password stored in the system for comparison.
     * @return True if the hashed input password matches the stored hashed password, false otherwise.
     */
    public static boolean verifyPassword(String inputPassword, String storedHashedPassword) {
        String hashedInput = hashText(inputPassword);
        return hashedInput.equals(storedHashedPassword);
    }
}
