package banking.domain.cards;

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

	/**
     * Karttan harcama yapıldığında borcu artırır, harcanabilir limiti düşürür.
     */
    public boolean chargeCard(double amount) {
        if (amount <= 0) return false;
        
        if (this.availableLimit >= amount) {
            this.debt += amount;
            this.availableLimit = this.creditLimit - this.debt; // Limiti güncelle
            return true;
        }
        System.out.println("Yetersiz kart limiti!");
        return false;
    }

    /**
     * Kredi kartı borcu ödendiğinde borcu düşürür, harcanabilir limiti artırır.
     */
    public void payDebt(double amount) {
        if (amount <= 0) return;
        
        if (amount > this.debt) {
            this.debt = 0.0; // Borçtan fazla ödenirse borç sıfırlanır
        } else {
            this.debt -= amount;
        }
        this.availableLimit = this.creditLimit - this.debt; // Limiti yeniden hesapla
    }

	/**
	 * Kredili limit artırımı yapar. Borç değişmez, kullanılabilir limit aynı miktarda artar.
	 */
	public boolean increaseCreditLimit(double amount) {
		if (amount <= 0) {
			return false;
		}

		this.creditLimit += amount;
		this.availableLimit += amount;
		return true;
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
