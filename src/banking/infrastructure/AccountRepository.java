package banking.infrastructure;

import java.nio.file.Path;

import banking.domain.accounts.Account;
import banking.domain.accounts.CheckingAccount;
import banking.domain.accounts.CurrencyAccount;
import banking.domain.accounts.DepositAccount;

public interface AccountRepository {

	void ensureUserFile(String name, String surname, long id, String password);

    int getAccountTypeCount(long userId, String accountType);
    
    int hasDebitCardIBAN(long userId, Path filePath, int accountNumber);
//    int findAvailableCheckingIBAN(long userId, Path filePath, int accountNumber);
    
    boolean adjustFirstActiveCheckingBalance(long userId, String accountsFolderPath, double amount);

    boolean isCardNumberExistsDebit(String cardNumber, String accountsFolderPath);
    
    void saveCheckingAccount(long userId, CheckingAccount account);

	void saveDepositAccount(long userId, DepositAccount account);
	
	void saveCurrencyAccount(long userId, CurrencyAccount account);
}
