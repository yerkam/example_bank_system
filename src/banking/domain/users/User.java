package banking.domain.users;

public abstract class User {
	protected long userId;
	protected String name;
	protected String surname;
	protected String hashedPassword;
	protected String role;
	
	public User(long userId, String name, String surname, String hashedPassword, String role) {
		this.userId = userId;
		this.name = name;
		this.surname = surname;
		this.hashedPassword = hashedPassword;
		this.role = role;
	}

	public long getUserId() {
		return userId;
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
