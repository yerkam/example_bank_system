package banking.infrastructure;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Scanner;

import banking.domain.accounts.Account;
import banking.domain.accounts.CheckingAccount;
import banking.domain.accounts.CurrencyAccount;
import banking.domain.accounts.DepositAccount;


/**
 * The FileAccountRepository class implements the AccountRepository interface and provides file-based storage for user accounts.
 * It manages user account files, allowing for the creation, retrieval, and updating of account information.
 */
public class FileAccountRepository implements AccountRepository {
	
	// Singleton instance of FileHandler to manage file paths and operations
	private final FileHandler fileHandler = FileHandler.getInstance();
    private String accountsFolderPath = fileHandler.getAccountsFolderPath();
    
    
    /**
     * Returns the file path for a specific user's account file.
     * @param id The user's ID.
     * @return The full path to the user's account file.
     */
    private String getUserFilePath(long id) {
        return accountsFolderPath + File.separator + id + ".txt";
    } 
	

    /**
     * Counts how many accounts of a specific type the user has.
     *
     * @param id          The user's ID.
     * @param accountType The account type to count.
     * @return The number of accounts of the specified type.
     */
    public int getAccountTypeCount(long userId, String accountType) {

        String filePath = getUserFilePath(userId);

        File file = new File(filePath);

        int count = 0;

        if ("CHECKING".equals(accountType.toUpperCase())) {
            count = 1000;
        }
        else if ("DEPOSIT".equals(accountType.toUpperCase())) {
            count = 2000;
        }
        else if ("CURRENCY".equals(accountType.toUpperCase())) {
            count = 3000;
        }

        if (!file.exists()) {
            return count;
        }

        try (Scanner reader = new Scanner(file)) {

            if (reader.hasNextLine()) {
                reader.nextLine();
            }

            while (reader.hasNextLine()) {

                String line = reader.nextLine().trim();

                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split("#");

                if (parts[0].equalsIgnoreCase(accountType)) {
                    count++;
                }
            }

        } catch (FileNotFoundException e) {

            System.out.println(
                    "Error reading user file: "
                            + e.getMessage()
            );
        }

        return count;
    }
		
