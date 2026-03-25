package banking.presentation;

import java.util.Scanner;

public class Menu {
	
	static Scanner scanner = new Scanner(System.in);
	
	public Menu(String loginEntity) {
		switch (loginEntity) {
			case "customer":
				CustomerMenu();
				break;
			case "employee":
				EmployeeMenu();
				break;
			case "manager":
				ManagerMenu();
				break;
			default:
				System.out.println("Invalid login entity. Exiting.");
				System.exit(0);
		}
	}

	public void CustomerMenu() {
		boolean menuChoice = false;
		while (!menuChoice) {
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
			
			String choice = scanner.nextLine().trim();
			
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
	
	public void EmployeeMenu() {
		boolean menuChoice = false;
		while (!menuChoice) {
			System.out.println(" - You can access the functionalities of the BANK APPLICATION using the following keys: ");
			System.out.println(" -- 'CC'      : Enter the letter 'CC' to Create a new checking account...");
			System.out.println(" -- 'CD'      : Enter the letter 'CD' to Create a new deposit account...");
			System.out.println(" -- 'CCU'     : Enter the letter 'CCU' to Create a new currency account...");
			//We can add more functionalities to the EMPLOYEE menu as needed...
			
			String choice = scanner.nextLine().trim();
			
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
	
	public void ManagerMenu() {
		boolean menuChoice = false;
		while (!menuChoice) {
			System.out.println(" - You can access the functionalities of the BANK APPLICATION using the following keys: ");
			System.out.println(" -- 'CC'      : Enter the letter 'CC' to Create a new checking account...");
			System.out.println(" -- 'CD'      : Enter the letter 'CD' to Create a new deposit account...");
			System.out.println(" -- 'CCU'     : Enter the letter 'CCU' to Create a new currency account...");
			//We can add more functionalities to the CUSTOMER menu as needed...
			
			String choice = scanner.nextLine().trim();
			
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
