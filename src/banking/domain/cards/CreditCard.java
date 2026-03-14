package banking.domain.cards;

import java.time.LocalDate;

public class CreditCard extends Card {
	
	private long userId;
	private double creditLimit;
	private double availableLimit;
	private double debt;
	private int paymentDay;
	
	public CreditCard(String cardNumber, String cvv, String expiryDate, boolean active, String holderName,
			long userId, double creditLimit, double availableLimit, double debt, int paymentDay) {
		super(cardNumber, cvv, expiryDate, active, holderName);
		this.userId = userId;
		this.creditLimit = creditLimit;
		this.availableLimit = availableLimit;
		this.debt = debt;
		this.paymentDay = paymentDay;
	}

	public long getUserId() {
		return userId;
	}

	public double getCreditLimit() {
		return creditLimit;
	}

	public double getAvailableLimit() {
		return availableLimit;
	}

	public double getDebt() {
		return debt;
	}

	public int getPaymentDay() {
		return paymentDay;
	}

}
