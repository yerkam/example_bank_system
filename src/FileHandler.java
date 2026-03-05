package src;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Handles initialization of the folder structure for the banking system.
 * 
 * Folder structure:
 *   data/ (hidden)
 *   +-- Accounts/           - Per-user account files ({id}.txt)
 *   +-- TransactionHistory/  - Per-user transaction history files
 *   +-- Credit Card/        - Credit card data
 *   |   +-- CreditCards.txt
 *   +-- FrozenAccounts.txt
 */
public class FileHandler {

    private final String dataFolderName;
    private final String accountsFolderPath;
    private final String transactionHistoryPath;
    private final String creditCardFolderPath;
    private final String creditCardsFile;
    private final String frozenAccountsFile;
    
    public FileHandler() {
        String folderName = "data";
        if (!System.getProperty("os.name").toLowerCase().contains("win")) {
            folderName = "." + folderName;
        }
        this.dataFolderName = folderName;
        this.accountsFolderPath = dataFolderName + File.separator + "Accounts";
        this.transactionHistoryPath = dataFolderName + File.separator + "TransactionHistory";
        this.creditCardFolderPath = dataFolderName + File.separator + "Credit Card";
        this.creditCardsFile = creditCardFolderPath + File.separator + "CreditCards.txt";
        this.frozenAccountsFile = dataFolderName + File.separator + "FrozenAccounts.txt";
        initializeFolderStructure();
    }

    /**
     * Initializes the folder structure for the banking system.
     * Creates the data folder with Accounts, TransactionHistory, and Credit Card subfolders.
     */
    private void initializeFolderStructure() {
        try {
            Path dataPath = Paths.get(dataFolderName);
            if (!Files.exists(dataPath)) {
                Files.createDirectory(dataPath);
                if (System.getProperty("os.name").toLowerCase().contains("win")) {
                    Files.setAttribute(dataPath, "dos:hidden", true);
                }
            }

            Path accountsPath = Paths.get(accountsFolderPath);
            if (!Files.exists(accountsPath)) {
                Files.createDirectory(accountsPath);
            }

            Path transactionPath = Paths.get(transactionHistoryPath);
            if (!Files.exists(transactionPath)) {
                Files.createDirectory(transactionPath);
            }

            Path creditCardPath = Paths.get(creditCardFolderPath);
            if (!Files.exists(creditCardPath)) {
                Files.createDirectory(creditCardPath);
            }

            Path creditCardsPath = Paths.get(creditCardsFile);
            if (!Files.exists(creditCardsPath)) {
                Files.createFile(creditCardsPath);
            }

            Path frozenAccountsPath = Paths.get(frozenAccountsFile);
            if (!Files.exists(frozenAccountsPath)) {
                Files.createFile(frozenAccountsPath);
            }

        } catch (Exception e) {
            System.out.println("An error occurred while initializing folder structure: " + e.getMessage());
        }
    }

    public String getAccountsFolderPath() {
        return accountsFolderPath;
    }

    public String getTransactionHistoryPath() {
        return transactionHistoryPath;
    }

    public String getCreditCardsFile() {
        return creditCardsFile;
    }

    public String getFrozenAccountsFile() {
        return frozenAccountsFile;
    }
}