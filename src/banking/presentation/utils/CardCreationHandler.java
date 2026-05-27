package banking.presentation.utils;

import java.util.Scanner;

import banking.application.BankFacade;
import banking.domain.users.User;

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
	public void createCreditCard(User user) {
		boolean validInput = false; String firstName = ""; String lastName = ""; long ID = 0; double creditLimit = 0.0; int paymentDay = 0;
		System.out.println("Please enter your details to create a new credit card.");
		
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
		
		bankFacade.createCreditCard(user.getUserId(), user.getName() + " " + user.getSurname(), creditLimit, paymentDay);
	}
	
	
	/**
	 * Handles the debit card creation process by prompting the user for their details and
	 * creating a new debit card using the CardManager.
	 */
	public void createDebitCard(User user) {
		boolean validInput = false; int accountNumber = 0;
		System.out.println("Please enter your details to create a new debit card.");
		
		validInput = false;
		while (!validInput) {
			System.out.println("---------------------------------------------------------");
			try {
				System.out.print("Account Number: ");
				accountNumber = Integer.parseInt(scanner.nextLine().trim());
				if (accountNumber < 1000) {
					System.out.println("Account number cannot be less than 1000. Please try again.");
				} else {
					validInput = true;
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid Account number format! Account number must be a number.");
			}
			
		}
		
		
		bankFacade.createDebitCard(user.getUserId(), accountNumber, user.getName() + " " + user.getSurname());
	}
	
	
	
}
