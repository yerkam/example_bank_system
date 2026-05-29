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
	 * Handles the credit card creation process.
	 * FIX #6: Payment day validation now correctly enforces 1-28 (matching CardManager).
	 */
	public void createCreditCard(User user) {
		boolean validInput = false;
		double creditLimit = 0.0;
		int paymentDay = 0;
		System.out.println("Please enter your details to create a new credit card.");

		// FIX #6: was paymentDay >= 0, should be 1-28
		while (!validInput) {
			System.out.println("---------------------------------------------------------");
			try {
				System.out.print("Payment Day (1-28): ");
				paymentDay = Integer.parseInt(scanner.nextLine().trim());
				if (paymentDay < 1 || paymentDay > 28) {
					System.out.println("Payment day must be between 1 and 28. Please try again.");
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
					System.out.println("Credit Limit cannot be negative. Please try again.");
				} else {
					validInput = true;
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid Credit Limit format! Credit Limit must be a number.");
			}
		}

		bankFacade.createCreditCard(user.getUserId(), user.getName() + " " + user.getSurname(), creditLimit, paymentDay);
	}

	/**
	 * Handles the debit card creation process.
	 */
	public void createDebitCard(User user) {
		boolean validInput = false;
		int accountNumber = 0;
		System.out.println("Please enter your details to create a new debit card.");

		while (!validInput) {
			System.out.println("---------------------------------------------------------");
			try {
				System.out.print("Checking Account Number: ");
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
