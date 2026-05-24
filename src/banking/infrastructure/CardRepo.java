package banking.infrastructure;

import banking.domain.cards.CreditCard;
import banking.domain.cards.DebitCard;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CardRepo {

    /**
     * Saves the debit card data to the specified file path.
     * @param cardData The debit card data to save.
     * @param filePath The path to the file where the card data should be saved.
     * @param accountNumber The account number associated with the debit card.
     * @return true if the card was saved successfully, false otherwise.
     */
    public boolean saveDebitCard(DebitCard cardData, Path filePath, int accountNumber) {
        boolean saveCard = false;
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
                            System.out.println("This checking account already has a debit card. Card creation failed.");
                            return false;
                        }
                        lines.set(i, line + "#" + cardData.getCardNumber() + "#" + cardData.getCvv() + "#" + cardData.getExpiryDate() + "#" + cardData.getHolderName());
                        saveCard = true;
                        break;
                    }
                }
            }

            Files.write(filePath, lines);

        } catch (Exception e) {
            System.out.println("An error occurred while saving the debit card: " + e.getMessage());
            saveCard = false;
        }

        return saveCard;
    }

    /**
     * Checks if a credit card already exists for a given user ID.
     * @param userId The user's ID.
     * @return true if a credit card exists for this user, false otherwise.
     */
    public boolean isCreditCardExists(long userId, String filePath) {
        try {
            Path creditPath = Paths.get(filePath);
            if (Files.exists(creditPath)) {
                for (String line : Files.readAllLines(creditPath)) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = line.split("#");
                    if (parts.length >= 6) {
                        long storedUserId = Long.parseLong(parts[5]);
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

    public boolean saveCreditCard(CreditCard cardData, String filePath) {
        boolean saveCard = false;
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write("CreditCard#" + cardData.getCardNumber() + "#" + cardData.getCvv() + "#" + cardData.getExpiryDate() + "#" + cardData.isActive() + "#" + cardData.getHolderName()
                    + "#" + cardData.getUserId() + "#" + cardData.getCreditLimit() + "#" + cardData.getAvailableLimit() + "#" + cardData.getDebt() + "#" + cardData.getPaymentDay() + "\n");
            saveCard = true;
        } catch (Exception e) {
            System.out.println("An error occurred while saving the credit card: " + e.getMessage());
            saveCard = false;
        }
        return saveCard;
    }

    public CreditCard findCreditCardByUserId(long userId, String filePath) {
        try {
            Path creditPath = Paths.get(filePath);
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

    public boolean updateCreditCard(CreditCard cardData, String filePath) {
        try {
            Path creditPath = Paths.get(filePath);
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

    /**
     * Checks if a card number already exists in credit cards.
     * @param cardNumber The card number to check.
     * @return true if the card number exists, false otherwise.
     */
    public boolean isCardNumberExistsCredit(String cardNumber, String creditCardsFile) {
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

}
