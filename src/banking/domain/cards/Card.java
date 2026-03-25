package banking.domain.cards;

import java.time.LocalDate;

public abstract class Card {

	private String cardNumber;
	private String cvv;
	private String expiryDate;
	private boolean active;
	private String holderName;
	
	public Card(String cardNumber, String cvv, String expiryDate, boolean active, String holderName) {
		this.cardNumber = cardNumber;
		this.cvv = cvv;
		this.expiryDate = expiryDate;
		this.active = active;
		this.holderName = holderName;
	}
	
	public String getCvv() {
		return cvv;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public String getHolderName() {
		return holderName;
	}

	public void deactivateCard() {
		active = false;
	}
	
	public void activateCard() {
		active = true;
	}
	
//	public boolean isExpired() {
////		return LocalDate.now().isAfter(expiryDate);
//	}
	
	public String getCardNumber() {
		return cardNumber;
	}
}
