package banking.application;

import java.io.File;
import java.util.Scanner;

import banking.domain.users.Customer;
import banking.domain.users.User;
import banking.infrastructure.FileHandler;
import banking.infrastructure.FileUserRepository;
import banking.infrastructure.UserRepository;

public class Authentication {
	
	private final FileHandler fileHandler = FileHandler.getInstance();
    private String accountsFolderPath = fileHandler.getAccountsFolderPath();
	private UserRepository userRepository;
	
	public Authentication(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	/**
     * Returns the file path for a specific user's account file.
     * @param id The user's ID.
     * @return The full path to the user's account file.
     */
    private String getUserFilePath(long id) {
        return accountsFolderPath + File.separator + id + ".txt";
    }

	  /**
	  * Checks if account credentials are valid by reading the first line of the user's file.
	  *
	  * @param id       Client's ID
	  * @param password Client's password
	  * @return true if the account exists and the password matches, false otherwise.
	  */
	 public boolean checkAccount(long id, String password) {
	     String filePath = getUserFilePath(id);
	     File file = new File(filePath);
	     if (!file.exists()) return false;
	
	     try (Scanner reader = new Scanner(file)) {
	         if (reader.hasNextLine()) {
	             String firstLine = reader.nextLine().trim();
	             if (!firstLine.isEmpty()) {
	                 String[] parts = firstLine.split("#");
	                 if (parts.length >= 3) {
	                     String storedPassword = parts[2];
	                     return storedPassword.equals(banking.infrastructure.SecurityUtil.hashText(password));
	                 }
	             }
	         }
	     } catch (Exception e) {
	         System.out.println("Error checking account: " + e.getMessage());
	     }
	     return false;
	 }
	 
	 public boolean login(long id, String password, String role) {
	     return userRepository.validateCredentials(id, password, role);
	 }
	 
	 public User authenticateUser(long id, String password, String role) {
		 boolean valid = userRepository.validateCredentials(id, password, role);
		 
		 if (!valid) {
			 return null;
		 }
		 
		 return userRepository.findUserById(id, role);
	 }
	 
	 public Customer findCustomerById(long customerId) {
		    User user = userRepository.findUserById(customerId, "CUSTOMER");
		    
		    if (user instanceof Customer customer) {
		        return customer;
		    }

		    return null;
	}

}
