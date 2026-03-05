package src;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.time.LocalDate;

/**
 * Manages debit and credit card operations.
 * 
 * Debit cards are stored inline in user account files (data/Accounts/{id}.txt)
 * alongside the checking account they are linked to:
 *   Checking#accountNum#balance#active#IBAN#cardNumber#cvv#cardExpiry#holderName
 * 
 * Credit cards are stored in a separate file (data/Credit Card/CreditCards.txt):
 *   CreditCard#cardNumber#cvv#expiryDate#holderName#userId#creditLimit#availableLimit#debt#paymentDay
 */
public class CardManager {
    private final String accountsFolderPath;
    private final String creditCardsFile;

    public CardManager(String accountsFolderPath, String creditCardsFile) {
        this.accountsFolderPath = accountsFolderPath;
        this.creditCardsFile = creditCardsFile;
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
     * Checks if a card number already exists in debit cards (user files) or credit cards.
     * @param cardNumber The card number to check.
     * @return true if the card number exists, false otherwise.
     */
    private boolean isCardNumberExists(String cardNumber) {
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

            // Check in credit cards file
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
        int cvv = random.nextInt(1000); // Range: 000-999
        return String.format("%03d", cvv); // Format as 3 digits with leading zeros if necessary
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
     * Creates a debit card linked to a specific checking account.
     * The card info is appended to the checking account line in the user's file.
     *
     * @param userId        The user's ID.
     * @param accountNumber The checking account number to link the card to.
     * @param holderName    The name of the cardholder.
     * @return The generated card number, or null if creation failed.
     */
    public String createDebitCard(long userId, int accountNumber, String holderName) {
        String filePath = accountsFolderPath + File.separator + userId + ".txt";
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            System.out.println("User file not found. Card creation failed.");
            return null;
        }

        try {
            java.util.List<String> lines = Files.readAllLines(path);
            String generatedCardNumber = null;

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("#");
                if (parts[0].equals("Checking") && parts.length >= 5) {
                    int storedAccountNumber = Integer.parseInt(parts[1]);
                    if (storedAccountNumber == accountNumber) {
                        if (parts.length > 5) {
                            System.out.println("This checking account already has a debit card. Card creation failed.");
                            return null;
                        }
                        generatedCardNumber = generateCardNumber();
                        String cvv = generateCVV();
                        String expiryDate = generateExpiryDate();
                        lines.set(i, line + "#" + generatedCardNumber + "#" + cvv + "#" + expiryDate + "#" + holderName);
                        break;
                    }
                }
            }

            if (generatedCardNumber == null) {
                System.out.println("Checking account #" + accountNumber + " not found. Card creation failed.");
                return null;
            }

            Files.write(path, lines);
            return generatedCardNumber;

        } catch (Exception e) {
            System.out.println("An error occurred while creating the debit card: " + e.getMessage());
            return null;
        }
    }


    /**
     * Creates a credit card. Stored in the Credit Card folder.
     *
     * @param userId      The user's ID.
     * @param holderName  The name of the cardholder.
     * @param creditLimit The credit limit for the card.
     * @param paymentDay  The day of the month for payment (1-28).
     * @return The generated card number, or null if creation failed.
     */
    public String createCreditCard(long userId, String holderName, double creditLimit, int paymentDay) {
        if (paymentDay < 1 || paymentDay > 28) {
            System.out.println("Invalid payment day. Must be between 1 and 28. Card creation failed.");
            return null;
        }

        if (isCreditCardExists(userId)) {
            System.out.println("A credit card already exists for user ID " + userId + ". Card creation failed.");
            return null;
        }

        try (FileWriter writer = new FileWriter(creditCardsFile, true)) {
            String cardNumber = generateCardNumber();
            String cvv = generateCVV();
            String expiryDate = generateExpiryDate();
            double availableLimit = creditLimit;
            double debt = 0.0;

            writer.write("CreditCard#" + cardNumber + "#" + cvv + "#" + expiryDate + "#" + holderName
                    + "#" + userId + "#" + creditLimit + "#" + availableLimit + "#" + debt + "#" + paymentDay + "\n");
            return cardNumber;
        } catch (Exception e) {
            System.out.println("An error occurred while creating the credit card: " + e.getMessage());
            return null;
        }
    }

    /**
     * Checks if a credit card already exists for a given user ID.
     * @param userId The user's ID.
     * @return true if a credit card exists for this user, false otherwise.
     */
    private boolean isCreditCardExists(long userId) {
        try {
            Path creditPath = Paths.get(creditCardsFile);
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

}
