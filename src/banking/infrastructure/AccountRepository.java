package banking.infrastructure;

import java.nio.file.Path;

import banking.domain.accounts.Account;
import banking.domain.accounts.CheckingAccount;
import banking.domain.accounts.CurrencyAccount;
import banking.domain.accounts.DepositAccount;

/**
 * The AccountRepository interface defines the contract for managing user accounts in the banking application.
 * It provides methods for ensuring user files, retrieving account information, adjusting balances, and saving accounts.
 */
public interface AccountRepository {

	// Ensures that a user file exists for the given user details. If the file does not exist, it creates one.
	void ensureUserFile(String name, String surname, long id, String password);

	
    int getAccountTypeCount(long userId, String accountType);
    
    
    int hasDebitCardIBAN(long userId, int accountNumber);
    
    boolean adjustFirstActiveCheckingBalance(long userId, String accountsFolderPath, double amount);
    
    void saveCheckingAccount(long userId, CheckingAccount account);

	void saveDepositAccount(long userId, DepositAccount account);
	
	void saveCurrencyAccount(long userId, CurrencyAccount account);
}
