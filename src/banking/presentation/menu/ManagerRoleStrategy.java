package banking.presentation.menu;

import java.util.Scanner;

import banking.application.BankFacade;
import banking.domain.users.Customer;
import banking.domain.users.User;
import banking.infrastructure.AccountSummary;
import banking.presentation.utils.AccountCreationHandler;
import banking.presentation.utils.CardCreationHandler;
import banking.presentation.utils.FindCustomerHandler;

public class ManagerRoleStrategy implements RoleStrategy {
	static Scanner scanner = new Scanner(System.in);
	private BankFacade bankFacade;
	private AccountCreationHandler accountCreationHandler;
	private CardCreationHandler cardCreationHandler;
	private FindCustomerHandler findCustomerHandler;

	public ManagerRoleStrategy(BankFacade bankFacade, AccountCreationHandler accountCreationHandler,
			CardCreationHandler cardCreationHandler, User loggedInUser) {
		this.bankFacade = bankFacade;
		this.accountCreationHandler = accountCreationHandler;
		this.cardCreationHandler = cardCreationHandler;
		findCustomerHandler = new FindCustomerHandler(bankFacade);
	}

	public void showMenu() {
		boolean menuChoice = false;
		while (!menuChoice) {
			System.out.println("-------------------------------------------------------------------------------------------------");
			System.out.println(" - You are logged in as a MANAGER! You have access to the following functionalities: ");
			System.out.println("-------------------------------------------------------------------------------------------------");
			System.out.println(" -- 'CC'   : Create a new checking account for a customer");
			System.out.println(" -- 'CD'   : Create a new deposit account for a customer");
			System.out.println(" -- 'CCU'  : Create a new currency account for a customer");
			System.out.println(" -- 'CCA'  : Create a new credit card for a customer");
			System.out.println(" -- 'DCA'  : Create a new debit card for a customer");
			System.out.println(" -- 'VA'   : View customer account details");
			System.out.println(" -- 'TL'   : Process a loan for a customer");
			System.out.println(" -- 'RL'   : Repay a loan for a customer");
			System.out.println(" -- 'CS'   : View customer credit score");
			System.out.println(" -- 'RA'   : Reactivate a frozen customer account");   // FIX #2
			System.out.println(" -- 'CEA'  : Create a new employee account");
			System.out.println(" -- 'EXIT' : Exit the program");
			System.out.println("-------------------------------------------------------------------------------------------------");

			System.out.print(" - Your choice: ");
			String choice = scanner.nextLine().trim().toUpperCase();

			switch (choice) {
				case "CC":
					Customer customer = findCustomerHandler.promptCustomer();
					accountCreationHandler.createCheckingAccount(customer);
					break;

				case "CD":
					customer = findCustomerHandler.promptCustomer();
					accountCreationHandler.createDepositAccount(customer);
					break;

				case "CCU":
					customer = findCustomerHandler.promptCustomer();
					accountCreationHandler.createCurrencyAccount(customer);
					break;

				case "CCA":
					customer = findCustomerHandler.promptCustomer();
					cardCreationHandler.createCreditCard(customer);
					break;

				case "DCA":
					customer = findCustomerHandler.promptCustomer();
					cardCreationHandler.createDebitCard(customer);
					break;

				case "VA":
					customer = findCustomerHandler.promptCustomer();
					AccountSummary summary = bankFacade.getAccountSummary(customer.getUserId());
					System.out.println("------- ACCOUNT SUMMARY -------");
					System.out.println(" - Total Accounts: " + summary.getTotalAccounts());
					System.out.println(" - Checking Accounts: " + summary.getCheckingAccounts());
					System.out.println(" - Deposit Accounts: " + summary.getDepositAccounts());
					System.out.println(" - Currency Accounts: " + summary.getCurrencyAccounts());
					System.out.println(" - Debit Cards: " + summary.getDebitCards());
					System.out.println(" - Credit Cards: " + summary.getCreditCards());
					System.out.println(" - Total Balance: " + summary.getTotalBalance());
					break;

				case "TL":
					customer = findCustomerHandler.promptCustomer();
					try {
						System.out.print("Checking Account Number: ");
						int accountNumber = Integer.parseInt(scanner.nextLine().trim());
						System.out.print("Loan amount: ");
						double amount = Double.parseDouble(scanner.nextLine().trim());
						System.out.print("Loan term (months): ");
						int months = Integer.parseInt(scanner.nextLine().trim());
						String result = bankFacade.requestLoan(customer.getUserId(), accountNumber, amount, months);
						System.out.println(result);
					} catch (NumberFormatException e) {
						System.out.println("Invalid input.");
					}
					break;

				case "RL":
					customer = findCustomerHandler.promptCustomer();
					try {
						System.out.print("Repayment amount: ");
						double amount = Double.parseDouble(scanner.nextLine().trim());
						String result = bankFacade.repayLoan(customer.getUserId(), amount);
						System.out.println(result);
					} catch (NumberFormatException e) {
						System.out.println("Invalid amount.");
					}
					break;

				case "CS":
					customer = findCustomerHandler.promptCustomer();
					int score = bankFacade.getCreditScore(customer.getUserId());
					String band = bankFacade.getCreditScoreBand(customer.getUserId());
					System.out.println("Credit Score: " + score);
					System.out.println("Rating: " + band);
					break;

				// FIX #2: Reactivate frozen account
				case "RA":
					try {
						System.out.print("Customer ID: ");
						long customerId = Long.parseLong(scanner.nextLine().trim());
						System.out.print("Account Number to reactivate: ");
						int accNum = Integer.parseInt(scanner.nextLine().trim());
						System.out.print("Reactivation Password: ");
						String reactivationPwd = scanner.nextLine().trim();
						boolean success = bankFacade.reactivateAccount(customerId, accNum, reactivationPwd);
						if (success) {
							System.out.println("Account reactivated successfully.");
						} else {
							System.out.println("Reactivation failed. Check the ID, account number, and password.");
						}
					} catch (NumberFormatException e) {
						System.out.println("Invalid input.");
					}
					break;

				case "CEA":
					accountCreationHandler.createEmployeeAccount();
					System.out.println("Employee account created successfully.");
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
