package banking.presentation.menu;

import java.util.Scanner;

import banking.application.BankFacade;
import banking.presentation.utils.AccountCreationHandler;

public class ManagerRoleStrategy implements RoleStrategy {
	static Scanner scanner = new Scanner(System.in);
	private BankFacade bankFacade;
	private AccountCreationHandler accountCreationHandler;
	
	public ManagerRoleStrategy(BankFacade bankFacade, AccountCreationHandler accountCreationHandler) {
		this.bankFacade = bankFacade;
		this.accountCreationHandler = accountCreationHandler;
	}
	
	
	public void showMenu() {
		boolean menuChoice = false;
		while (!menuChoice) {
			System.out.println("-------------------------------------------------------------------------------------------------");
			System.out.println(" - You are logged in as a MANAGER! You have access to the following functionalities: ");
			System.out.println("-------------------------------------------------------------------------------------------------");
			System.out.println(" - You can access the functionalities of the BANK APPLICATION using the following keys: ");
			System.out.println(" -- 'CC'      : Enter the letter 'CC' to Create a new checking account for the customer...");
			System.out.println(" -- 'CD'      : Enter the letter 'CD' to Create a new deposit account for the customer...");
			System.out.println(" -- 'CCU'     : Enter the letter 'CCU' to Create a new currency account for the customer...");
			System.out.println(" -- 'CEA'     : Enter the letter 'CEA' to Create a new employee account...");
			//We can add more functionalities to the CUSTOMER menu as needed...
			
			String choice = scanner.nextLine().trim().toUpperCase();
			
			//Add functions for each case as needed...
			switch (choice) {
				case "CC":
					accountCreationHandler.createCheckingAccount();
					System.out.println("Checking account created successfully...");
					break;
				case "CD":
					accountCreationHandler.createDepositAccount();
					System.out.println("Deposit account created successfully...");
					break;
				case "CCU":
					accountCreationHandler.createCurrencyAccount();
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