	/**
	 * Checks if a debit card IBAN exists for a given user and account number.
	 *
	 * @param userId        The user's ID.
	 * @param accountNumber The account number to check.
	 * @return The IBAN if it exists, or 0 if it does not.
	 */
	public int hasDebitCardIBAN(long userId, int accountNumber) {
		String filePath = accountsFolderPath + File.separator + userId + ".txt";
        Path path = Paths.get(filePath);
		int IBAN = 0;
		try {
            java.util.List<String> lines = Files.readAllLines(path);
            
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("#");
                if (parts[0].equals("Checking") && parts.length >= 5) {
                    int storedAccountNumber = Integer.parseInt(parts[1]);
                    if (storedAccountNumber == accountNumber) {
                        if (parts.length > 5) {
                        	IBAN = 0;
                        }
                        else {
							IBAN = Integer.parseInt(parts[4]);
						}
                        break;
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("An error occurred while checking if the debit card exists: " + e.getMessage());
            IBAN = 0;
        }
		
		return IBAN;
	}

	
	/**
	 * Adjusts the balance of the first active checking account for a user by a specified amount.
	 *
	 * @param userId             The user's ID.
	 * @param accountsFolderPath The path to the accounts folder.
	 * @param amount             The amount to adjust (positive to increase, negative to decrease).
	 * @return true if the balance was successfully adjusted, false otherwise.
	 */
    public boolean adjustFirstActiveCheckingBalance(long userId, String accountsFolderPath, double amount) {
        if (amount == 0) {
            return true;
        }

        try {
            File userFile = new File(accountsFolderPath + File.separator + userId + ".txt");
            if (!userFile.exists()) {
                System.out.println("User file not found while updating checking balance.");
                return false;
            }

            java.util.List<String> lines = Files.readAllLines(userFile.toPath());
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("#");
                if (parts.length >= 5 && parts[0].equals("Checking") && Boolean.parseBoolean(parts[3])) {
                    double balance = Double.parseDouble(parts[2]);
                    balance += amount;
                    parts[2] = String.valueOf(balance);
                    lines.set(i, String.join("#", parts));
                    Files.write(userFile.toPath(), lines);
                    return true;
                }
            }

            System.out.println("No active checking account found for user " + userId + ".");
        } catch (Exception e) {
            System.out.println("An error occurred while updating checking balance: " + e.getMessage());
        }

        return false;
    }
		
	
    /**
	 * Saves a checking account for a user. If the user's file does not exist, it creates one with the user's info.
	 * The account information is appended to the user's file.
	 *
	 * @param userId  The user's ID.
	 * @param account The checking account to save.
	 */
	public void saveCheckingAccount(long userId, CheckingAccount account) {
//		ensureUserFile(account.getName(), account.getSurname(), account.getId(), account.getPassword());

        try (FileWriter writer = new FileWriter(getUserFilePath(account.getUserId()), true)) {
            writer.write("Checking#" + account.getAccountNumber() + "#" + account.getBalance() + "#" + account.isActive() + "#" +  "\n");
        } catch (Exception e) {
            System.out.println("An error occurred while creating the checking account: " + e.getMessage());
        }
		
	}


	/**
	 * Saves a deposit account for a user. If the user's file does not exist, it creates one with the user's info.
	 * The account information is appended to the user's file.
	 *
	 * @param userId  The user's ID.
	 * @param account The deposit account to save.
	 */
	public void saveDepositAccount(long userId, DepositAccount account) {
//		ensureUserFile(account.getName(), account.getSurname(), account.getId(), account.getPassword());

        try (FileWriter writer = new FileWriter(getUserFilePath(account.getUserId()), true)) {
            LocalDate date = LocalDate.now();
            LocalDate expiryDate = date.plusMonths(account.getMonths());
            writer.write("Deposit#" + account.getAccountNumber() + "#" + account.getBalance() + "#" + expiryDate + "#" + "\n");
        } catch (Exception e) {
            System.out.println("An error occurred while creating the deposit account: " + e.getMessage());
        }
		
	}

	
	/**
	 * Saves a currency account for a user. If the user's file does not exist, it creates one with the user's info.
	 * The account information is appended to the user's file.
	 *
	 * @param userId  The user's ID.
	 * @param account The currency account to save.
	 */
	public void saveCurrencyAccount(long userId, CurrencyAccount account) {

//		ensureUserFile(account.getName(), account.getSurname(), account.getId(), account.getPassword());

        try (FileWriter writer = new FileWriter(getUserFilePath(account.getUserId()), true)) {
            writer.write("Currency#" + account.getAccountNumber() + "#" + account.getBalance() + "#" + account.getCurrency() + "#" + "\n");
        } catch (Exception e) {
            System.out.println("An error occurred while creating the currency account: " + e.getMessage());
        }
		
	}

		
//	/**
//     * Ensures the user file exists. If not, creates it with user info on the first line.
//     * If the file already exists, does nothing.
//     *
//     * @param name     Client's name
//     * @param surname  Client's surname
//     * @param id       Client's ID
//     * @param password Client's password
//     */
//    public void ensureUserFile(String name, String surname, long id, String password) {
//        String filePath = getUserFilePath(id);
//        Path path = Paths.get(filePath);
//        String hashedPassword = banking.infrastructure.SecurityUtil.hashText(password);
//        if (!Files.exists(path)) {
//            try (FileWriter writer = new FileWriter(filePath)) {
//                writer.write(name + "#" + surname + "#" + hashedPassword + "\n");
//            } catch (Exception e) {
//                System.out.println("Error creating user file: " + e.getMessage());
//            }
//        }
//    }
}
