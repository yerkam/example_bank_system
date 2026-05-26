package banking.domain.accounts;

import java.time.LocalDate;

public class DepositAccount extends Account {

    private LocalDate expiryDate;
    private int months;

    public DepositAccount(int accountNumber, double balance, LocalDate expiryDate, long userId, int months) {

        super(accountNumber, balance, userId);
        this.expiryDate = expiryDate;
        this.months = months;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }
    
    public int getMonths() {
		return months;
	}
}