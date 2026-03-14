package banking.application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Scanner;

/**
 * Manages account operations using per-user files.
 * Each user has their own file at data/Accounts/{id}.txt
 * 
 * File format:
 *   Line 1: name#surname#password
 *   Subsequent lines (multiple accounts of each type allowed):
 *     Checking#accountNumber#balance#active#IBAN
 *     Checking#accountNumber#balance#active#IBAN#cardNumber#cvv#cardExpiry#holderName  (with debit card)
 *     Deposit#accountNumber#balance#expiryDate
 *     Currency#accountNumber#balance#currencyType
 */
public class AccountManager {

    private final String accountsFolderPath;

    public AccountManager(String accountsFolderPath) {
        this.accountsFolderPath = accountsFolderPath;
    }


    /**
     * Returns the file path for a specific user's account file.
     * @param id The user's ID.
     * @return The full path to the user's account file.
     */
    private String getUserFilePath(long id) {
        return accountsFolderPath + File.separator + id + ".txt";
    }


    /**
     * Ensures the user file exists. If not, creates it with user info on the first line.
     * If the file already exists, does nothing.
     *
     * @param name     Client's name
     * @param surname  Client's surname
     * @param id       Client's ID
     * @param password Client's password
     */
    private void ensureUserFile(String name, String surname, long id, int password) {
        String filePath = getUserFilePath(id);
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write(name + "#" + surname + "#" + password + "\n");
            } catch (Exception e) {
                System.out.println("Error creating user file: " + e.getMessage());
            }
        }
    }


    /**
     * Checks if a user already has a specific account type (Checking, Deposit, Currency).
     *
     * @param id          The user's ID.
     * @param accountType The account type to check for (e.g., "Checking", "Deposit", "Currency").
     * @return true if the user already has that account type, false otherwise.
     */
    public boolean hasAccountType(long id, String accountType) {
        String filePath = getUserFilePath(id);
        File file = new File(filePath);
        if (!file.exists()) return false;

        try (Scanner reader = new Scanner(file)) {
            // Skip the first line (user info)
            if (reader.hasNextLine()) reader.nextLine();

            while (reader.hasNextLine()) {
                String line = reader.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("#");
                if (parts[0].equals(accountType)) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error reading user file: " + e.getMessage());
        }
        return false;
    }


    /**
     * Counts how many accounts of a specific type the user has.
     *
     * @param id          The user's ID.
     * @param accountType The account type to count.
     * @return The number of accounts of the specified type.
     */
    public int getAccountTypeCount(long id, String accountType) {
        String filePath = getUserFilePath(id);
        File file = new File(filePath);
        if (!file.exists()) return 0;

        int count = 0;
        try (Scanner reader = new Scanner(file)) {
            if (reader.hasNextLine()) reader.nextLine();
            while (reader.hasNextLine()) {
                String line = reader.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("#");
                if (parts[0].equals(accountType)) {
                    count++;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error reading user file: " + e.getMessage());
        }
        return count;
    }


    /**
     * Checks if a user account file exists.
     *
     * @param id The user's ID.
     * @return true if the user file exists, false otherwise.
     */
    public boolean isAccountExists(long id) {
        return Files.exists(Paths.get(getUserFilePath(id)));
    }


    /**
     * Checks if account credentials are valid by reading the first line of the user's file.
     *
     * @param id       Client's ID
     * @param password Client's password
     * @return true if the account exists and the password matches, false otherwise.
     */
    public boolean checkAccount(long id, int password) {
        String filePath = getUserFilePath(id);
        File file = new File(filePath);
        if (!file.exists()) return false;

        try (Scanner reader = new Scanner(file)) {
            if (reader.hasNextLine()) {
                String firstLine = reader.nextLine().trim();
                if (!firstLine.isEmpty()) {
                    String[] parts = firstLine.split("#");
                    if (parts.length >= 3) {
                        int storedPassword = Integer.parseInt(parts[2]);
                        return storedPassword == password;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error checking account: " + e.getMessage());
        }
        return false;
    }


    /**
     * Creates a checking account for the user.
     * If the user file doesn't exist, it will be created.
     * Multiple checking accounts are allowed.
     *
     * @param name     Client's name
     * @param surname  Client's surname
     * @param id       Client's ID
     * @param password Client's password
     * @param balance  Client's account balance
     * @param active   Client's account status
     */
    public void createCheckingAccount(String name, String surname, long id, int password, double balance, boolean active) {
        ensureUserFile(name, surname, id, password);
        int accountNumber = getAccountTypeCount(id, "Checking") + 1;

        try (FileWriter writer = new FileWriter(getUserFilePath(id), true)) {
            writer.write("Checking#" + accountNumber + "#" + balance + "#" + active + "#" + generateIBAN() + "\n");
        } catch (Exception e) {
            System.out.println("An error occurred while creating the checking account: " + e.getMessage());
        }
    }


    /**
     * Creates a deposit account for the user.
     * If the user file doesn't exist, it will be created.
     * Multiple deposit accounts are allowed.
     *
     * @param name     Client's name
     * @param surname  Client's surname
     * @param id       Client's ID
     * @param password Client's password
     * @param balance  Client's account balance
     * @param months   Client's account duration in months
     */
    public void createDepositAccount(String name, String surname, long id, int password, double balance, int months) {
        ensureUserFile(name, surname, id, password);
        int accountNumber = getAccountTypeCount(id, "Deposit") + 1;

        try (FileWriter writer = new FileWriter(getUserFilePath(id), true)) {
            LocalDate date = LocalDate.now();
            LocalDate expiryDate = date.plusMonths(months);
            writer.write("Deposit#" + accountNumber + "#" + balance + "#" + expiryDate + "\n");
        } catch (Exception e) {
            System.out.println("An error occurred while creating the deposit account: " + e.getMessage());
        }
    }


    /**
     * Creates a currency account for the user.
     * Supports USD and EUR currencies.
     * If the user file doesn't exist, it will be created.
     * If the user already has a currency account, creation is rejected.
     *
     * @param name     Client's name
     * @param surname  Client's surname
     * @param id       Client's ID
     * @param password Client's password
     * @param balance  Client's account balance
     * @param currency Currency type (USD or EUR)
     */
    public void createCurrencyAccount(String name, String surname, long id, int password, double balance, String currency) {
        // Currencies will be refactored
        if (!currency.equals("USD") && !currency.equals("EUR")) {
            System.out.println("Invalid currency type. Only USD and EUR are supported. Account creation failed.");
            return;
        }

        ensureUserFile(name, surname, id, password);
        int accountNumber = getAccountTypeCount(id, "Currency") + 1;

        try (FileWriter writer = new FileWriter(getUserFilePath(id), true)) {
            writer.write("Currency#" + accountNumber + "#" + balance + "#" + currency + "\n");
        } catch (Exception e) {
            System.out.println("An error occurred while creating the currency account: " + e.getMessage());
        }
    }


    /**
     * Generates a unique IBAN for new checking accounts.
     * Scans all user files in the accounts folder to ensure uniqueness.
     *
     * @return A unique IBAN number for the new account.
     */
    private int generateIBAN() {
        int randomNum = (int) (Math.random() * 1000000000);
        File accountsFolder = new File(accountsFolderPath);
        File[] userFiles = accountsFolder.listFiles((dir, name) -> name.endsWith(".txt"));

        if (userFiles != null) {
            for (File userFile : userFiles) {
                try (Scanner myReader = new Scanner(userFile)) {
                    // Skip first line (user info)
                    if (myReader.hasNextLine()) myReader.nextLine();

                    while (myReader.hasNextLine()) {
                        String data = myReader.nextLine().trim();
                        if (data.isEmpty()) continue;
                        String[] parts = data.split("#");
                        if (parts[0].equals("Checking") && parts.length >= 5) {
                            int storedIBAN = Integer.parseInt(parts[4]);
                            if (storedIBAN == randomNum) {
                                return generateIBAN(); // Recursively generate a new IBAN
                            }
                        }
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("Error reading file for IBAN check: " + e.getMessage());
                }
            }
        }
        return randomNum;
    }
}
