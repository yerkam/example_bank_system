package banking.application.factories;

import banking.domain.users.Customer;
import banking.domain.users.Employee;

public class UserFactory {
	public static Customer createCustomer(long id, String name, String surname, String hashedPassword) {
		return new Customer(id, name, surname, hashedPassword);
	}
	
	public static Employee createEmployee(long id, String name, String surname, String hashedPassword) {
		return new Employee(id, name, surname, hashedPassword);
	}
	
	
}
