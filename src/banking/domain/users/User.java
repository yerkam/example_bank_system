package banking.domain.users;

public abstract class User {
	protected long id;
	protected String name;
	protected String surname;
	protected String hashedPassword;
	protected String role;
	
	public User(long id, String name, String surname, String hashedPassword, String role) {
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.hashedPassword = hashedPassword;
		this.role = role;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public String getHashedPassword() {
		return hashedPassword;
	}

	public String getRole() {
		return role;
	}
	
	
}
