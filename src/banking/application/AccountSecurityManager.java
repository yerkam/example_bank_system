package banking.application;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;

import banking.infrastructure.FileHandler;

/**
 * Manages account security operations such as freezing and reactivating checking accounts.
 * Works with per-user account files in the Accounts folder.
 */
public class AccountSecurityManager {
    static FileHandler fileHandler = new FileHandler();
    private final static String accountsFolderPath = fileHandler.getAccountsFolderPath();
    private final static String frozenAccountsFile = fileHandler.getFrozenAccountsFile();

    /**
     * Generates a random alphanumeric password for account reactivation.
     * @return A random alphanumeric password string of 12 characters.
     */
    private static String generateReactivationPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            int index = secureRandom.nextInt(characters.length());
            password.append(characters.charAt(index));
        }
        return password.toString();
    }


    /**
     * Freezes a checking account by setting its status to false.
     * Generates a reactivation password stored in FrozenAccounts.txt.
     *
     * @param userId        The user's ID.
     * @param accountNumber The checking account number to freeze.
     * @return The reactivation password, or null if the operation failed.
     */
    public static String freezeAccount(long userId, int accountNumber) {
        try {
            String filePath = accountsFolderPath + File.separator + userId + ".txt";
            Path userPath = Paths.get(filePath);
            if (!Files.exists(userPath)) {
                System.out.println("User file does not exist.");
                return null;
            }

            java.util.List<String> lines = Files.readAllLines(userPath);
            boolean accountFound = false;

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("#");
                if (parts[0].equals("Checking") && parts.length >= 5) {
                    int storedAccountNumber = Integer.parseInt(parts[1]);
                    if (storedAccountNumber == accountNumber) {
                        boolean currentStatus = Boolean.parseBoolean(parts[3]);
                        if (!currentStatus) {
                            System.out.println("Account is already frozen.");
                            return null;
                        }
                        parts[3] = "false";
                        lines.set(i, String.join("#", parts));
                        accountFound = true;
                        break;
                    }
                }
            }

            if (!accountFound) {
                System.out.println("Checking account #" + accountNumber + " not found for user " + userId + ".");
                return null;
            }

            Files.write(userPath, lines);

            String reactivationPassword = generateReactivationPassword();
            try (FileWriter writer = new FileWriter(frozenAccountsFile, true)) {
                writer.write(userId + "#" + accountNumber + "#" + reactivationPassword + "\n");
            }

            return reactivationPassword;

        } catch (Exception e) {
            System.out.println("An error occurred while freezing the account: " + e.getMessage());
            return null;
        }
    }


    /**
     * Reactivates a frozen checking account if the provided password matches.
     *
     * @param userId        The user's ID.
     * @param accountNumber The checking account number to reactivate.
     * @param password      The reactivation password.
     * @return true if reactivated successfully, false otherwise.
     */
    public boolean reactivateAccount(long userId, int accountNumber, String password) {
        try {
            Path frozenPath = Paths.get(frozenAccountsFile);
            if (!Files.exists(frozenPath)) {
                System.out.println("The frozen accounts file does not exist.");
                return false;
            }

            java.util.List<String> frozenLines = Files.readAllLines(frozenPath);
            int matchingLineIndex = -1;

            for (int i = 0; i < frozenLines.size(); i++) {
                String line = frozenLines.get(i).trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("#");
                if (parts.length >= 3) {
                    long storedUserId = Long.parseLong(parts[0]);
                    int storedAccountNumber = Integer.parseInt(parts[1]);
                    String storedPassword = parts[2];
                    if (storedUserId == userId && storedAccountNumber == accountNumber && storedPassword.equals(password)) {
                        matchingLineIndex = i;
                        break;
                    }
                }
            }

            if (matchingLineIndex == -1) {
                System.out.println("Invalid reactivation password or account not found in frozen accounts.");
                return false;
            }

            String filePath = accountsFolderPath + File.separator + userId + ".txt";
            Path userPath = Paths.get(filePath);
            if (!Files.exists(userPath)) {
                System.out.println("User file does not exist.");
                return false;
            }

            java.util.List<String> userLines = Files.readAllLines(userPath);
            boolean accountUpdated = false;

            for (int i = 0; i < userLines.size(); i++) {
                String line = userLines.get(i).trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("#");
                if (parts[0].equals("Checking") && parts.length >= 5) {
                    int storedAccountNumber = Integer.parseInt(parts[1]);
                    if (storedAccountNumber == accountNumber) {
                        parts[3] = "true";
                        userLines.set(i, String.join("#", parts));
                        accountUpdated = true;
                        break;
                    }
                }
            }

            if (!accountUpdated) {
                System.out.println("Checking account #" + accountNumber + " not found in user file.");
                return false;
            }

            Files.write(userPath, userLines);
            frozenLines.remove(matchingLineIndex);
            Files.write(frozenPath, frozenLines);

            return true;

        } catch (Exception e) {
            System.out.println("An error occurred while reactivating the account: " + e.getMessage());
            return false;
        }
    }

    public static void renewPassword(long ID, String newPassword){
        try {
            String filePath = accountsFolderPath + File.separator + ID + ".txt";
            Path userPath = Paths.get(filePath);
            if (!Files.exists(userPath)) {
                System.out.println("User file does not exist.");
                return;
            }

            java.util.List<String> lines = Files.readAllLines(userPath);
            if (lines.isEmpty()) {
                System.out.println("User file is empty.");
                return;
            }

            String firstLine = lines.get(0).trim();
            String[] parts = firstLine.split("#");
            if (parts.length >= 3) {
                parts[2] = banking.infrastructure.SecurityUtil.hashText(newPassword);
                lines.set(0, String.join("#", parts));
                Files.write(userPath, lines);
                System.out.println("Password updated successfully for user ID: " + ID);
            } else {
                System.out.println("Invalid user file format. Password update failed.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred while updating the password: " + e.getMessage());
        }
    }

}
