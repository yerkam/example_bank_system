package banking.application.factories;

import java.time.LocalDate;

import banking.domain.accounts.CheckingAccount;
import banking.domain.accounts.CurrencyAccount;
import banking.domain.accounts.DepositAccount;

/**
 * Factory class for creating different types of accounts.
 */
public class AccountFactory {
	public static CheckingAccount createCheckingAccount(int accountNumber, double balance, boolean active, int iban, long userId) {
        return new CheckingAccount(accountNumber, balance, active, iban, userId);
    }

    public static CurrencyAccount createCurrencyAccount(int accountNumber, double balance, String currency, long userId) {
        return new CurrencyAccount(accountNumber, balance, currency, userId);
    }

    public static DepositAccount createDepositAccount(int accountNumber, double balance, LocalDate expiryDate, long userId, int months) {
        return new DepositAccount(accountNumber, balance, expiryDate, userId, months);
    }
}
