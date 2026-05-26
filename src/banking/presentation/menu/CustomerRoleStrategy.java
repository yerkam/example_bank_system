package banking.presentation.menu;

import java.util.Scanner;

import banking.application.BankFacade;

public class CustomerRoleStrategy implements RoleStrategy {
	static Scanner scanner = new Scanner(System.in);
	private BankFacade bankFacade;
	
	public CustomerRoleStrategy(BankFacade bankFacade) {
		this.bankFacade = bankFacade;
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
			
			//We can add more functionalities to the CUSTOMER menu as needed...
			
			String choice = scanner.nextLine().trim().toUpperCase();
			
			//Add functions for each case as needed...
			switch (choice) {
				case "1":
					menuChoice = true;
					break;
				case "2":
					menuChoice = true;
					break;
				case "3":
					System.out.println("Thank you for using the Banking System. Goodbye!");
					System.exit(0);
				default:
					System.out.println("Invalid option. Please try again.");
			}
		}
	}
}
