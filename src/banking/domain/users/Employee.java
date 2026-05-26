package banking.domain.users;

public class Employee extends User {
	
	public Employee(long userId, String name, String surname, String hashedPassword) {
		super(userId, name, surname, hashedPassword, "EMPLOYEE");
	}
}
