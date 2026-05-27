package banking.presentation.menu;

import java.util.Scanner;

import banking.application.BankFacade;
import banking.domain.users.Customer;
import banking.domain.users.User;
import banking.infrastructure.AccountSummary;
import banking.presentation.utils.AccountCreationHandler;
import banking.presentation.utils.CardCreationHandler;
import banking.presentation.utils.FindCustomerHandler;

public class EmployeeRoleStrategy implements RoleStrategy {
	static Scanner scanner = new Scanner(System.in);
	private BankFacade bankFacade;
	private AccountCreationHandler accountCreationHandler;
	private CardCreationHandler cardCreationHandler;
	private User loggedInUser;
	private FindCustomerHandler findCustomerHandler;
	
	public EmployeeRoleStrategy(BankFacade bankFacade, AccountCreationHandler accountCreationHandler,
			CardCreationHandler cardCreationHandler, User loggedInUser) {
		this.bankFacade = bankFacade;
		this.accountCreationHandler = accountCreationHandler;
		this.cardCreationHandler = cardCreationHandler;
		this.loggedInUser = loggedInUser;
		findCustomerHandler = new FindCustomerHandler(bankFacade);
	}
	
	
	@Override
	public void showMenu() {
		boolean menuChoice = false;
		while (!menuChoice) {
			System.out.println("-------------------------------------------------------------------------------------------------");
			System.out.println(" - You are logged in as an EMPLOYEE! You have access to the following functionalities: ");
			System.out.println("-------------------------------------------------------------------------------------------------");
			System.out.println(" - You can access the functionalities of the BANK APPLICATION using the following keys: ");
			System.out.println(" -- 'CC'      : Enter the letter 'CC' to Create a new checking account for the customer...");
			System.out.println(" -- 'CD'      : Enter the letter 'CD' to Create a new deposit account for the customer...");
			System.out.println(" -- 'CCU'     : Enter the letter 'CCU' to Create a new currency account for the customer...");
			System.out.println(" -- 'CCA'     : Enter the letter 'CCA' to Create a new credit card for the customer...");
			System.out.println(" -- 'DCA'     : Enter the letter 'DCA' to Create a new debit card for the customer...");
			System.out.println(" -- 'VA'      : Enter the letter 'VA' to View your account details for the customer...");
			System.out.println(" -- 'TL'      : Enter the letters 'TL' to Take loans for the customer...");
			System.out.println(" -- 'RL'      : Enter the letters 'TL' to Repay loans for the customer...");
			System.out.println(" -- 'CS'      : Enter the letters 'TL' to View your credit score for the customer...");
			System.out.println(" -- 'EXIT'    : Enter the letters 'EXIT' to Exit the program...");
			System.out.println("-------------------------------------------------------------------------------------------------");
			
			//We can add more functionalities to the EMPLOYEE menu as needed...
			
			System.out.print(" - Your choice: ");
			String choice = scanner.nextLine().trim().toUpperCase();
			
			//Add functions for each case as needed...
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
				        int accountNumber = Integer.parseInt(scanner.nextLine());
				        System.out.print("Loan amount: "); 
				        double amount = Double.parseDouble(scanner.nextLine());
				        System.out.print("Loan term (months): ");
				        int months = Integer.parseInt(scanner.nextLine());
	
				        String result = bankFacade.requestLoan(customer.getUserId(), accountNumber, amount, months);
				        System.out.println(result);
	
				    } 
					catch (NumberFormatException e) {
				        System.out.println("Invalid input.");
				    }
					break;
				case "RL":
					customer = findCustomerHandler.promptCustomer();
					try {
				        System.out.print("Repayment amount: ");
				        double amount = Double.parseDouble(scanner.nextLine());
	
				        String result = bankFacade.repayLoan(customer.getUserId(), amount);
				        System.out.println(result);
	
				    } 
					catch (NumberFormatException e) {
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
