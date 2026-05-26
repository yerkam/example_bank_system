package banking.infrastructure;

import banking.domain.users.User;

public interface UserRepository {
	
	void saveUser(User user);
	
	User findUserById(long id, String role);
	
	boolean validateCredentials(long id, String password, String role);
	
	boolean userExists(long id, String role);
	
	long getNextUserId(String role);
}
