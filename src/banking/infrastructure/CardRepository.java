package banking.infrastructure;

import banking.domain.cards.CreditCard;
import banking.domain.cards.DebitCard;

/**
 * Interface for card-related data operations. Implemented by FileCardRepository to handle file-based storage.
 */
public interface CardRepository {
	boolean saveDebitCard(DebitCard card, long userId, int accountNumber);

    boolean saveCreditCard(CreditCard card);

    boolean isCardNumberExistsCredit(String cardNumber);
    
    boolean isCardNumberExistsDebit(String cardNumber);
    
    boolean isCreditCardExists(long userId);

    CreditCard findCreditCardByUserId(long userId);

    boolean updateCreditCard(CreditCard card);
}
