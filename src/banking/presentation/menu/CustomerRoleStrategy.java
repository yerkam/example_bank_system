package banking.presentation.menu;

import java.util.Scanner;

import banking.application.BankFacade;
import banking.domain.users.User;
import banking.infrastructure.AccountSummary;
import banking.presentation.utils.AccountCreationHandler;
import banking.presentation.utils.CardCreationHandler;

public class CustomerRoleStrategy implements RoleStrategy {
	static Scanner scanner = new Scanner(System.in);
	private BankFacade bankFacade;
	private AccountCreationHandler accountCreationHandler;
	private CardCreationHandler cardCreationHandler;
	private User loggedInUser;

	public CustomerRoleStrategy(BankFacade bankFacade, AccountCreationHandler accountCreationHandler,
			CardCreationHandler cardCreationHandler, User loggedInUser) {
		this.bankFacade = bankFacade;
		this.accountCreationHandler = accountCreationHandler;
		this.cardCreationHandler = cardCreationHandler;
		this.loggedInUser = loggedInUser;
	}

	public void showMenu() {
		boolean menuChoice = false;
		while (!menuChoice) {
			System.out.println("-------------------------------------------------------------------------------------------------");
			System.out.println(" - You are logged in as a CUSTOMER! You have access to the following functionalities: ");
			System.out.println("-------------------------------------------------------------------------------------------------");
			System.out.println(" -- 'CC'   : Create a new checking account");
			System.out.println(" -- 'CD'   : Create a new deposit account");
			System.out.println(" -- 'CCU'  : Create a new currency account");
			System.out.println(" -- 'CCA'  : Create a new credit card");
			System.out.println(" -- 'DCA'  : Create a new debit card");
			System.out.println(" -- 'VA'   : View your account details (with IBANs and account numbers)");
			System.out.println(" -- 'TM'   : Transfer money to another account via IBAN");   // FIX #1
			System.out.println(" -- 'TH'   : View your transaction history");                // FIX #1
			System.out.println(" -- 'TL'   : Take a loan");
			System.out.println(" -- 'RL'   : Repay a loan");
			System.out.println(" -- 'CS'   : View your credit score");
			System.out.println(" -- 'RP'   : Reset your password");                          // FIX #3
			System.out.println(" -- 'EXIT' : Exit the program");
			System.out.println("-------------------------------------------------------------------------------------------------");

			System.out.print(" - Your choice: ");
			String choice = scanner.nextLine().trim().toUpperCase();

			switch (choice) {
				case "CC":
					accountCreationHandler.createCheckingAccount(loggedInUser);
					System.out.println("Checking account created successfully.");
					break;

				case "CD":
					accountCreationHandler.createDepositAccount(loggedInUser);
					System.out.println("Deposit account created successfully.");
					break;

				case "CCU":
					accountCreationHandler.createCurrencyAccount(loggedInUser);
					System.out.println("Currency account created successfully.");
					break;

				case "CCA":
					cardCreationHandler.createCreditCard(loggedInUser);
					break;

				case "DCA":
					cardCreationHandler.createDebitCard(loggedInUser);
					break;

				// FIX #5: VA now shows IBAN, account number, balance per account
				case "VA":
					System.out.println("------- ACCOUNT DETAILS -------");
					String[][] details = bankFacade.getAccountDetails(loggedInUser.getUserId());
					if (details.length == 0) {
						System.out.println("No accounts found.");
					}
					for (String[] parts : details) {
						if (parts.length == 0) continue;
						switch (parts[0]) {
							case "Checking":
								// Checking#accNum#balance#active#IBAN[#card#cvv#expiry#holder]
								System.out.println(" [Checking Account]");
								System.out.println("   Account Number : " + parts[1]);
								System.out.println("   Balance        : " + parts[2]);
								System.out.println("   Active         : " + parts[3]);
								System.out.println("   IBAN           : " + parts[4]);
								if (parts.length > 5) {
									System.out.println("   Debit Card     : " + parts[5] + " (CVV: " + parts[6] + ", Expiry: " + parts[7] + ")");
								}
								break;
							case "Deposit":
								// Deposit#accNum#balance#expiryDate
								System.out.println(" [Deposit Account]");
								System.out.println("   Account Number : " + parts[1]);
								System.out.println("   Balance        : " + parts[2]);
								System.out.println("   Maturity Date  : " + parts[3]);
								break;
							case "Currency":
								// Currency#accNum#balance#currencyType
								System.out.println(" [Currency Account]");
								System.out.println("   Account Number : " + parts[1]);
								System.out.println("   Balance        : " + parts[2]);
								System.out.println("   Currency       : " + parts[3]);
								break;
						}
						System.out.println("   --------------------------------");
					}
					// Also show totals summary
					AccountSummary summary = bankFacade.getAccountSummary(loggedInUser.getUserId());
					System.out.println(" Total Balance : " + summary.getTotalBalance());
					System.out.println(" Credit Cards  : " + summary.getCreditCards());
					break;

				// FIX #1: Transfer money
				case "TM":
					try {
						System.out.print("Your Checking Account Number to send from: ");
						int senderAccNum = Integer.parseInt(scanner.nextLine().trim());
						System.out.print("Recipient IBAN: ");
						int recipientIBAN = Integer.parseInt(scanner.nextLine().trim());
						System.out.print("Amount to transfer: ");
						double amount = Double.parseDouble(scanner.nextLine().trim());
						bankFacade.makeTransaction(loggedInUser.getUserId(), senderAccNum, recipientIBAN, amount);
					} catch (NumberFormatException e) {
						System.out.println("Invalid input. Please enter valid numbers.");
					}
					break;

				// FIX #1: Transaction history
				case "TH":
					String[][] history = bankFacade.getTransactionHistory(loggedInUser.getUserId());
					if (history.length == 0) {
						System.out.println("No transaction history found.");
					} else {
						System.out.println("------- TRANSACTION HISTORY -------");
						for (String[] tx : history) {
							if (tx.length >= 3) {
								System.out.println(" Recipient ID: " + tx[0] + " | Amount: " + tx[1] + " | Date: " + tx[2]);
							}
						}
					}
					break;

				case "TL":
					try {
						System.out.print("Checking Account Number: ");
						int accountNumber = Integer.parseInt(scanner.nextLine().trim());
						System.out.print("Loan amount: ");
						double amount = Double.parseDouble(scanner.nextLine().trim());
						System.out.print("Loan term (months): ");
						int months = Integer.parseInt(scanner.nextLine().trim());
						String result = bankFacade.requestLoan(loggedInUser.getUserId(), accountNumber, amount, months);
						System.out.println(result);
					} catch (NumberFormatException e) {
						System.out.println("Invalid input.");
					}
					break;

				case "RL":
					try {
						System.out.print("Repayment amount: ");
						double amount = Double.parseDouble(scanner.nextLine().trim());
						String result = bankFacade.repayLoan(loggedInUser.getUserId(), amount);
						System.out.println(result);
					} catch (NumberFormatException e) {
						System.out.println("Invalid amount.");
					}
					break;

				case "CS":
					int score = bankFacade.getCreditScore(loggedInUser.getUserId());
					String band = bankFacade.getCreditScoreBand(loggedInUser.getUserId());
					System.out.println("Credit Score: " + score);
					System.out.println("Rating: " + band);
					break;

				// FIX #3: Reset password
				case "RP":
					boolean validPw = false;
					while (!validPw) {
						System.out.print("New Password (must be 6 digits): ");
						String newPw = scanner.nextLine().trim();
						if (newPw.matches("\\d{6}")) {
							bankFacade.renewPassword(loggedInUser.getUserId(), newPw);
							validPw = true;
						} else {
							System.out.println("Invalid format! Password must be exactly 6 digits.");
						}
					}
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
