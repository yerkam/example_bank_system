package banking.domain.users;

public class Customer extends User {
	
	public Customer(long id, String name, String surname, String hashedPassword) {
		super(id, name, surname, hashedPassword, "CUSTOMER");
	}

}
