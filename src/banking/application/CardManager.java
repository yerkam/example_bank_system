package banking.application;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.time.LocalDate;

import banking.domain.cards.Card;
import banking.domain.cards.CreditCard;
import banking.domain.cards.DebitCard;
import banking.infrastructure.AccountRepo;
import banking.infrastructure.CardRepo;

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
    private final AccountRepo accountRepo = new AccountRepo();
    private final CardRepo cardRepo = new CardRepo();

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
    	// Check in debit cards (user files)
    	boolean debit = accountRepo.isCardNumberExistsDebit(cardNumber, accountsFolderPath);
    	
    	// Check in credit cards
    	boolean credit = cardRepo.isCardNumberExistsCredit(cardNumber, creditCardsFile);
    	
    	if(debit || credit) {
    		return true;
    	}
    	else {
    		return false;
    	}
    	
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
        
        int IBAN = accountRepo.hasDebitCard(userId, path, accountNumber);
        if (IBAN == 0) {
			System.out.println("This checking account already has a debit card. Card creation failed.");
			return null;
		}
        else {
        	String generatedCardNumber = null;
        	generatedCardNumber = generateCardNumber();
            String cvv = generateCVV();
            String expiryDate = generateExpiryDate();
            
            DebitCard newCard = new DebitCard(generatedCardNumber, cvv, expiryDate, true, holderName, IBAN);
            
            
            if (cardRepo.saveDebitCard(newCard, path, accountNumber)) {
				return generatedCardNumber;
			}
			else {
				System.out.println("An error occurred while creating the debit card. Card creation failed.");
				return null;
			}
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

        if (cardRepo.isCreditCardExists(userId, creditCardsFile)) {
            System.out.println("A credit card already exists for user ID " + userId + ". Card creation failed.");
            return null;
        }
        
        String cardNumber = generateCardNumber();
        String cvv = generateCVV();
        String expiryDate = generateExpiryDate();
        double availableLimit = creditLimit;
        double debt = 0.0;
        
        
        CreditCard newCard = new CreditCard(cardNumber, cvv, expiryDate, true, holderName, userId, creditLimit, availableLimit, debt, paymentDay);
        

        if (cardRepo.saveCreditCard(newCard, creditCardsFile)) {
        	return cardNumber;
        }
        else {
			System.out.println("An error occurred while creating the credit card. Card creation failed.");
			return null;
		}
    }

   

}
