package banking.infrastructure;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class AccountRepo {

	
    public int hasDebitCard(long userId, Path filePath, int accountNumber) {
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
	
	/**
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
}
