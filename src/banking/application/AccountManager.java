package banking.application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Scanner;

import banking.application.factories.AccountFactory;
import banking.application.utils.IBANGenerator;
import banking.domain.accounts.CheckingAccount;
import banking.domain.accounts.CurrencyAccount;
import banking.domain.accounts.DepositAccount;
import banking.infrastructure.AccountRepository;
import banking.infrastructure.FileHandler;

/**
 * Manages account operations using per-user files.
 * Each user has their own file at data/Accounts/{id}.txt
 * 
 * File format:
 *   Line 1: name#surname#password
 *   Subsequent lines (multiple accounts of each type allowed):
 *     Checking#accountNumber#balance#active#IBAN
 *     Checking#accountNumber#balance#active#IBAN#cardNumber#cvv#cardExpiry#holderName  (with debit card)
 *     Deposit#accountNumber#balance#expiryDate
 *     Currency#accountNumber#balance#currencyType
 */
public class AccountManager {

    private AccountRepository accountRepository;
    private IBANGenerator ibanGenerator;
    
    public AccountManager(AccountRepository accountRepository, IBANGenerator ibanGenerator) {
		this.accountRepository = accountRepository;
		this.ibanGenerator = ibanGenerator;
	}
    
    
    /**
	 * Creates a checking account for the user.
	 * If the user file doesn't exist, it will be created.
	 * Multiple checking accounts are allowed.
	 *
	 * @param name     Client's name
	 * @param surname  Client's surname
	 * @param id       Client's ID
	 * @param password Client's password
	 * @param balance  Client's account balance
	 * @param active   Client's account status
	 */
    public void createCheckingAccount(String name, String surname, long id, String password, double balance, boolean active) {
		int accountNumber = accountRepository.getAccountTypeCount(id, "Checking") + 1;
		int iban = ibanGenerator.generateIBAN();
		
		CheckingAccount account = AccountFactory.createCheckingAccount(accountNumber, balance, active, iban, name, surname, password, id);
		accountRepository.saveCheckingAccount(id, account);
	}
    
    
    /**
	 * Creates a deposit account for the user.
	 * If the user file doesn't exist, it will be created.
	 * Multiple deposit accounts are allowed.
	 *
	 * @param name     Client's name
	 * @param surname  Client's surname
	 * @param id       Client's ID
	 * @param password Client's password
	 * @param balance  Client's account balance
	 * @param months   Client's account duration in months
	 */
    public void createDepositAccount(String name, String surname, long id, String password, double balance, int months) {
    	int accountNumber = accountRepository.getAccountTypeCount(id, "Deposit") + 1;
    	LocalDate expiryDate = LocalDate.now().plusMonths(months);
    	
    	DepositAccount account = AccountFactory.createDepositAccount(accountNumber, balance, expiryDate, name, surname, password, id, months);
    	accountRepository.saveDepositAccount(id, account);
    }
    
    
    /**
	 * Creates a currency account for the user.
	 * If the user file doesn't exist, it will be created.
	 * Multiple currency accounts are allowed.
	 *
	 * @param name     Client's name
	 * @param surname  Client's surname
	 * @param id       Client's ID
	 * @param password Client's password
	 * @param balance  Client's account balance
	 * @param months   Client's account currency type (USD or EUR)
	 */
    public void createCurrencyAccount(String name, String surname, long id, String password, double balance, String currency) {
    	if (!currency.equals("USD") && !currency.equals("EUR")) {
            System.out.println("Invalid currency type. Only USD and EUR are supported. Account creation failed.");
            return;
        }
		int accountNumber = accountRepository.getAccountTypeCount(id, "Currency") + 1;
		
		CurrencyAccount account = AccountFactory.createCurrencyAccount(accountNumber, balance, currency, name, surname, password, id);
		accountRepository.saveCurrencyAccount(id, account);
	}
    

}
