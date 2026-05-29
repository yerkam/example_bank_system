package banking.presentation;

import java.util.Scanner;

import banking.application.BankFacade;
import banking.domain.users.User;
import banking.presentation.menu.CustomerRoleStrategy;
import banking.presentation.menu.EmployeeRoleStrategy;
import banking.presentation.menu.ManagerRoleStrategy;
import banking.presentation.menu.RoleContext;
import banking.presentation.menu.RoleStrategy;
import banking.presentation.utils.AccountCreationHandler;
import banking.presentation.utils.CardCreationHandler;

/**
 * Handles user authentication and account creation for the banking system.
 */
public class Login {

	static Scanner scanner = new Scanner(System.in);
	private BankFacade bankFacade;
	private AccountCreationHandler accountCreationHandler;
	private CardCreationHandler cardCreationHandler;

	public Login(BankFacade bankFacade) {
		this.bankFacade = bankFacade;
		accountCreationHandler = new AccountCreationHandler(bankFacade);
		cardCreationHandler = new CardCreationHandler(bankFacade);

		System.out.println("--- Welcome to the Banking System! ---");
		boolean loginChoice = false;
		boolean personnelType = false;
		String loginEntity = "";

		while (!personnelType) {
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
			} else {
				loginProcess(loginEntity);
				loginChoice = true;
			}
		}
	}

	public void loginProcess(String loginEntity) {
		System.out.println("Please enter your ID and password to log in.");
		long ID = 0;
		boolean loggedIn = false;
		int failedAttempts = 0;

		while (failedAttempts < 3 && !loggedIn) {

			boolean validID = false;
			while (!validID) {
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
			while (!validPassword) {
				System.out.println("---------------------------------------------------------");
				System.out.print("Password (must be 6 digits): ");
				password = scanner.nextLine().trim();
				if (password.matches("\\d{6}")) {
					validPassword = true;
				} else {
					System.out.println("Invalid format! Password must be exactly 6 digits.");
				}
			}

			User loggedInUser = bankFacade.authenticateUser(ID, password, loginEntity.toUpperCase());
			if (loggedInUser != null) {
				System.out.println("Login successful!");
				loggedIn = true;

				RoleStrategy roleStrategy;
				switch (loginEntity) {
					case "customer":
						roleStrategy = new CustomerRoleStrategy(bankFacade, accountCreationHandler, cardCreationHandler, loggedInUser);
						break;
					case "employee":
						roleStrategy = new EmployeeRoleStrategy(bankFacade, accountCreationHandler, cardCreationHandler, loggedInUser);
						break;
					case "manager":
						roleStrategy = new ManagerRoleStrategy(bankFacade, accountCreationHandler, cardCreationHandler, loggedInUser);
						break;
					default:
						System.out.println("Invalid login entity. Exiting.");
						System.exit(0);
						return;
				}
				RoleContext roleContext = new RoleContext(roleStrategy);
				roleContext.showMenu();

			} else {
				failedAttempts++;
				if (failedAttempts < 3) {
					System.out.println("Login failed! Please try again. (" + failedAttempts + "/3 attempts used)");
				} else {
					// FIX #4: freezeAccount(userId, accountNumber) — use account number 1001
					// (first/primary checking account) as per the use case in the report.
					String reactivationPwd = bankFacade.freezeAccount(ID, 1001);
					System.out.println("You have entered the wrong password 3 times!");
					System.out.println("For your security, your primary checking account has been frozen.");
					if (reactivationPwd != null) {
						System.out.println("Reactivation password generated. Please contact bank personnel.");
					}
					System.out.println("Please contact bank personnel to reactivate your account.");
				}
			}
		}
	}

	public void createAccount() {
		accountCreationHandler.registerCustomer();
		System.out.println("Account created successfully! Please log in with your new credentials.");
	}
}
