package banking.domain.cards;

import java.time.LocalDate;

public class DebitCard extends Card {
	
	private int linkedAccountIBAN;
	
	public DebitCard(String cardNumber, String cvv, String expiryDate, boolean active, String holderName, int linkedAccountIBAN) {
		super(cardNumber, cvv, expiryDate, active, holderName);
		this.linkedAccountIBAN = linkedAccountIBAN;
	}
	
	public int getLinkedAccountIBAN() {
		return linkedAccountIBAN;
	}

}
