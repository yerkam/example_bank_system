package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Scanner;

/**
 * This class is responsible for handling file operations related to account management.
 */
public class FileHandler {

    private final String dataFolderName;
    private final String checkingFolderPath;
    private final String checkingAccountFile;
    private final String transactionHistoryPath;
    private final String depositFile;
    private final String currencyFile;
    private final String frozenAccountsFile;
    private final String debitCardsFile;
    private final String creditCardsFile;
    public FileHandler() {
        // Set the data folder name (hidden on non-Windows)
        String folderName = "data";
        if (!System.getProperty("os.name").toLowerCase().contains("win")) {
            folderName = "." + folderName;
        }
        this.dataFolderName = folderName;
        
        // Define paths for the folder structure
        this.checkingFolderPath = dataFolderName + File.separator + "Checking";
        this.checkingAccountFile = checkingFolderPath + File.separator + "account.txt";
        this.transactionHistoryPath = checkingFolderPath + File.separator + "transaction history";
        this.depositFile = dataFolderName + File.separator + "Deposit.txt";
        this.currencyFile = dataFolderName + File.separator + "Currency.txt";
        this.frozenAccountsFile = dataFolderName + File.separator + "FrozenAccounts.txt";
        this.debitCardsFile = dataFolderName + File.separator + "DebitCards.txt";
        this.creditCardsFile = dataFolderName + File.separator + "CreditCards.txt";
        initializeFolderStructure();
    }

    /**
     * Initializes the folder structure for account management.
     * Creates the hidden data folder with Checking folder, Deposit.txt, and Currency.txt.
     */
    private void initializeFolderStructure() {
        try {
            // Create main data folder
            Path dataPath = Paths.get(dataFolderName);
            if (!Files.exists(dataPath)) {
                Files.createDirectory(dataPath);
                // Hide the folder on Windows
                if (System.getProperty("os.name").toLowerCase().contains("win")) {
                    Files.setAttribute(dataPath, "dos:hidden", true);
                }
            }

            // Create Checking folder
            Path checkingPath = Paths.get(checkingFolderPath);
            if (!Files.exists(checkingPath)) {
                Files.createDirectory(checkingPath);
            }

            // Create account.txt inside Checking folder
            Path accountPath = Paths.get(checkingAccountFile);
            if (!Files.exists(accountPath)) {
                Files.createFile(accountPath);
            }

            // Create transaction history folder inside Checking folder
            Path transactionPath = Paths.get(transactionHistoryPath);
            if (!Files.exists(transactionPath)) {
                Files.createDirectory(transactionPath);
            }

            // Create Deposit.txt in data folder
            Path depositPath = Paths.get(depositFile);
            if (!Files.exists(depositPath)) {
                Files.createFile(depositPath);
            }

            // Create Currency.txt in data folder
            Path currencyPath = Paths.get(currencyFile);
            if (!Files.exists(currencyPath)) {
                Files.createFile(currencyPath);
            }

            // Create FrozenAccounts.txt in data folder
            Path frozenAccountsPath = Paths.get(frozenAccountsFile);
            if (!Files.exists(frozenAccountsPath)) {
                Files.createFile(frozenAccountsPath);
            }
            // Create DebitCards.txt in data folder
            Path debitCardsPath = Paths.get(debitCardsFile);
            if (!Files.exists(debitCardsPath)) {
                Files.createFile(debitCardsPath);
            }

            // Create CreditCards.txt in data folder
            Path creditCardsPath = Paths.get(creditCardsFile);
            if (!Files.exists(creditCardsPath)) {
                Files.createFile(creditCardsPath);
            }

        } catch (Exception e) {
            System.out.println("An error occurred while initializing folder structure: " + e.getMessage());
        }
    }


