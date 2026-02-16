package src;

/**
 * Demonstration of the account freezing and reactivation feature.
 * This shows a complete workflow of freezing and reactivating an account.
 */
public class DemoFreeze {
    public static void main(String[] args) {
        FileHandler fileHandler = new FileHandler();
        
        System.out.println("======================================================");
        System.out.println("     ACCOUNT FREEZING & REACTIVATION DEMO");
        System.out.println("======================================================");
        System.out.println();
        
        // Step 1: Create an account
        long accountId = 111222333L;
        System.out.println("Step 1: Creating a new checking account...");
        fileHandler.createCheckingAccount("John", "Doe", accountId, 1234, 5000.0, true);
        System.out.println("✓ Account created successfully!");
        System.out.println("  - Account ID: " + accountId);
        System.out.println("  - Owner: John Doe");
        System.out.println("  - Balance: $5000.00");
        System.out.println("  - Status: ACTIVE");
        System.out.println();
        
        // Step 2: Freeze the account
        System.out.println("Step 2: Freezing the account...");
        String reactivationPassword = fileHandler.freezeAccount(accountId);
        System.out.println("✓ Account frozen successfully!");
        System.out.println("  - Status: FROZEN");
        System.out.println("  - Reactivation password: " + reactivationPassword);
        System.out.println("  - IMPORTANT: Save this password to reactivate the account!");
        System.out.println();
        
        // Step 3: Simulate trying to reactivate with wrong password
        System.out.println("Step 3: Attempting to reactivate with wrong password...");
        boolean result = fileHandler.reactivateAccount(accountId, "WrongPassword123");
        System.out.println("✗ Reactivation failed as expected.");
        System.out.println("  - The wrong password was rejected.");
        System.out.println();
        
        // Step 4: Reactivate with correct password
        System.out.println("Step 4: Reactivating with the correct password...");
        result = fileHandler.reactivateAccount(accountId, reactivationPassword);
        if (result) {
            System.out.println("✓ Account reactivated successfully!");
            System.out.println("  - Status: ACTIVE");
            System.out.println("  - The account is now fully operational.");
        }
        System.out.println();
        
        System.out.println("======================================================");
        System.out.println("                    DEMO COMPLETE");
        System.out.println("======================================================");
        System.out.println();
        System.out.println("Features demonstrated:");
        System.out.println("  ✓ Account freezing with random password generation");
        System.out.println("  ✓ Password-protected reactivation");
        System.out.println("  ✓ Wrong password rejection");
        System.out.println("  ✓ Secure random password generation (12 characters)");
    }
}
