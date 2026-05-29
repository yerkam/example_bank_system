package banking.application;

import banking.domain.users.Customer;
import banking.domain.users.User;
import banking.infrastructure.AccountSummary;
import banking.infrastructure.FileHandler;

public class BankFacade {

	private AccountManager accountManager;
	private CardManager cardManager;
	private Authentication authentication;
	private AccountSecurityManager accountSecurityManager;
	private LoanManager loanManager;
	private TransactionManager transactionManager;

	private final FileHandler fileHandler = FileHandler.getInstance();
	private String accountsFolderPath = fileHandler.getAccountsFolderPath();
	private String creditCardsFile = fileHandler.getCreditCardsFile();
	private String loansFile = fileHandler.getLoansFile();

	public BankFacade(AccountManager accountManager, CardManager cardManager,
			Authentication authentication, AccountSecurityManager accountSecurityManager) {
		this.accountManager = accountManager;
		this.cardManager = cardManager;
		this.authentication = authentication;
		this.accountSecurityManager = accountSecurityManager;
		this.loanManager = new LoanManager(accountsFolderPath, creditCardsFile, loansFile);
		this.transactionManager = new TransactionManager();
	}

	// Account Creation
	public void createCheckingAccount(String name, String surname, long userId, String password, double balance, boolean active) {
		accountManager.createCheckingAccount(name, surname, userId, password, balance, true);
	}

	public void createDepositAccount(String name, String surname, long userId, String password, double balance, int months) {
		accountManager.createDepositAccount(name, surname, userId, password, balance, months);
	}

	public void createCurrencyAccount(String name, String surname, long userId, String password, double balance, String currency) {
		accountManager.createCurrencyAccount(name, surname, userId, password, balance, currency);
	}

	public long generateID(String role) {
		return accountManager.generateID(role);
	}

	// Card Creation
	public boolean userExists(long id, String role) {
		return accountManager.doesUserExist(id, role);
	}

	public void createCreditCard(long userId, String holderName, double creditLimit, int paymentDay) {
		cardManager.createCreditCard(userId, holderName, creditLimit, paymentDay);
	}

	public void createDebitCard(long userId, int accountNumber, String holderName) {
		cardManager.createDebitCard(userId, accountNumber, holderName);
	}

	// Authentication
	public User authenticateUser(long ID, String password, String role) {
		return authentication.authenticateUser(ID, password, role);
	}

	public Customer findCustomerById(long customerId) {
		return authentication.findCustomerById(customerId);
	}

	// Account Security
	// FIX #4: signature corrected to (userId, accountNumber) matching AccountSecurityManager
	public String freezeAccount(long userId, int accountNumber) {
		return accountSecurityManager.freezeAccount(userId, accountNumber);
	}

	// FIX #2: expose reactivateAccount for Employee/Manager menus
	public boolean reactivateAccount(long userId, int accountNumber, String reactivationPassword) {
		return accountSecurityManager.reactivateAccount(userId, accountNumber, reactivationPassword);
	}

	// FIX #3: expose renewPassword for Customer menu
	public void renewPassword(long userId, String newPassword) {
		accountSecurityManager.renewPassword(userId, newPassword);
	}

	public void createEmployeeAccount(String name, String surname, long id, String password) {
		accountManager.createEmployeeAccount(name, surname, id, password);
	}

	// Account Summary
	public AccountSummary getAccountSummary(long userId) {
		return accountManager.getAccountSummary(userId);
	}

	// FIX #5: return detailed per-account lines so VA can show IBAN/account numbers
	public String[][] getAccountDetails(long userId) {
		return accountManager.getAccountDetails(userId);
	}

	// Loans
	public String requestLoan(long userId, int checkingAccountNumber, double amount, int termMonths) {
		return loanManager.requestLoan(userId, checkingAccountNumber, amount, termMonths);
	}

	public String repayLoan(long userId, double amount) {
		return loanManager.repayLoan(userId, amount);
	}

	public int getCreditScore(long userId) {
		return loanManager.calculateCreditScore(userId);
	}

	public String getCreditScoreBand(long userId) {
		return loanManager.getCreditScoreBand(userId);
	}

	// FIX #1: wire TransactionManager through the facade
	public void makeTransaction(long senderId, int senderCheckingAccountNumber, int iban, double amount) {
		transactionManager.makeTransaction(senderId, senderCheckingAccountNumber, iban, amount);
	}

	public String[][] getTransactionHistory(long userId) {
		return transactionManager.getTransactionHistory(userId);
	}
}
