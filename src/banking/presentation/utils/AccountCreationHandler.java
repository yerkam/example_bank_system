package banking.presentation.utils;

import java.util.Scanner;

import banking.application.BankFacade;
import banking.domain.users.User;

public class AccountCreationHandler {
	static Scanner scanner = new Scanner(System.in);
	private BankFacade bankFacade;
	
	public AccountCreationHandler(BankFacade bankFacade) {
		this.bankFacade = bankFacade;
	}
	
	
	/**
	 * Handles the CUSTOMER account creation process by prompting the user at the login page and 
	 * creating a new CUSTOMER account using the User Factory.
	 */
	public void registerCustomer() {
	    boolean validInput = false;

	    String firstName = ""; String lastName = ""; String password = ""; double balance = 0.0; long userID = 0;

	    System.out.println("Please enter your details to register.");
	    while (!validInput) {
			System.out.println("--------------------------");
			System.out.print("Customer's First Name: ");
			firstName = scanner.nextLine().trim();
			if (firstName.isEmpty()) {
				System.out.println("Customer's First name cannot be empty. Please try again.");
			}
			else {
				validInput = true;
			}
		}

	    validInput = false;
		while (!validInput) {
			System.out.println("--------------------------");
			System.out.print("Customer's Last Name: ");
			lastName = scanner.nextLine().trim();
			if (lastName.isEmpty()) {
				System.out.println("Customer's Last name cannot be empty. Please try again.");
			}
			else {
				validInput = true;
			}
		}

		System.out.println("---------------------------------------------------------");
		userID = bankFacade.generateID("CUSTOMER");
		System.out.println("Generated Customer Account ID: " + userID);
		
		validInput = false;
		while(!validInput) {
			System.out.println("---------------------------------------------------------");
			System.out.print("Account Password (must be 6 digits): ");
            password = scanner.nextLine().trim();
            // Şifre tam 6 rakamdan oluşuyorsa döngüden çık
            if (password.matches("\\d{6}")) {
                validInput = true;
            } else {
                System.out.println("Invalid format! Account Password must be exactly 6 digits.");
            }
		}

		validInput = false;
		while (!validInput) {
			System.out.println("---------------------------------------------------------");
			try {
				System.out.print("Initial Account Balance: ");
				balance = Double.parseDouble(scanner.nextLine().trim());
				if (balance < 0) {
					System.out.println("Account Balance cannot be negative. Please try again.");
				} else {
					validInput = true;
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid Account balance format! Account Balance must be a number.");
			}
			
		}

	    bankFacade.createCheckingAccount(firstName, lastName, userID, password, balance, true);
	}
	
	/**
	 * Handles the account creation process by prompting the user for their details and
	 * creating a new checking account using the AccountManager.
	 */
	public void createCheckingAccount(User user) {
		boolean validInput = false; double balance = 0.0;
		System.out.println("Please enter your details to create a new account.");
		
		validInput = false;
		while (!validInput) {
			System.out.println("---------------------------------------------------------");
			try {
				System.out.print("Initial Account Balance: ");
				balance = Double.parseDouble(scanner.nextLine().trim());
				if (balance < 0) {
					System.out.println("Account Balance cannot be negative. Please try again.");
				} else {
					validInput = true;
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid Account balance format! Account Balance must be a number.");
			}
			
		}
		
		bankFacade.createCheckingAccount(user.getName(), user.getSurname(), user.getUserId(), user.getHashedPassword(), balance, true);
	}
	
	
	/**
	 * Handles the account creation process by prompting the user for their details and 
	 * creating a new deposit account using the AccountManager.
	 */
	public void createDepositAccount(User user) {
		boolean validInput = false; double balance = 0.0;
		int months = 0;
		System.out.println("Please enter your details to create a new account.");
		
		validInput = false;
		while (!validInput) {
			System.out.println("---------------------------------------------------------");
			try {
				System.out.print("Initial Balance: ");
				balance = Double.parseDouble(scanner.nextLine().trim());
				if (balance < 0) {
					System.out.println("Account Balance cannot be negative. Please try again.");
				} else {
					validInput = true;
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid account balance format! Account Balance must be a number.");
			}
			
		}
		
		validInput = false;
		while (!validInput) {
			System.out.println("---------------------------------------------------------");
			try {
				System.out.print("Number of Months for Deposit: ");
				months = Integer.parseInt(scanner.nextLine().trim());
				if (months < 1) {
					System.out.println("Number of months cannot be less than 1. Please try again.");
				} else {
					validInput = true;
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid months format! Months must be a number.");
			}
			
		}
		
		bankFacade.createDepositAccount(user.getName(), user.getSurname(), user.getUserId(), user.getHashedPassword(), balance, months);
	}
	
	
	/**
	 * Handles the account creation process by prompting the user for their details and 
	 * creating a new currency account using the AccountManager.
	 */
	public void createCurrencyAccount(User user) {
		boolean validInput = false; double balance = 0.0;
		String currency = "";
		System.out.println("Please enter your details to create a new account.");
		
		validInput = false;
		while (!validInput) {
			System.out.println("---------------------------------------------------------");
			try {
				System.out.print("Initial Account Balance: ");
				balance = Double.parseDouble(scanner.nextLine().trim());
				if (balance < 0) {
					System.out.println("Account Balance cannot be negative. Please try again.");
				} else {
					validInput = true;
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid account balance format! Account Balance must be a number.");
			}
			
		}
		
		validInput = false;
		while (!validInput) {
			System.out.println("---------------------------------------------------------");
			System.out.print("Currency ('USD' or 'EUR'): ");
            currency = scanner.nextLine().trim().toUpperCase();
            // Şifre tam 6 rakamdan oluşuyorsa döngüden çık
            if (currency.equals("USD") || currency.equals("EUR")) {
                validInput = true;
            } else {
                System.out.println("Invalid format! Currency must be exactly USD or EUR.");
            }
			
		}
		
		bankFacade.createCurrencyAccount(user.getName(), user.getSurname(), user.getUserId(), user.getHashedPassword(), balance, currency);
	}
	
	
	/**
	 * Handles the EMPLOYEE account creation process by prompting the MANAGER for their employee's details and 
	 * creating a new EMPLOYEE account using the User Factory.
	 */
	public void createEmployeeAccount() {
		boolean validInput = false; String firstName = ""; String lastName = ""; long ID = 0; String password = ""; double balance = 0.0;
		String currency = "";
		System.out.println("Please enter your details to create a new account.");
		
		while (!validInput) {
			System.out.println("--------------------------");
			System.out.print("Employee's First Name: ");
			firstName = scanner.nextLine().trim();
			if (firstName.isEmpty()) {
				System.out.println("EMployee's First name cannot be empty. Please try again.");
			}
			else {
				validInput = true;
			}
		}
		
		validInput = false;
		while (!validInput) {
			System.out.println("--------------------------");
			System.out.print("EMployee's Last Name: ");
			lastName = scanner.nextLine().trim();
			if (lastName.isEmpty()) {
				System.out.println("EMployee's Last name cannot be empty. Please try again.");
			}
			else {
				validInput = true;
			}
		}
		
		System.out.println("---------------------------------------------------------");
		ID = bankFacade.generateID("EMPLOYEE");
		System.out.println("Generated EMPLOYEE Account ID: " + ID);
		
		validInput = false;
		while(!validInput) {
			System.out.println("---------------------------------------------------------");
			System.out.print("Account Password (must be 6 digits): ");
            password = scanner.nextLine().trim();
            // Şifre tam 6 rakamdan oluşuyorsa döngüden çık
            if (password.matches("\\d{6}")) {
                validInput = true;
            } else {
                System.out.println("Invalid format! Account Password must be exactly 6 digits.");
            }
		}
		
		bankFacade.createEmployeeAccount(firstName, lastName, ID, password);
	}
	
	
	
}
