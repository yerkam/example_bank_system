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
import banking.application.factories.UserFactory;
import banking.application.utils.IBANGenerator;
import banking.domain.accounts.CheckingAccount;
import banking.domain.accounts.CurrencyAccount;
import banking.domain.accounts.DepositAccount;
import banking.domain.users.Customer;
import banking.domain.users.Employee;
import banking.infrastructure.AccountRepository;
import banking.infrastructure.FileHandler;
import banking.infrastructure.UserRepository;

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
    private UserRepository userRepository;
    
    public AccountManager(AccountRepository accountRepository, UserRepository userRepository, IBANGenerator ibanGenerator) {
		this.userRepository = userRepository;
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
    public void createCheckingAccount(String name, String surname, long userId, String password, double balance, boolean active) {
    	if (!userRepository.userExists(userId, "CUSTOMER")) {
			Customer customer = UserFactory.createCustomer(userId, name, surname, password);
			userRepository.saveUser(customer);
		}
    	
    	
    	int accountNumber = accountRepository.getAccountTypeCount(userId, "Checking") + 1;
		int iban = ibanGenerator.generateIBAN();
		
		CheckingAccount account = AccountFactory.createCheckingAccount(accountNumber, balance, active, iban, userId);
		accountRepository.saveCheckingAccount(userId, account);
		
		
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
    public void createDepositAccount(String name, String surname, long userId, String password, double balance, int months) {
    	if (!userRepository.userExists(userId, "CUSTOMER")) {
			Customer customer = UserFactory.createCustomer(userId, name, surname, password);
			userRepository.saveUser(customer);
		}
    	
    	int accountNumber = accountRepository.getAccountTypeCount(userId, "Deposit") + 1;
    	LocalDate expiryDate = LocalDate.now().plusMonths(months);
    	
    	DepositAccount account = AccountFactory.createDepositAccount(accountNumber, balance, expiryDate, userId, months);
    	accountRepository.saveDepositAccount(userId, account);
    	
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
    public void createCurrencyAccount(String name, String surname, long userId, String password, double balance, String currency) {
    	if (!currency.equals("USD") && !currency.equals("EUR")) {
            System.out.println("Invalid currency type. Only USD and EUR are supported. Account creation failed.");
            return;
        }
    	
    	if (!userRepository.userExists(userId, "CUSTOMER")) {
			Customer customer = UserFactory.createCustomer(userId, name, surname, password);
			userRepository.saveUser(customer);
		}
    	
		int accountNumber = accountRepository.getAccountTypeCount(userId, "Currency") + 1;
		
		CurrencyAccount account = AccountFactory.createCurrencyAccount(accountNumber, balance, currency, userId);
		accountRepository.saveCurrencyAccount(userId, account);
		
	}
    
    public void createEmployeeAccount(String name, String surname, long id, String password) {
		if (userRepository.userExists(id, "EMPLOYEE")) {
			System.out.println("An employee with this ID already exists. Account creation failed.");
			return;
		}
		
		Employee employee = UserFactory.createEmployee(id, name, surname, password);
		userRepository.saveUser(employee);
	}
    
    public long generateID(String role) {
		long newID = userRepository.getNextUserId(role);
		return newID;
	}
    
    public boolean doesUserExist(long id, String role) {
    	return userRepository.userExists(id, role.toUpperCase());
    }
    

}
