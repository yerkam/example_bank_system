package banking.infrastructure;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import banking.domain.users.User;

/**
 * Handles initialization of the folder structure for the banking system.
 * 
 * Folder structure:
 *   data/ (hidden)
 *   +-- Accounts/           - Per-user account files ({id}.txt)
 *   +-- TransactionHistory/  - Per-user transaction history files
 *   +-- Credit Card/        - Credit card data
 *   |   +-- CreditCards.txt
 *   +-- Loans/              - Loan records
 *   |   +-- Loans.txt
 *   +-- FrozenAccounts.txt
 */
public class FileHandler {
	
	private static FileHandler instance;

    private final String dataFolderName;
    private final String accountsFolderPath;
    private final String transactionHistoryPath;
    private final String creditCardFolderPath;
    private final String creditCardsFile;
    private final String loanFolderPath;
    private final String loansFile;
    private final String usersFolderPath;
    private final String customersFile;
    private final String employeesFile;
    private final String managersFile;
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
        this.loanFolderPath = dataFolderName + File.separator + "Loans";
        this.loansFile = loanFolderPath + File.separator + "Loans.txt";
        this.usersFolderPath = dataFolderName + File.separator + "Users";
        this.customersFile = usersFolderPath + File.separator + "Customers.txt";
        this.employeesFile = usersFolderPath + File.separator + "Employees.txt";
        this.managersFile = usersFolderPath + File.separator + "Managers.txt";
        this.frozenAccountsFile = dataFolderName + File.separator + "FrozenAccounts.txt";
        
        initializeFolderStructure();
        instantiateManager();
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

            Path loanPath = Paths.get(loanFolderPath);
            if (!Files.exists(loanPath)) {
                Files.createDirectory(loanPath);
            }
            
            Path userPath = Paths.get(usersFolderPath);
            if (!Files.exists(userPath)) {
                Files.createDirectory(userPath);
            }
            
            Path customersPath = Paths.get(customersFile);
            if (!Files.exists(customersPath)) {
                Files.createFile(customersPath);
            }
            
            Path employeesPath = Paths.get(employeesFile);
            if (!Files.exists(employeesPath)) {
                Files.createFile(employeesPath);
            }
            
            Path managersPath = Paths.get(managersFile);
            if (!Files.exists(managersPath)) {
                Files.createFile(managersPath);
            }

            Path creditCardsPath = Paths.get(creditCardsFile);
            if (!Files.exists(creditCardsPath)) {
                Files.createFile(creditCardsPath);
            }

            Path loansPath = Paths.get(loansFile);
            if (!Files.exists(loansPath)) {
                Files.createFile(loansPath);
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
    
    public String getUsersFolderPath() {
        return usersFolderPath;
    }
    
    public String getCustomersFile() {
        return customersFile;
    }
    
    public String getEmployeesFile() {
        return employeesFile;
    }
    
    public String getManagersFile() {
        return managersFile;
    }

    public String getLoansFile() {
        return loansFile;
    }

    public String getFrozenAccountsFile() {
        return frozenAccountsFile;
    }
    
    public static FileHandler getInstance() {
		if (instance == null) {
			instance = new FileHandler();
		}
		return instance;
	}
    
    public void instantiateManager() {
    	if (!managerExists(6001)) {
    		String filePath = getManagersFile();
    	    try (FileWriter writer = new FileWriter(filePath, true)) {
    	        writer.write(
    	            "6001" + "#" + "Manager" + "#" + "One" + "#" +
    	            SecurityUtil.hashText("123456") + "#" + "MANAGER" + "\n");
    	        
    	        System.out.println("MANAGER account created with ID: 6001 and password: 12345");
    	    } 
    	    catch (Exception e) {
    	        System.out.println("Error initiating MANAGER: " + e.getMessage());
    	    }
    	}
		
	}
    
   
	public boolean managerExists(long id) {
    	String filePath = getManagersFile();
	    Path path = Paths.get(filePath);
	    try {
	        if (!Files.exists(path)) {
	            return false;
	        }
	        for (String line : Files.readAllLines(path)) {
	            String[] parts = line.split("#");
	            long storedId = Long.parseLong(parts[0]);
	            if (storedId == id) {
	                return true;
	            }
	        }

	    } 
	    catch (Exception e) {
	        System.out.println("Error reading manager file: " + e.getMessage());
	    }
	    return false;
	}
	

}