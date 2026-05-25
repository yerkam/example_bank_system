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

public class FileAccountRepository implements AccountRepository {
	
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
		
	// Name should be changed...
	public int hasDebitCardIBAN(long userId, Path filePath, int accountNumber) {
		int IBAN = 0;
		try {
            java.util.List<String> lines = Files.readAllLines(filePath);
            
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
		
	/** SHOULD BE MOVED TO CARD REPOSITORY
     * Checks if a card number already exists in debit cards (user files) or credit cards.
     * @param cardNumber The card number to check.
     * @return true if the card number exists, false otherwise.
     */
    public boolean isCardNumberExistsDebit(String cardNumber, String accountsFolderPath) {
        try {
            // Check in user account files (debit cards are in Checking lines)
            File accountsFolder = new File(accountsFolderPath);
            File[] userFiles = accountsFolder.listFiles((dir, name) -> name.endsWith(".txt"));
            if (userFiles != null) {
                for (File userFile : userFiles) {
                    for (String line : Files.readAllLines(userFile.toPath())) {
                        if (line.trim().isEmpty()) continue;
                        String[] parts = line.split("#");
                        // Checking with debit card: Checking#num#balance#active#IBAN#cardNumber#...
                        if (parts[0].equals("Checking") && parts.length >= 6 && parts[5].equals(cardNumber)) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error checking card number in the account folder path: " + e.getMessage());
        }
        return false;
    }

	@Override
	public void saveCheckingAccount(long userId, CheckingAccount account) {
		ensureUserFile(account.getName(), account.getSurname(), account.getId(), account.getPassword());

        try (FileWriter writer = new FileWriter(getUserFilePath(account.getId()), true)) {
            writer.write("Checking#" + account.getAccountNumber() + "#" + account.getBalance() + "#" + account.isActive() + "#" + account.getIban() + "\n");
        } catch (Exception e) {
            System.out.println("An error occurred while creating the checking account: " + e.getMessage());
        }
		
	}

	@Override
	public void saveDepositAccount(long userId, DepositAccount account) {
		ensureUserFile(account.getName(), account.getSurname(), account.getId(), account.getPassword());

        try (FileWriter writer = new FileWriter(getUserFilePath(account.getId()), true)) {
            LocalDate date = LocalDate.now();
            LocalDate expiryDate = date.plusMonths(account.getMonths());
            writer.write("Deposit#" + account.getAccountNumber() + "#" + account.getBalance() + "#" + expiryDate + "\n");
        } catch (Exception e) {
            System.out.println("An error occurred while creating the deposit account: " + e.getMessage());
        }
		
	}

	@Override
	public void saveCurrencyAccount(long userId, CurrencyAccount account) {

		ensureUserFile(account.getName(), account.getSurname(), account.getId(), account.getPassword());

        try (FileWriter writer = new FileWriter(getUserFilePath(account.getId()), true)) {
            writer.write("Currency#" + account.getAccountNumber() + "#" + account.getBalance() + "#" + account.getCurrency() + "\n");
        } catch (Exception e) {
            System.out.println("An error occurred while creating the currency account: " + e.getMessage());
        }
		
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
    public void ensureUserFile(String name, String surname, long id, String password) {
        String filePath = getUserFilePath(id);
        Path path = Paths.get(filePath);
        String hashedPassword = banking.infrastructure.SecurityUtil.hashText(password);
        if (!Files.exists(path)) {
            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write(name + "#" + surname + "#" + hashedPassword + "\n");
            } catch (Exception e) {
                System.out.println("Error creating user file: " + e.getMessage());
            }
        }
    }
}
