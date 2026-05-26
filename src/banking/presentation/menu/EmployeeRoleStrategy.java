package banking.presentation.menu;

import java.util.Scanner;

public class EmployeeRoleStrategy implements RoleStrategy {
	static Scanner scanner = new Scanner(System.in);
	
	
	@Override
	public void showMenu() {
		boolean menuChoice = false;
		while (!menuChoice) {
			System.out.println("-------------------------------------------------------------------------------------------------");
			System.out.println(" - You are logged in as an EMPLOYEE! You have access to the following functionalities: ");
			System.out.println("-------------------------------------------------------------------------------------------------");
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
}
