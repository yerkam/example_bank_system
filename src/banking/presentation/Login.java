package banking.presentation;

import java.util.Scanner;

import banking.application.BankFacade;
import banking.infrastructure.AccountRepository;
import banking.presentation.menu.CustomerRoleStrategy;
import banking.presentation.menu.EmployeeRoleStrategy;
import banking.presentation.menu.ManagerRoleStrategy;
import banking.presentation.menu.RoleContext;
import banking.presentation.menu.RoleStrategy;
import banking.presentation.utils.AccountCreationHandler;

/**
 * The login class handles user authentication and account creation for the banking system.
 * It provides a simple command-line interface for users to log in or create a new account.
 */
public class Login {
	
	static Scanner scanner = new Scanner(System.in);
	// The BankFacade is used to interact with the underlying application logic for authentication and account management. 
	// Can be found in the banking.application package. It serves as a single point of access to the various functionalities 
	// of the banking system, such as account creation, card management, and authentication.
	private BankFacade bankFacade;
	private AccountCreationHandler accountCreationHandler;
	
	/**
	 * Displays the login menu and handles user input for logging in or creating a new account.
	 * The user can choose to log in as a customer, bank employee, or bank manager, or exit the system.
	 */
	public Login(BankFacade bankFacade) {	
		this.bankFacade = bankFacade;
		accountCreationHandler = new AccountCreationHandler(bankFacade);
		
		System.out.println("--- Welcome to the Banking System! ---");
		boolean loginChoice = false;
		boolean personnelType = false; 
		String loginEntity = "";
		
		
		
		
		while(!personnelType) { 
			System.out.println("---------------------------------------------------------");
			System.out.println("A. CUSTOMER LOGIN");
			System.out.println("B. BANK EMPLOYEE LOGIN");
			System.out.println("C. BANK MANAGER LOGIN");
			System.out.println("D. Exit");
			System.out.print("Please select an option (A, B, C, D): ");
			
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
				System.out.println("---------------------------------------------------------");
				System.out.println("1. Log in");
				System.out.println("2. Register for a new account");
				System.out.println("3. Exit");
				System.out.print("Please select an option (1, 2, 3): ");
				
				String choice = scanner.nextLine().trim();
				
				switch (choice) {
					case "1":
//						System.out.println("---------------------------------------------------------");
//						System.out.println("Welcome to the Banking System!");
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
		
		System.out.println("Please enter your ID and password to log in.");
		long ID = 0; boolean loggedIn = false; int failedAttempts = 0;
		
		while(failedAttempts < 3 && !loggedIn) {
			
			boolean validID = false;
			
			while(!validID) {
				try {
					System.out.println("---------------------------------------------------------");
					System.out.print("ID: ");
					ID = Long.parseLong(scanner.nextLine().trim());
					validID = true;
				} catch (NumberFormatException e) {
					System.out.println("Invalid ID format! ID must be a number.");
				}
			}
			
			boolean validPassword = false;
			String password = "";
			while(!validPassword) {
				System.out.println("---------------------------------------------------------");
				System.out.println("Password (must be 6 digits):");
				password = scanner.nextLine().trim();
				if (password.matches("\\d{6}")) {
					validPassword = true;
				} 
				else {
					System.out.println("Invalid format! Password must be exactly 6 digits.");
				}
			}
			
			
			if (password.matches("\\d{6}")) {
            	if (bankFacade.doesAccountExist(ID, password)) {
        			System.out.println("Login successful!");
        			loggedIn = true;
        			
        			RoleStrategy roleStrategy;
        			switch (loginEntity) {
        				case "customer":
        					roleStrategy = new CustomerRoleStrategy(bankFacade, accountCreationHandler);
        					break;
        				case "employee":
        					roleStrategy = new EmployeeRoleStrategy(bankFacade, accountCreationHandler);
        					break;
        				case "manager":
        					roleStrategy = new ManagerRoleStrategy(bankFacade, accountCreationHandler);
        					break;
        				default:
        					System.out.println("Invalid login entity. Exiting.");
        					System.exit(0);
        					return; // This return is just to satisfy the compiler, it will never be reached.
        			}
        			RoleContext roleContext = new RoleContext(roleStrategy);
        			roleContext.showMenu();
        			
        		} 
            	else {
        			failedAttempts++;
        			if(failedAttempts < 3) {
        				System.out.println("Login failed! Please try again.");
        			}
        			else{
        				bankFacade.freezeAccount(ID, 1);
        				System.out.println("You have entered the wrong password 3 times!");
        				System.out.println("For your security, your primary checking account has been frozen.");
        				System.out.println("Please contact bank personnel to reactivate it.");
        			}
        		}
            } 
            else {
                System.out.println("Invalid format! Password must be exactly 6 digits.");
            }
			
		}
		
		
	}
	
	/**
	 * Handles the account creation process by prompting the user for their details and creating a new account using the AccountManager.
	 */
	public void createAccount() {
		accountCreationHandler.createCheckingAccount();
		System.out.println("Account created successfully! Please log in with your new credentials.");
	}	 
}
