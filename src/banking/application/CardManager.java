package banking.application;

import banking.application.factories.CardFactory;
import banking.application.utils.CardGenerator;
import banking.domain.cards.CreditCard;
import banking.domain.cards.DebitCard;
import banking.infrastructure.AccountRepository;
import banking.infrastructure.CardRepository;



/**
 * Manages debit and credit card operations.
 * 
 * Debit cards are stored inline in user account files (data/Accounts/{id}.txt)
 * alongside the checking account they are linked to:
 *   Checking#accountNum#balance#active#IBAN#cardNumber#cvv#cardExpiry#holderName
 * 
 * Credit cards are stored in a separate file (data/Credit Card/CreditCards.txt):
 *   CreditCard#cardNumber#cvv#expiryDate#active#holderName#userId#creditLimit#availableLimit#debt#paymentDay
 */
public class CardManager {
	private CardRepository cardRepository;
    private AccountRepository accountRepository;
    private CardGenerator cardGenerator;;
    
    
    
    public CardManager(CardRepository cardRepository, AccountRepository accountRepository, CardGenerator cardGenerator) {
    	this.cardRepository = cardRepository;
		this.accountRepository = accountRepository;
		this.cardGenerator = cardGenerator;
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
	        System.out.println("Invalid payment day.");
	        return null;
	    }
	
	    if (cardRepository.isCreditCardExists(userId)) {
	        System.out.println("Credit card already exists.");
	        return null;
	    }
	
	    String cardNumber;

	    do {
	        cardNumber = cardGenerator.generateCardNumber();
	    }
	    while (isCardNumberExists(cardNumber));
	    
	    String cvv = cardGenerator.generateCVV();
	    String expiryDate = cardGenerator.generateExpiryDate();
	
	    CreditCard card = CardFactory.createCreditCard(cardNumber, cvv, expiryDate, holderName,
	            userId, creditLimit, paymentDay);
	
	    boolean saved = cardRepository.saveCreditCard(card);
	    if (saved) {
	    	return cardNumber;
	    }
	    else {
			System.out.println("An error occurred while creating the credit card. Card creation failed.");
			return null;
		}
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

        int iban = accountRepository.hasDebitCardIBAN(userId, accountNumber);

        if (iban == 0) {
            System.out.println("Debit card already exists.");
            return null;
        }

        String cardNumber;

	    do {
	        cardNumber = cardGenerator.generateCardNumber();
	    }
	    while (isCardNumberExists(cardNumber));

        DebitCard card = CardFactory.createDebitCard(cardNumber, cardGenerator.generateCVV(), cardGenerator.generateExpiryDate(),
                holderName, iban);

        boolean saved = cardRepository.saveDebitCard(card, userId, accountNumber);

        if (saved) {
			return cardNumber;
		}
		else {
			System.out.println("An error occurred while creating the debit card. Card creation failed.");
			return null;
		}
    }
    
    /**
     * Checks if a card number already exists in debit cards (user files) or credit cards.
     * @param cardNumber The card number to check.
     * @return true if the card number exists, false otherwise.
     */
    private boolean isCardNumberExists(String cardNumber) {
    	// Check in debit cards (user files)
    	boolean debit = cardRepository.isCardNumberExistsDebit(cardNumber);
    	
    	// Check in credit cards
    	boolean credit = cardRepository.isCardNumberExistsCredit(cardNumber);
    	
    	if(debit || credit) {
    		return true;
    	}
    	else {
    		return false;
    	}
    	
    }   

}
