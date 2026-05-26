package banking.domain.users;

public class Manager extends User {
	
	public Manager(long id, String name, String surname, String hashedPassword) {
		super(id, name, surname, hashedPassword, "MANAGER");
	}
}
