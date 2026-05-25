package banking.application.factories;

import banking.domain.cards.CreditCard;
import banking.domain.cards.DebitCard;

public class CardFactory {
	public static DebitCard createDebitCard(String cardNumber, String cvv, String expiryDate, String holderName, int iban) {

        return new DebitCard(cardNumber, cvv, expiryDate, true, holderName, iban);
    }

    public static CreditCard createCreditCard(String cardNumber, String cvv, String expiryDate, String holderName, 
    		long userId, double creditLimit, int paymentDay) {

        return new CreditCard(cardNumber, cvv, expiryDate, true, holderName, userId,
                creditLimit, creditLimit, 0.0, paymentDay);
    }
}
