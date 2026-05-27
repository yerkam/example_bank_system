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
	
	// Singleton instance of FileHandler to manage file paths and operations
	private final FileHandler fileHandler = FileHandler.getInstance();
    private String accountsFolderPath = fileHandler.getAccountsFolderPath();
    private String creditCardsFile = fileHandler.getCreditCardsFile();
    private String loansFile = fileHandler.getLoansFile();
	
	
	// Constructor to initialize the BankFacade with the necessary managers and authentication
	public BankFacade(AccountManager accountManager, CardManager cardManager, Authentication authentication, AccountSecurityManager accountSecurityManager) {
		this.accountManager = accountManager;
		this.cardManager = cardManager;
		this.authentication = authentication;
		this.accountSecurityManager = accountSecurityManager;
		loanManager = new LoanManager(accountsFolderPath, creditCardsFile, loansFile);
	}
	
	// Account Creation Methods
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
	
	
	// Card Creation Methods
	public boolean userExists(long id, String role) {
		return accountManager.doesUserExist(id, role);
	}
	public void createCreditCard(long userId, String holderName, double creditLimit, int paymentDay) {
		cardManager.createCreditCard(userId, holderName, creditLimit, paymentDay);
	}
	
	public void createDebitCard(long userId, int accountNumber, String holderName) {
		cardManager.createDebitCard(userId, accountNumber, holderName);
	}
	
	/**
	 * Checks if an account with the given ID and password exists in the login details file.
	 * @param ID The user ID to check.
	 * @param password The password to check.
	 * @return true if the account exists, false otherwise.
	 */
	public User authenticateUser(long ID, String password, String role) {
		return authentication.authenticateUser(ID, password, role);
	}
	
	public Customer findCustomerById(long customerId) {
	    return authentication.findCustomerById(customerId);
}
	
	// Account Security Methods
	public void freezeAccount(long ID, int durationInDays) {
		accountSecurityManager.freezeAccount(ID, durationInDays);
	}
	
	public void createEmployeeAccount(String name, String surname, long id, String password) {
		accountManager.createEmployeeAccount(name, surname, id, password);
	}
	
	
	public AccountSummary getAccountSummary(long userId) {
	    return accountManager.getAccountSummary(userId);
	}
	
	// Loan Methods
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

}
