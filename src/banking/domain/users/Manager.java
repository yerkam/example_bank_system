package banking.domain.users;

public class Manager extends User {
	
	public Manager(long userId, String name, String surname, String hashedPassword) {
		super(userId, name, surname, hashedPassword, "MANAGER");
	}
}
