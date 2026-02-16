package src;

/**
 * Test class to validate the account freezing and reactivation functionality.
 */
public class TestFreeze {
    public static void main(String[] args) {
        FileHandler fileHandler = new FileHandler();
        
        // Test 1: Create a test account
        System.out.println("=== Test 1: Creating a test checking account ===");
        long testId = 987654321L;
        fileHandler.createCheckingAccount("Alice", "Smith", testId, 5678, 2000.0, true);
        System.out.println("Account created with ID: " + testId);
        System.out.println();
        
        // Test 2: Freeze the account
        System.out.println("=== Test 2: Freezing the account ===");
        String reactivationPassword = fileHandler.freezeAccount(testId);
        if (reactivationPassword != null) {
            System.out.println("Account frozen successfully!");
            System.out.println("Reactivation password: " + reactivationPassword);
        } else {
            System.out.println("Failed to freeze account.");
        }
        System.out.println();
        
        // Test 3: Try to freeze again (should fail)
        System.out.println("=== Test 3: Attempting to freeze already frozen account ===");
        String secondPassword = fileHandler.freezeAccount(testId);
        if (secondPassword == null) {
            System.out.println("Correctly prevented freezing an already frozen account.");
        } else {
            System.out.println("ERROR: Should not have generated a new password.");
        }
        System.out.println();
        
        // Test 4: Try to reactivate with wrong password (should fail)
        System.out.println("=== Test 4: Attempting to reactivate with wrong password ===");
        boolean reactivated = fileHandler.reactivateAccount(testId, "wrongpassword123");
        if (!reactivated) {
            System.out.println("Correctly rejected wrong password.");
        } else {
            System.out.println("ERROR: Should not have reactivated with wrong password.");
        }
        System.out.println();
        
        // Test 5: Reactivate with correct password (should succeed)
        System.out.println("=== Test 5: Reactivating with correct password ===");
        reactivated = fileHandler.reactivateAccount(testId, reactivationPassword);
        if (reactivated) {
            System.out.println("Account reactivated successfully!");
        } else {
            System.out.println("Failed to reactivate account.");
        }
        System.out.println();
        
        // Test 6: Try to reactivate again (should fail as it's not frozen anymore)
        System.out.println("=== Test 6: Attempting to reactivate already active account ===");
        boolean secondReactivation = fileHandler.reactivateAccount(testId, reactivationPassword);
        if (!secondReactivation) {
            System.out.println("Correctly prevented reactivating an already active account.");
        } else {
            System.out.println("ERROR: Should not have reactivated an active account.");
        }
        System.out.println();
        
        System.out.println("=== All tests completed ===");
    }
}
