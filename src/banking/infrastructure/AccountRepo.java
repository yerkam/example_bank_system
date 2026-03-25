package banking.infrastructure;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
