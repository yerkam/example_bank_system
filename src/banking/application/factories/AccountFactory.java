package banking.application.factories;

import java.time.LocalDate;

import banking.domain.accounts.CheckingAccount;
import banking.domain.accounts.CurrencyAccount;
import banking.domain.accounts.DepositAccount;

/**
 * Factory class for creating different types of accounts.
 */
public class AccountFactory {
	public static CheckingAccount createCheckingAccount(int accountNumber, double balance, boolean active, int iban,
			String name, String surname, String password, long id) {

        return new CheckingAccount(accountNumber, balance, active, iban, name, surname, password, id);
    }

    public static CurrencyAccount createCurrencyAccount(int accountNumber, double balance, String currency,
    		String name, String surname, String password, long id) {

        return new CurrencyAccount(accountNumber, balance, currency, name, surname, password, id);
    }

    public static DepositAccount createDepositAccount(int accountNumber, double balance, LocalDate expiryDate,
    		String name, String surname, String password, long id, int months) {

        return new DepositAccount(accountNumber, balance, expiryDate, name, surname, password, id, months);
    }
}
