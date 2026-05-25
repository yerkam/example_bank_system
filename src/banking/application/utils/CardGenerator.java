package banking.application.utils;

import java.security.SecureRandom;
import java.time.LocalDate;

import banking.infrastructure.FileHandler;


/**
 * Utility class for generating card details such as card numbers, CVV, and expiry dates.
 */
public class CardGenerator {
	
	/**
     * Generates a unique 16-digit card number.
     * @return A unique 16-digit card number as a String.
     */
	public String generateCardNumber() {
		SecureRandom random = new SecureRandom();
        StringBuilder cardNumber = new StringBuilder();
        
        // Generate 16 digits
        for (int i = 0; i < 16; i++) {
            cardNumber.append(random.nextInt(10));
        }
        
        String generatedNumber = cardNumber.toString();
        
        return generatedNumber;
    }
	
	

	
	/**
     * Generates a random 3-digit CVV number.
     * @return A 3-digit CVV as a String.
     */
    public String generateCVV() {
    	SecureRandom random = new SecureRandom();
        int cvv = random.nextInt(1000); // Range: 000-999
        return String.format("%03d", cvv); // Format as 3 digits with leading zeros if necessary
    }

    
    /**
     * Generates an expiry date for a card (5 years from now).
     * @return Expiry date in MM/YY format.
     */
    public String generateExpiryDate() {
    	LocalDate now = LocalDate.now();
        LocalDate expiryDate = now.plusYears(5);
        int month = expiryDate.getMonthValue();
        int year = expiryDate.getYear() % 100; // Last 2 digits of year
        return String.format("%02d/%02d", month, year);
    }
}
