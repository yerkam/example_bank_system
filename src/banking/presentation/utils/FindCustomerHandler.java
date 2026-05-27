package banking.presentation.utils;

import java.util.Scanner;

import banking.application.BankFacade;
import banking.domain.users.Customer;

public class FindCustomerHandler {
	private BankFacade bankFacade;
    private Scanner scanner = new Scanner(System.in);

    public FindCustomerHandler(BankFacade bankFacade) {
        this.bankFacade = bankFacade;
    }

    public Customer promptCustomer() {
        try {
            System.out.print("Enter Customer ID: ");
            long customerId = Long.parseLong(scanner.nextLine());

            Customer customer = bankFacade.findCustomerById(customerId);

            if (customer == null) {
                System.out.println("Customer not found.");
                return null;
            }
            return customer;
        }
        catch (NumberFormatException e) {
            System.out.println("Invalid customer ID.");
            return null;
        }
    }
}
