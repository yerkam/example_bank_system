package banking.presentation.utils;

import java.util.Scanner;

import banking.application.BankFacade;

public class CardCreationHandler {
	static Scanner scanner = new Scanner(System.in);
	private BankFacade bankFacade;
	
	public CardCreationHandler(BankFacade bankFacade) {
		this.bankFacade = bankFacade;
	}
	
	/**
	 * Handles the credit card creation process by prompting the user for their details and
	 * creating a new credit card using the CardManager.
	 */
	public void createCreditCard() {
		boolean validInput = false; String firstName = ""; String lastName = ""; long ID = 0; double creditLimit = 0.0; int paymentDay = 0;
		System.out.println("Please enter your details to create a new credit card.");
		
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
		
		validInput = false;
		while (!validInput) {
			System.out.println("---------------------------------------------------------");
			try {
				System.out.print("Initial Account Balance: ");
				ID = Long.parseLong(scanner.nextLine().trim());
				if (bankFacade.userExists(ID, "CUSTOMER")) {
					validInput = true;
				} else {
					System.out.println("Account ID cannot be found. Please try again.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid Account ID format! Account Balance must be a number.");
			}
			
		}
		
		validInput = false;
		while (!validInput) {
			System.out.println("---------------------------------------------------------");
			try {
				System.out.print("Payment Day: ");
				paymentDay = Integer.parseInt(scanner.nextLine().trim());
				if (paymentDay < 0) {
					System.out.println("Payment day cannot be negative. Please try again.");
				} else {
					validInput = true;
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid Payment day format! Payment day must be a number.");
			}
			
		}
		
		validInput = false;
		while (!validInput) {
			System.out.println("---------------------------------------------------------");
			try {
				System.out.print("Credit Limit: ");
				creditLimit = Double.parseDouble(scanner.nextLine().trim());
				if (creditLimit < 0) {
					System.out.println("Account Credit Limit cannot be negative. Please try again.");
				} else {
					validInput = true;
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid Account Credit Limit format! Account Credit Limit must be a number.");
			}
			
		}
		
		bankFacade.createCreditCard(ID, firstName + " " + lastName, creditLimit, paymentDay);
	}
	
	
}
