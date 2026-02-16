package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

        // Initialize folder structure
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
     * Returns the path to the transaction history folder.
     * @return Path to the transaction history folder.
     */
    public String getTransactionHistoryPath() {
        return transactionHistoryPath;
    }

    /**
     * Returns the path to the currency file.
     * @return Path to the currency file.
     */
    public String getCurrencyFile() {
        return currencyFile;
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
}
