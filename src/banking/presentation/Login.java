package banking.presentation;

import java.util.Scanner;

import banking.application.AccountManager;
import banking.application.Authentication;
import banking.application.utils.IBANGenerator;
import banking.infrastructure.AccountRepository;
import banking.presentation.menu.CustomerRoleStrategy;
import banking.presentation.menu.EmployeeRoleStrategy;
import banking.presentation.menu.ManagerRoleStrategy;
import banking.presentation.menu.RoleContext;
import banking.presentation.menu.RoleStrategy;

/**
 * The login class handles user authentication and account creation for the banking system.
 * It provides a simple command-line interface for users to log in or create a new account.
 */
public class Login {
	
	static Scanner scanner = new Scanner(System.in);
	private AccountManager accountManager;
	private Authentication authentication;
	
	/**
	 * Displays the login menu and handles user input for logging in or creating a new account.
	 * The user can choose to log in as a customer, bank employee, or bank manager, or exit the system.
	 */
	public Login(AccountManager accountManager, Authentication authentication) {
		this.accountManager = accountManager;
		this.authentication = authentication;
		
		System.out.println("Welcome to the Banking System!");
		boolean loginChoice = false;
		boolean personnelType = false; 
		String loginEntity = "";
		
		
		
		
		while(!personnelType) { 
			System.out.println("Please select an option (A, B, C):");
			System.out.println("A. CUSTOMER LOGIN");
			System.out.println("B. BANK EMPLOYEE LOGIN");
			System.out.println("C. BANK MANAGER LOGIN");
			System.out.println("D. Exit");
			
			String choice = scanner.nextLine().trim().toLowerCase();
			
			switch (choice) {
				case "a":
					loginEntity = "customer";
					personnelType = true;
					break;
				case "b":
					loginEntity = "employee";
					personnelType = true;
					break;
				case "c":
					loginEntity = "manager";
					personnelType = true;
					break;
				case "d":
					System.out.println("Thank you for using the Banking System. Goodbye!");
					System.exit(0);
				default:
					System.out.println("Invalid option. Please try again.");
			}
		}
		
		
		while (!loginChoice) {
			if (loginEntity.equalsIgnoreCase("customer")) {
				System.out.println("Please select an option (1, 2, 3):");
				System.out.println("1. Log in ");
				System.out.println("2. Register for a new account");
				System.out.println("3. Exit");
				
				String choice = scanner.nextLine().trim();
				
				switch (choice) {
					case "1":
						loginProcess(loginEntity);
						loginChoice = true;
						break;
					case "2":
						createAccount();
						loginChoice = true;
						break;
					case "3":
						System.out.println("Thank you for using the Banking System. Goodbye!");
						System.exit(0);
					default:
						System.out.println("Invalid option. Please try again.");
				}
			}
			else {
				loginProcess(loginEntity);
				loginChoice = true;
			}
		}
	}
	
	
	/**
	 * Handles the user login process by prompting for user name and password.
	 * In a real implementation, this method would validate the credentials against stored user data and allow access to the banking system if the credentials are correct.
	 * @param loginEntity The type of user logging in (customer, employee, or manager) to tailor the login process accordingly.
	 */
	public void loginProcess(String loginEntity) {
		System.out.println("Welcome to the Banking System!");
		System.out.println("Please enter your ID and password to log in.");
		
		System.out.println("ID: ");
		long ID = Long.parseLong(scanner.nextLine().trim());

		// Freeze account after 3 failed login attempts
		int failedAttempts = 0;
        boolean loggedIn = false;
		
		String password = "";
        while (failedAttempts < 3 && !loggedIn) {
            System.out.println("Password (must be 6 digits):");
            password = scanner.nextLine().trim();
            // Is password exactly 6 digits? If so, break the loop
            if (password.matches("\\d{6}")) {
                break;
            } else {
                System.out.println("Invalid format! Password must be exactly 6 digits.");
            }
        }
		
		if (doesAccountExist(ID, password)) {
			System.out.println("Login successful!");
			loggedIn = true;
			
			RoleStrategy roleStrategy;
			switch (loginEntity) {
				case "customer":
					roleStrategy = new CustomerRoleStrategy();
					break;
				case "employee":
					roleStrategy = new EmployeeRoleStrategy();
					break;
				case "manager":
					roleStrategy = new ManagerRoleStrategy();
					break;
				default:
					System.out.println("Invalid login entity. Exiting.");
					System.exit(0);
					return; // This return is just to satisfy the compiler, it will never be reached.
			}
			RoleContext roleContext = new RoleContext(roleStrategy);
			roleContext.showMenu();
			
		} else {
			failedAttempts++;
			if(failedAttempts < 3) {
				System.out.println("Login failed! Please try again.");
			}else{
				banking.application.AccountSecurityManager.freezeAccount(ID, 1);
				System.out.println("You have entered the wrong password 3 times!");
				System.out.println("For your security, your primary checking account has been frozen.");
				System.out.println("Please contact bank personnel to reactivate it.");
			}
		}
	}
	
	/**
	 * Checks if an account with the given ID and password exists in the login details file.
	 * @param ID The user ID to check.
	 * @param password The password to check.
	 * @return true if the account exists, false otherwise.
	 */
	public boolean doesAccountExist(long ID, String password) {
		return authentication.checkAccount(ID, password);
    }
	
	/**
	 * Handles the account creation process by prompting the user for their details and creating a new account using the AccountManager.
	 */
	public void createAccount() {
		System.out.println("Welcome to the Banking System!");
		System.out.println("Please enter your details to create a new account.");
		
		System.out.println("First Name: ");
		String firstName = scanner.nextLine().trim();
		
		System.out.println("Last Name: ");
		String lastName = scanner.nextLine().trim();
		
		System.out.println("ID: ");
		long ID = Long.parseLong(scanner.nextLine().trim());
		
		String password = "";
        while (true) {
            System.out.println("Password (must be 6 digits):");
            password = scanner.nextLine().trim();
            // Şifre tam 6 rakamdan oluşuyorsa döngüden çık
            if (password.matches("\\d{6}")) {
                break;
            } else {
                System.out.println("Invalid format! Password must be exactly 6 digits.");
            }
        }

		System.out.println("Balance you want to deposit: ");
		double balance = Double.parseDouble(scanner.nextLine().trim());

		accountManager.createCheckingAccount(firstName, lastName, ID, password, balance, true);
	}	 
}
