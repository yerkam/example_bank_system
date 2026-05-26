package banking.presentation.menu;

import java.util.Scanner;

import banking.application.BankFacade;
import banking.domain.users.User;
import banking.presentation.utils.AccountCreationHandler;

public class EmployeeRoleStrategy implements RoleStrategy {
	static Scanner scanner = new Scanner(System.in);
	private BankFacade bankFacade;
	private AccountCreationHandler accountCreationHandler;
	private User loggedInUser;
	
	public EmployeeRoleStrategy(BankFacade bankFacade, AccountCreationHandler accountCreationHandler, User loggedInUser) {
		this.bankFacade = bankFacade;
		this.accountCreationHandler = accountCreationHandler;
		this.loggedInUser = loggedInUser;
	}
	
	
	@Override
	public void showMenu() {
		boolean menuChoice = false;
		while (!menuChoice) {
			System.out.println("-------------------------------------------------------------------------------------------------");
			System.out.println(" - You are logged in as an EMPLOYEE! You have access to the following functionalities: ");
			System.out.println("-------------------------------------------------------------------------------------------------");
			System.out.println(" - You can access the functionalities of the BANK APPLICATION using the following keys: ");
			System.out.println(" -- 'CC'      : Enter the letter 'CC' to Create a new checking account for the customer...");
			System.out.println(" -- 'CD'      : Enter the letter 'CD' to Create a new deposit account for the customer...");
			System.out.println(" -- 'CCU'     : Enter the letter 'CCU' to Create a new currency account for the customer...");
			System.out.println(" -- 'EXIT'    : Enter the letters 'EXIT' to Exit the program...");
			System.out.println("-------------------------------------------------------------------------------------------------");
			
			//We can add more functionalities to the EMPLOYEE menu as needed...
			
			System.out.print(" - Your choice: ");
			String choice = scanner.nextLine().trim().toUpperCase();
			
			//Add functions for each case as needed...
			switch (choice) {
				case "CC":
//					accountCreationHandler.createCheckingAccountByBank();
					System.out.println("Checking account created successfully...");
					break;
				case "CD":
//					accountCreationHandler.createDepositAccount(loggedInUser);
					System.out.println("Deposit account created successfully...");
					break;
				case "CCU":
//					accountCreationHandler.createCurrencyAccount(loggedInUser);
					System.out.println("Currency account created successfully...");
					break;
				case "EXIT":
					menuChoice = true;
					System.out.println("Thank you for using the Banking System. Goodbye!");
					System.exit(0);
				default:
					System.out.println("Invalid option. Please try again.");
			}
		}
	}
}
