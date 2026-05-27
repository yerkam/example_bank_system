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
			System.out.println(" - You can access the functionalities of the BANK APPLICATION using the following keys: ");
			System.out.println(" -- 'CC'      : Enter the letter 'CC' to Create a new checking account...");
			System.out.println(" -- 'CD'      : Enter the letter 'CD' to Create a new deposit account...");
			System.out.println(" -- 'CCU'     : Enter the letter 'CCU' to Create a new currency account...");
			System.out.println(" -- 'CCA'     : Enter the letter 'CCA' to Create a new credit card...");
			System.out.println(" -- 'DCA'     : Enter the letter 'DCA' to Create a new debit card...");
			System.out.println(" -- 'VA'      : Enter the letter 'VA' to View your account details...");
			System.out.println(" -- 'TL'      : Enter the letters 'TL' to Take loans...");
			System.out.println(" -- 'RL'      : Enter the letters 'TL' to Repay loans...");
			System.out.println(" -- 'CS'      : Enter the letters 'TL' to View your credit score...");
			System.out.println(" -- 'EXIT'    : Enter the letters 'EXIT' to Exit the program...");
			System.out.println("-------------------------------------------------------------------------------------------------");
			
			//We can add more functionalities to the CUSTOMER menu as needed...
			
			System.out.print(" - Your choice: ");
			String choice = scanner.nextLine().trim().toUpperCase();
			
			//Add functions for each case as needed...
			switch (choice) {
				case "CC":
					accountCreationHandler.createCheckingAccount(loggedInUser);
					System.out.println("Checking account created successfully...");
					break;
				case "CD":
					accountCreationHandler.createDepositAccount(loggedInUser);
					System.out.println("Deposit account created successfully...");
					break;
				case "CCU":
					accountCreationHandler.createCurrencyAccount(loggedInUser);
					System.out.println("Currency account created successfully...");
					break;
				case "CCA":
					cardCreationHandler.createCreditCard(loggedInUser);
					break;
				case "DCA":
					cardCreationHandler.createDebitCard(loggedInUser);
					break;
				case "VA":
					AccountSummary summary = bankFacade.getAccountSummary(loggedInUser.getUserId());
					
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
					try {
						System.out.print("Checking Account Number: ");
				        int accountNumber = Integer.parseInt(scanner.nextLine());
				        System.out.print("Loan amount: "); 
				        double amount = Double.parseDouble(scanner.nextLine());
				        System.out.print("Loan term (months): ");
				        int months = Integer.parseInt(scanner.nextLine());

				        String result = bankFacade.requestLoan(loggedInUser.getUserId(), accountNumber, amount, months);
				        System.out.println(result);

				    } 
					catch (NumberFormatException e) {
				        System.out.println("Invalid input.");
				    }
					break;
				case "RL":
					try {
				        System.out.print("Repayment amount: ");
				        double amount = Double.parseDouble(scanner.nextLine());

				        String result = bankFacade.repayLoan(loggedInUser.getUserId(), amount);
				        System.out.println(result);

				    } 
					catch (NumberFormatException e) {
				        System.out.println("Invalid amount.");
				    }
					break;
				case "CS":
					int score = bankFacade.getCreditScore(loggedInUser.getUserId());

				    String band = bankFacade.getCreditScoreBand(loggedInUser.getUserId());

				    System.out.println("Credit Score: " + score);
				    System.out.println("Rating: " + band);
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
