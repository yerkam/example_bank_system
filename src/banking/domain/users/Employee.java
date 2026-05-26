package banking.domain.users;

public class Employee extends User {
	
	public Employee(long id, String name, String surname, String hashedPassword) {
		super(id, name, surname, hashedPassword, "EMPLOYEE");
	}
}
