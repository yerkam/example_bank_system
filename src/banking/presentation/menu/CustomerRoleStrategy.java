package banking.presentation.menu;

import java.util.Scanner;

import banking.application.BankFacade;
import banking.presentation.utils.AccountCreationHandler;

public class CustomerRoleStrategy implements RoleStrategy {
	static Scanner scanner = new Scanner(System.in);
	private BankFacade bankFacade;
	private AccountCreationHandler accountCreationHandler;
	
	public CustomerRoleStrategy(BankFacade bankFacade, AccountCreationHandler accountCreationHandler) {
		this.bankFacade = bankFacade;
		this.accountCreationHandler = accountCreationHandler;
	}
	
	
	public void showMenu() {
		boolean menuChoice = false;
		while (!menuChoice) {
			System.out.println("-------------------------------------------------------------------------------------------------");
			System.out.println(" - You are logged in as a CUSTOMER! You have access to the following functionalities: ");
			System.out.println("-------------------------------------------------------------------------------------------------");
			System.out.println(" - You can access the functionalities of the BANK APPLICATION using the following keys: ");
			System.out.println(" -- 'CC'      : Enter the letter 'CC' to Create a new checking account...");
			System.out.println(" -- 'CD'      : Enter the letter 'CD' to Create a new deposit account...");
			System.out.println(" -- 'CCU'     : Enter the letter 'CCU' to Create a new currency account...");
			System.out.println(" -- 'VA'      : Enter the letter 'VA' to View your account details...");
			System.out.println(" -- 'VT'      : Enter the letter 'VT' to View your transaction history...");
			System.out.println(" -- 'TR'      : Enter the letters 'TR' to Transfer money...");
			System.out.println(" -- 'TL'      : Enter the letters 'TL' to Take loans...");
			System.out.println(" -- 'RSP'     : Enter the letters 'RSP' to Reset Password...");
			System.out.println(" -- 'EXIT'    : Enter the letters 'EXIT' to Exit the program...");
			System.out.println("-------------------------------------------------------------------------------------------------");
			
			//We can add more functionalities to the CUSTOMER menu as needed...
			
			System.out.print(" - Your choice: ");
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
				case "VA":
					
					break;
				case "VT":
					
					break;
				case "TR":
					
					break;
				case "TL":
					
					break;
				case "RSP":
					
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
