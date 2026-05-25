package banking.infrastructure;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import banking.domain.cards.CreditCard;
import banking.domain.cards.DebitCard;


/**
 * Implements the CardRepository interface to manage debit and credit card data using file storage.
 * Debit cards are stored inline in user account files, while credit cards are stored in a separate file.
 */
public class FileCardRepository implements CardRepository {
	
	private final FileHandler fileHandler = FileHandler.getInstance();
    private String accountsFolderPath = fileHandler.getAccountsFolderPath();
    private String creditCardsFile = fileHandler.getCreditCardsFile();

	/**
     * Saves the debit card data to the specified file path.
     * @param cardData The debit card data to save.
     * @param filePath The path to the file where the card data should be saved.
     * @param accountNumber The account number associated with the debit card.
     * @return true if the card was saved successfully, false otherwise.
     */
	public boolean saveDebitCard(DebitCard card, long userId, int accountNumber) {
		boolean saveCard = false;
		String filePath = accountsFolderPath + File.separator + userId + ".txt";
        Path path = Paths.get(filePath);
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
                            System.out.println("This checking account already has a debit card. Card creation failed.");
                            return false;
                        }
                        lines.set(i, line + "#" + card.getCardNumber() + "#" + card.getCvv() + "#" + card.getExpiryDate() + "#" + card.getHolderName());
                        saveCard = true;
                        break;
                    }
                }
            }

            Files.write(path, lines);

        } catch (Exception e) {
            System.out.println("An error occurred while saving the debit card: " + e.getMessage());
            saveCard = false;
        }

        return saveCard;
	}
	

	/**
	 * Saves the credit card data to the credit cards file.
	 * @param cardData The credit card data to save.
	 * @return true if the card was saved successfully, false otherwise.
	 */
	public boolean saveCreditCard(CreditCard cardData) {
		
        boolean saveCard = false;
        try (FileWriter writer = new FileWriter(creditCardsFile, true)) {
            writer.write("CreditCard#" + cardData.getCardNumber() + "#" + cardData.getCvv() + "#" + cardData.getExpiryDate() + "#" + cardData.isActive() + "#" + cardData.getHolderName()
                    + "#" + cardData.getUserId() + "#" + cardData.getCreditLimit() + "#" + cardData.getAvailableLimit() + "#" + cardData.getDebt() + "#" + cardData.getPaymentDay() + "\n");
            saveCard = true;
        } catch (Exception e) {
            System.out.println("An error occurred while saving the credit card: " + e.getMessage());
            saveCard = false;
        }
        return saveCard;
    }

	/**
     * Checks if a credit card already exists for a given user ID.
     * @param userId The user's ID.
     * @return true if a credit card exists for this user, false otherwise.
     */
    public boolean isCreditCardExists(long userId) {
        try {
            Path creditPath = Paths.get(creditCardsFile);
            if (Files.exists(creditPath)) {
                for (String line : Files.readAllLines(creditPath)) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = line.split("#");
                    if (parts.length >= 6) {
                        long storedUserId = Long.parseLong(parts[6]);
                        if (storedUserId == userId) {
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


    /**
     * Checks if a card number already exists in credit cards.
     * @param cardNumber The card number to check.
     * @return true if the card number exists, false otherwise.
     */
    public boolean isCardNumberExistsCredit(String cardNumber) {
        try {
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
            System.out.println("Error checking card number in the credit cards file: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Checks if a card number already exists in debit cards (user files) or credit cards.
     * @param cardNumber The card number to check.
     * @return true if the card number exists, false otherwise.
     */
    public boolean isCardNumberExistsDebit(String cardNumber) {
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

    
    /**
	 * Finds and returns a CreditCard object for a given user ID.
	 * @param userId The user's ID.
	 * @return A CreditCard object if found, or null if no card exists for the user.
	 */
    public CreditCard findCreditCardByUserId(long userId) {
        try {
            Path creditPath = Paths.get(creditCardsFile);
            if (!Files.exists(creditPath)) {
                return null;
            }

            for (String line : Files.readAllLines(creditPath)) {
                String trimmedLine = line.trim();
                if (trimmedLine.isEmpty()) continue;

                String[] parts = trimmedLine.split("#");
                if (parts[0].equals("CreditCard") && parts.length >= 10) {
                    boolean active = parts.length >= 11 ? Boolean.parseBoolean(parts[4]) : true;
                    int holderNameIndex = parts.length >= 11 ? 5 : 4;
                    int userIdIndex = parts.length >= 11 ? 6 : 5;
                    int creditLimitIndex = parts.length >= 11 ? 7 : 6;
                    int availableLimitIndex = parts.length >= 11 ? 8 : 7;
                    int debtIndex = parts.length >= 11 ? 9 : 8;
                    int paymentDayIndex = parts.length >= 11 ? 10 : 9;

                    long storedUserId = Long.parseLong(parts[userIdIndex]);
                    if (storedUserId == userId) {
                        return new CreditCard(
                            parts[1],
                            parts[2],
                            parts[3],
                            active,
                            parts[holderNameIndex],
                            storedUserId,
                            Double.parseDouble(parts[creditLimitIndex]),
                            Double.parseDouble(parts[availableLimitIndex]),
                            Double.parseDouble(parts[debtIndex]),
                            Integer.parseInt(parts[paymentDayIndex])
                        );
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading credit card: " + e.getMessage());
        }

        return null;
    }

    
    /**
	 * Updates the credit card information for a given user ID.
	 * @param cardData The CreditCard object containing the updated card information.
	 * @return true if the card was updated successfully, false otherwise.
	 */
    public boolean updateCreditCard(CreditCard cardData) {
        try {
            Path creditPath = Paths.get(creditCardsFile);
            if (!Files.exists(creditPath)) {
                System.out.println("Credit cards file not found.");
                return false;
            }

            java.util.List<String> lines = Files.readAllLines(creditPath);
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("#");
                if (parts[0].equals("CreditCard") && parts.length >= 10) {
                    boolean active = parts.length >= 11 ? Boolean.parseBoolean(parts[4]) : true;
                    int holderNameIndex = parts.length >= 11 ? 5 : 4;
                    int userIdIndex = parts.length >= 11 ? 6 : 5;
                    int creditLimitIndex = parts.length >= 11 ? 7 : 6;
                    int availableLimitIndex = parts.length >= 11 ? 8 : 7;
                    int debtIndex = parts.length >= 11 ? 9 : 8;
                    int paymentDayIndex = parts.length >= 11 ? 10 : 9;

                    long storedUserId = Long.parseLong(parts[userIdIndex]);
                    if (storedUserId == cardData.getUserId()) {
                        lines.set(i,
                            "CreditCard#" + cardData.getCardNumber() + "#" + cardData.getCvv() + "#" + cardData.getExpiryDate() + "#" + cardData.isActive() + "#" + cardData.getHolderName()
                            + "#" + cardData.getUserId() + "#" + cardData.getCreditLimit() + "#" + cardData.getAvailableLimit() + "#" + cardData.getDebt() + "#" + cardData.getPaymentDay()
                        );
                        Files.write(creditPath, lines);
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("An error occurred while updating the credit card: " + e.getMessage());
        }

        return false;
    }

}
