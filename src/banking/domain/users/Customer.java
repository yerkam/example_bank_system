package banking.domain.users;

public class Customer extends User {
	
	public Customer(long userId, String name, String surname, String hashedPassword) {
		super(userId, name, surname, hashedPassword, "CUSTOMER");
	}

}