    /**
     * This method creates a checking account and writes the account information to a file.
     * 
     * @param name     Client's name
     * @param surname  Client's surname
     * @param id       Client's ID
     * @param password Client's password
     * @param balance  Client's account balance
     * @param active   Client's account status
     */
    public void createCheckingAccount(String name, String surname, long id, int password, double balance, boolean active) {
        Path path = Paths.get(checkingAccountFile);
        if (!Files.exists(path)) {
            System.out.println("The file does not exist. Account creation failed.");
            return;
        }

        if (isDuplicateId(id, checkingAccountFile)) {
            System.out.println("An account with the same ID already exists. Account creation failed.");
            return;
        }

        try (FileWriter writer = new FileWriter(checkingAccountFile, true)) {
            writer.write("Checking#" + name + "#" + surname + "#" + id + "#" + password + "#" + balance + "#" + active + "#" + generateIBAN() + "\n");
        } catch (Exception e) {
            System.out.println("An error occurred while creating the account: " + e.getMessage());
        }
    }


    /**
     * This method creates a deposit account and writes the account information to a file.
     *
     * @param name     Client's name
     * @param surname  Client's surname
     * @param id       Client's ID
     * @param password Client's password
     * @param balance  Client's account balance
     * @param months   Client's account duration in months
     */
    public void createDepositAccount(String name, String surname, long id, int password, double balance, int months) {
        Path path = Paths.get(depositFile);
        if (!Files.exists(path)) {
            System.out.println("The file does not exist. Account creation failed.");
            return;
        }

        if (isDuplicateId(id, depositFile)) {
            System.out.println("An account with the same ID already exists. Account creation failed.");
            return;
        }

        try (FileWriter writer = new FileWriter(depositFile, true)) {
            LocalDate date = LocalDate.now();
            LocalDate expiryDate = date.plusMonths(months);
            writer.write("Deposit#" + name + "#" + surname + "#" + id + "#" + password + "#" + balance + "#" + expiryDate + "\n");
        } catch (Exception e) {
            System.out.println("An error occurred while creating the account: " + e.getMessage());
        }
    }


    /**
     * This method checks if an account exists in the file based on the provided ID and password.
     *
     * @param id       Client's ID
     * @param password Client's password
     * @return true if the account exists and the password matches, false otherwise.
     */
    public boolean checkAccount(long id, int password) {
        // Check in checking accounts
        if (checkAccountInFile(id, password, checkingAccountFile)) {
            return true;
        }
        // Check in deposit accounts
        return checkAccountInFile(id, password, depositFile);
    }


