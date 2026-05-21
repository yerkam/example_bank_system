package banking.domain.accounts;

import java.time.LocalDate;

public class DepositAccount extends Account {

    private LocalDate expiryDate;

    public DepositAccount(
            int accountNumber,
            double balance,
            LocalDate expiryDate) {

        super(accountNumber, balance);
        this.expiryDate = expiryDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }
}