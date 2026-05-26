package banking.application;


public class BankFacade {
	
	private AccountManager accountManager;
	private CardManager cardManager;
	private Authentication authentication;
	private AccountSecurityManager accountSecurityManager;
	
	
	// Constructor to initialize the BankFacade with the necessary managers and authentication
	public BankFacade(AccountManager accountManager, CardManager cardManager, Authentication authentication, AccountSecurityManager accountSecurityManager) {
		this.accountManager = accountManager;
		this.cardManager = cardManager;
		this.authentication = authentication;
		this.accountSecurityManager = accountSecurityManager;
	}
	
	// Account Creation Methods
	public void createCheckingAccount(String name, String surname, long id, String password, double balance, boolean active) {
		accountManager.createCheckingAccount(name, surname, id, password, balance, true);
	}
	
	public void createDepositAccount(String name, String surname, long id, String password, double balance, int months) {
		accountManager.createDepositAccount(name, surname, id, password, balance, months);
	}
	
	public void createCurrencyAccount(String name, String surname, long id, String password, double balance, String currency) {
		accountManager.createCurrencyAccount(name, surname, id, password, balance, currency);
	}
	
	public long generateID(String role) {
		return accountManager.generateID(role);
	}
	
	
	// Card Creation Methods
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
	public boolean doesAccountExist(long ID, String password, String role) {
		return authentication.login(ID, password, role);
	}
	
	// Account Security Methods
	public void freezeAccount(long ID, int durationInDays) {
		accountSecurityManager.freezeAccount(ID, durationInDays);
	}
	
	public void createEmployeeAccount(String name, String surname, long id, String password) {
		accountManager.createEmployeeAccount(name, surname, id, password);
	}
	
	
	

}