    /**
     * Helper method to check if an account exists in a specific file.
     *
     * @param id       Client's ID
     * @param password Client's password
     * @param filePath Path to the file to check
     * @return true if the account exists and the password matches, false otherwise.
     */
    private boolean checkAccountInFile(long id, int password, String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (Files.exists(path)) {
                for (String line : Files.readAllLines(path)) {
                    if (line.trim().isEmpty()) {
                        continue;
                    }
                    String[] parts = line.split("#");
                    if (parts.length >= 5) {
                        long storedId = Long.parseLong(parts[3]);
                        int storedPassword = Integer.parseInt(parts[4]);
                        if (storedId == id && storedPassword == password) {
                            return true;
                        }
                    } else {
                        System.out.println("Invalid account data format in file: " + line);
                    }
                }
            } else {
                System.out.println("The file does not exist: " + filePath);
            }
        } catch (Exception e) {
            System.out.println("An error occurred while checking the account: " + e.getMessage());
        }
        return false;
    }


    /**
     * Checks whether an account with the given ID already exists in the specified file.
     *
     * @param id       The ID to check for duplicates.
     * @param filePath The path to the file to check.
     * @return true if a duplicate ID is found, false otherwise.
     */
    private boolean isDuplicateId(long id, String filePath) {
        File myObj = new File(filePath);
        try (Scanner myReader = new Scanner(myObj)) {
            long lineNum = 0;
            while (myReader.hasNextLine()) {
                lineNum++;
                String data = myReader.nextLine();
                if (data.trim().isEmpty()) {
                    continue;
                }
                String[] parts = data.split("#");
                if (parts.length >= 5) {
                    long storedId = Long.parseLong(parts[3]);
                    if (storedId == id) {
                        return true;
                    }
                } else {
                    System.out.println("Invalid account data at line: " + lineNum);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while checking for duplicate ID.");
            e.printStackTrace();
        }
        return false;
    }


    /**
     * This method retrieves the transaction history for a given account ID and returns it as a 2D array.
     * @param id The account ID for which to retrieve the transaction history.
     * @return A 2D array containing the transaction history, where each row represents a transaction with its ID, amount, and date.
     */
    public String[][] getTransactionHistory(long id) {
        TransactionList transactionList = new TransactionList();
        String filePath = transactionHistoryPath + File.separator + id + ".txt";
        File myObj = new File(filePath);
        if (!myObj.exists()) {
            return transactionList.toArray(); // Return empty list if no transaction history exists
        }
        try (Scanner myReader = new Scanner(myObj)) {
            long lineNum = 0;
            while (myReader.hasNextLine()) {
                lineNum++;
                String data = myReader.nextLine();
                if (data.trim().isEmpty()) {
                    continue;
                }
                String[] parts = data.split("#");
                if (parts.length == 3) {
                    long transactionId = Long.parseLong(parts[0]);
                    int amount = Integer.parseInt(parts[1]);
                    String date = parts[2];
                    transactionList.add(transactionId, amount, date);
                } else {
                    System.out.println("Invalid transaction data at line: " + lineNum);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading the transaction history.");
            e.printStackTrace();
        }
        return transactionList.toArray();
    }


    /**
     * This method generates a unique IBAN for new checking accounts.
     * @return A unique IBAN number for the new account.
     */
    private int generateIBAN() {
        int randomNum = (int) (Math.random() * 1000000000);
        File myObj = new File(checkingAccountFile);
        try (Scanner myReader = new Scanner(myObj)) {
            long lineNum = 0;
            while (myReader.hasNextLine()) {
                lineNum++;
                String data = myReader.nextLine();
                if (data.trim().isEmpty()) {
                    continue;
                }
                String[] parts = data.split("#");
                if (parts[0].equals("Checking")) {
                    if (parts.length >= 8) {
                        int storedIBAN = Integer.parseInt(parts[7]);
                        if (storedIBAN == randomNum) {
                            return generateIBAN(); // Generate a new IBAN if it already exists
                        }
                    } else {
                        System.out.println("Invalid account data at line: " + lineNum);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return randomNum;
    }


    /**
     * Generates a random alphanumeric password for account reactivation.
     * @return A random alphanumeric password string of 12 characters.
     */
    private String generateReactivationPassword() {
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
     * Freezes a checking account by setting its status to false and generating a reactivation password.
     * The reactivation password is stored in FrozenAccounts.txt.
     * 
     * @param id The ID of the account to freeze.
     * @return The reactivation password, or null if the operation failed.
     */
    public String freezeAccount(long id) {
        try {
            Path accountPath = Paths.get(checkingAccountFile);
            if (!Files.exists(accountPath)) {
                System.out.println("The account file does not exist.");
                return null;
            }

            // Read all lines from the account file
            java.util.List<String> lines = Files.readAllLines(accountPath);
            boolean accountFound = false;
            
            // Find and update the account
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split("#");
                if (parts.length >= 7 && parts[0].equals("Checking")) {
                    long storedId = Long.parseLong(parts[3]);
                    if (storedId == id) {
                        // Check if already frozen
                        boolean currentStatus = Boolean.parseBoolean(parts[6]);
                        if (!currentStatus) {
                            System.out.println("Account is already frozen.");
                            return null;
                        }
                        
                        // Set status to false
                        parts[6] = "false";
                        lines.set(i, String.join("#", parts));
                        accountFound = true;
                        break;
                    }
                }
            }

            if (!accountFound) {
                System.out.println("Account with ID " + id + " not found.");
                return null;
            }

            // Write the updated lines back to the file
            Files.write(accountPath, lines);

            // Generate reactivation password
            String reactivationPassword = generateReactivationPassword();

            // Store the password in FrozenAccounts.txt
            try (FileWriter writer = new FileWriter(frozenAccountsFile, true)) {
                writer.write(id + "#" + reactivationPassword + "\n");
            }

            return reactivationPassword;

        } catch (Exception e) {
            System.out.println("An error occurred while freezing the account: " + e.getMessage());
            return null;
        }
    }


    /**
     * Reactivates a frozen checking account if the provided password matches the stored reactivation password.
     * Sets the account status back to true and removes the entry from FrozenAccounts.txt.
     * 
     * @param id The ID of the account to reactivate.
     * @param password The reactivation password.
     * @return true if the account was successfully reactivated, false otherwise.
     */
    public boolean reactivateAccount(long id, String password) {
        try {
            Path frozenPath = Paths.get(frozenAccountsFile);
            if (!Files.exists(frozenPath)) {
                System.out.println("The frozen accounts file does not exist.");
                return false;
            }

            // Read all lines from FrozenAccounts.txt
            java.util.List<String> frozenLines = Files.readAllLines(frozenPath);
            boolean passwordMatches = false;
            int matchingLineIndex = -1;

            // Check if the password matches
            for (int i = 0; i < frozenLines.size(); i++) {
                String line = frozenLines.get(i);
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split("#");
                if (parts.length >= 2) {
                    long storedId = Long.parseLong(parts[0]);
                    String storedPassword = parts[1];
                    if (storedId == id && storedPassword.equals(password)) {
                        passwordMatches = true;
                        matchingLineIndex = i;
                        break;
                    }
                }
            }

            if (!passwordMatches) {
                System.out.println("Invalid reactivation password or account not found in frozen accounts.");
                return false;
            }

            // Update the account status in account.txt
            Path accountPath = Paths.get(checkingAccountFile);
            if (!Files.exists(accountPath)) {
                System.out.println("The account file does not exist.");
                return false;
            }

            java.util.List<String> accountLines = Files.readAllLines(accountPath);
            boolean accountUpdated = false;

            for (int i = 0; i < accountLines.size(); i++) {
                String line = accountLines.get(i);
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split("#");
                if (parts.length >= 7 && parts[0].equals("Checking")) {
                    long storedId = Long.parseLong(parts[3]);
                    if (storedId == id) {
                        // Set status to true
                        parts[6] = "true";
                        accountLines.set(i, String.join("#", parts));
                        accountUpdated = true;
                        break;
                    }
                }
            }

            if (!accountUpdated) {
                System.out.println("Account with ID " + id + " not found in account file.");
                return false;
            }

            // Write the updated account lines back
            Files.write(accountPath, accountLines);

            // Remove the entry from FrozenAccounts.txt
            frozenLines.remove(matchingLineIndex);
            Files.write(frozenPath, frozenLines);

            return true;

        } catch (Exception e) {
            System.out.println("An error occurred while reactivating the account: " + e.getMessage());
            return false;
        }
    }
      /**
     * This method creates a currency account and writes the account information to a file.
     * Supports USD and EUR currencies with fixed exchange rates.
     * 
     * @param name     Client's name
     * @param surname  Client's surname
     * @param id       Client's ID
     * @param password Client's password
     * @param balance  Client's account balance
     * @param currency Currency type (USD or EUR)
     */

    public void createCurrencyAccount(String name, String surname, long id, int password, double balance, String currency) {
        Path path = Paths.get(currencyFile);
        if (!Files.exists(path)) {
            System.out.println("The file does not exist. Account creation failed.");
            return;
        }

        // Validate currency type
        if (!currency.equals("USD") && !currency.equals("EUR")) {
            System.out.println("Invalid currency type. Only USD and EUR are supported. Account creation failed.");
            return;
        }

        if (isDuplicateId(id, currencyFile)) {
            System.out.println("An account with the same ID already exists. Account creation failed.");
            return;
        }

        try (FileWriter writer = new FileWriter(currencyFile, true)) {
            writer.write("Currency#" + name + "#" + surname + "#" + id + "#" + password + "#" + balance + "#" + currency + "\n");
        } catch (Exception e) {
            System.out.println("An error occurred while creating the account: " + e.getMessage());
        }
    }
    /**
     * Generates a unique 16-digit card number.
     * @return A unique 16-digit card number as a String.
     */
    private String generateCardNumber() {
        SecureRandom random = new SecureRandom();
        StringBuilder cardNumber = new StringBuilder();
        
        // Generate 16 digits
        for (int i = 0; i < 16; i++) {
            cardNumber.append(random.nextInt(10));
        }
        
        String generatedNumber = cardNumber.toString();
        
        // Check if the card number already exists in debit or credit cards
        if (isCardNumberExists(generatedNumber)) {
            return generateCardNumber(); // Recursively generate a new number
        }
        
        return generatedNumber;
    }


    /**
     * Checks if a card number already exists in debit or credit card files.
     * @param cardNumber The card number to check.
     * @return true if the card number exists, false otherwise.
     */
    private boolean isCardNumberExists(String cardNumber) {
        try {
            // Check in debit cards
            Path debitPath = Paths.get(debitCardsFile);
            if (Files.exists(debitPath)) {
                for (String line : Files.readAllLines(debitPath)) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = line.split("#");
                    if (parts.length >= 2 && parts[1].equals(cardNumber)) {
                        return true;
                    }
                }
            }
            
            // Check in credit cards
            Path creditPath = Paths.get(creditCardsFile);
            if (Files.exists(creditPath)) {
                for (String line : Files.readAllLines(creditPath)) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = line.split("#");
                    if (parts.length >= 2 && parts[1].equals(cardNumber)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error checking card number: " + e.getMessage());
        }
        return false;
    }


    /**
     * Generates a random 3-digit CVV number.
     * @return A 3-digit CVV as a String.
     */
    private String generateCVV() {
        SecureRandom random = new SecureRandom();
        int cvv = 100 + random.nextInt(900); // Range: 100-999
        return String.valueOf(cvv);
    }


    /**
     * Generates an expiry date for a card (5 years from now).
     * @return Expiry date in MM/YY format.
     */
    private String generateExpiryDate() {
        LocalDate now = LocalDate.now();
        LocalDate expiryDate = now.plusYears(5);
        int month = expiryDate.getMonthValue();
        int year = expiryDate.getYear() % 100; // Last 2 digits of year
        return String.format("%02d/%02d", month, year);
    }


    /**
     * Creates a debit card linked to a checking account.
     * 
     * @param accountId The ID of the checking account to link the card to.
     * @param holderName The name of the cardholder (typically first name + surname).
     * @return The generated card number, or null if creation failed.
     */
    public String createDebitCard(long accountId, String holderName) {
        Path path = Paths.get(debitCardsFile);
        if (!Files.exists(path)) {
            System.out.println("The debit cards file does not exist. Card creation failed.");
            return null;
        }

        // Check if the account exists in checking accounts
        if (!isAccountExists(accountId, checkingAccountFile)) {
            System.out.println("No checking account found with ID " + accountId + ". Card creation failed.");
            return null;
        }

        // Check if a debit card already exists for this account
        if (isDebitCardExists(accountId)) {
            System.out.println("A debit card already exists for account ID " + accountId + ". Card creation failed.");
            return null;
        }

        try (FileWriter writer = new FileWriter(debitCardsFile, true)) {
            String cardNumber = generateCardNumber();
            String cvv = generateCVV();
            String expiryDate = generateExpiryDate();
            
            writer.write("DebitCard#" + cardNumber + "#" + cvv + "#" + expiryDate + "#" + holderName + "#" + accountId + "\n");
            return cardNumber;
        } catch (Exception e) {
            System.out.println("An error occurred while creating the debit card: " + e.getMessage());
            return null;
        }
    }


    /**
     * Creates a credit card with a specified credit limit.
     * 
     * @param accountId The ID to associate with this credit card.
     * @param holderName The name of the cardholder.
     * @param creditLimit The credit limit for the card.
     * @param paymentDay The day of the month for payment (1-28).
     * @return The generated card number, or null if creation failed.
     */
    public String createCreditCard(long accountId, String holderName, double creditLimit, int paymentDay) {
        Path path = Paths.get(creditCardsFile);
        if (!Files.exists(path)) {
            System.out.println("The credit cards file does not exist. Card creation failed.");
            return null;
        }

        // Validate payment day
        if (paymentDay < 1 || paymentDay > 28) {
            System.out.println("Invalid payment day. Must be between 1 and 28. Card creation failed.");
            return null;
        }

        // Check if account ID is already used for a credit card
        if (isCreditCardExists(accountId)) {
            System.out.println("A credit card already exists for account ID " + accountId + ". Card creation failed.");
            return null;
        }

        try (FileWriter writer = new FileWriter(creditCardsFile, true)) {
            String cardNumber = generateCardNumber();
            String cvv = generateCVV();
            String expiryDate = generateExpiryDate();
            double availableLimit = creditLimit; // Initially, available limit equals credit limit
            double debt = 0.0; // No debt initially
            
            writer.write("CreditCard#" + cardNumber + "#" + cvv + "#" + expiryDate + "#" + holderName + "#" + accountId + "#" + creditLimit + "#" + availableLimit + "#" + debt + "#" + paymentDay + "\n");
            return cardNumber;
        } catch (Exception e) {
            System.out.println("An error occurred while creating the credit card: " + e.getMessage());
            return null;
        }
    }


    /**
     * Checks if an account exists in a specific file.
     * @param accountId The account ID to check.
     * @param filePath The file path to check in.
     * @return true if the account exists, false otherwise.
     */
    private boolean isAccountExists(long accountId, String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (Files.exists(path)) {
                for (String line : Files.readAllLines(path)) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = line.split("#");
                    if (parts.length >= 4) {
                        long storedId = Long.parseLong(parts[3]);
                        if (storedId == accountId) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error checking account existence: " + e.getMessage());
        }
        return false;
    }


    /**
     * Checks if a debit card already exists for a given account ID.
     * @param accountId The account ID to check.
     * @return true if a debit card exists for this account, false otherwise.
     */
    private boolean isDebitCardExists(long accountId) {
        try {
            Path path = Paths.get(debitCardsFile);
            if (Files.exists(path)) {
                for (String line : Files.readAllLines(path)) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = line.split("#");
                    if (parts.length >= 6) {
                        long storedAccountId = Long.parseLong(parts[5]);
                        if (storedAccountId == accountId) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error checking debit card existence: " + e.getMessage());
        }
        return false;
    }


    /**
     * Checks if a credit card already exists for a given account ID.
     * @param accountId The account ID to check.
     * @return true if a credit card exists for this account, false otherwise.
     */
    private boolean isCreditCardExists(long accountId) {
        try {
            Path path = Paths.get(creditCardsFile);
            if (Files.exists(path)) {
                for (String line : Files.readAllLines(path)) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = line.split("#");
                    if (parts.length >= 6) {
                        long storedAccountId = Long.parseLong(parts[5]);
                        if (storedAccountId == accountId) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error checking credit card existence: " + e.getMessage());
        }
        return false;
    }
}
