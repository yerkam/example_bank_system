package banking.infrastructure;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import banking.domain.users.Customer;
import banking.domain.users.Employee;
import banking.domain.users.Manager;
import banking.domain.users.User;

public class FileUserRepository implements UserRepository {
	
	// Singleton instance of FileHandler to manage file paths and operations
	private final FileHandler fileHandler = FileHandler.getInstance();
    private String accountsFolderPath = fileHandler.getAccountsFolderPath();
    private String customersfilepath = fileHandler.getCustomersFile();
    private String employeesfilepath = fileHandler.getEmployeesFile();
    private String managersfilepath = fileHandler.getManagersFile();
	private String usersFolderPath = fileHandler.getUsersFolderPath();
	
	@Override
	public void saveUser(User user) {
		String filePath = getRoleFilePath(user.getRole());
	    try (FileWriter writer = new FileWriter(filePath, true)) {
	        writer.write(
	            user.getId() + "#" + user.getName() + "#" + user.getSurname() + "#" +
	            SecurityUtil.hashText(user.getHashedPassword()) + "#" + user.getRole() + "\n");

	    } 
	    catch (Exception e) {
	        System.out.println("Error saving user: " + e.getMessage());
	    }
	}
	
	
	public User findUserById(long id, String role) {
	    String filePath = getRoleFilePath(role);
	    Path path = Paths.get(filePath);
	    try {
	        if (!Files.exists(path)) {
	            return null;
	        }
	        for (String line : Files.readAllLines(path)) {
	            String[] parts = line.split("#");
	            long storedId = Long.parseLong(parts[0]);
	            if (storedId == id) {
	                switch (role.toUpperCase()) {
	                    case "CUSTOMER":
	                        return new Customer(storedId, parts[1], parts[2], parts[3]);
	                    case "EMPLOYEE":
	                        return new Employee(storedId, parts[1], parts[2], parts[3]);
	                    case "MANAGER":
	                        return new Manager(storedId, parts[1], parts[2], parts[3]);
	                    default:
	                        return null;
	                }
	            }
	        }

	    } 
	    catch (Exception e) {
	        System.out.println("Error reading user file: " + e.getMessage());
	    }
	    return null;
	}
	
	
	public boolean validateCredentials(long id, String password, String role) {
		User user = findUserById(id, role);
	    if (user == null) {
	        return false;
	    }
	    String hashed = SecurityUtil.hashText(password);
	    return user.getHashedPassword().equals(hashed);
	}
	
	@Override
	public boolean userExists(long id, String role) {
		String filePath = getRoleFilePath(role);
	    Path path = Paths.get(filePath);
	    try {
	        if (!Files.exists(path)) {
	            return false;
	        }
	        for (String line : Files.readAllLines(path)) {
	            String[] parts = line.split("#");
	            long storedId = Long.parseLong(parts[0]);
	            if (storedId == id) {
	                return true;
	            }
	        }

	    } 
	    catch (Exception e) {
	        System.out.println("Error reading user file: " + e.getMessage());
	    }
	    return false;
	}
	
	public long getNextUserId(String role) {
		String filePath = getRoleFilePath(role);

	    Path path = Paths.get(filePath);

	    long highestId = switch (role.toUpperCase()) {

	        case "CUSTOMER" -> 1000;
	        case "EMPLOYEE" -> 4000;
	        case "MANAGER" -> 5000;

	        default -> 0;
	    };

	    try {

	        if (!Files.exists(path)) {
	            return highestId + 1;
	        }

	        for (String line : Files.readAllLines(path)) {

	            if (line.trim().isEmpty()) {
	                continue;
	            }

	            String[] parts = line.split("#");

	            long storedId =
	                    Long.parseLong(parts[0]);

	            if (storedId > highestId) {
	                highestId = storedId;
	            }
	        }

	    } catch (Exception e) {

	        System.out.println(
	                "Error generating next user ID: "
	                        + e.getMessage()
	        );
	    }

	    return highestId + 1;
	}
	
	
	private String getRoleFilePath(String role) {

	    switch (role.toUpperCase()) {
	        case "CUSTOMER":
	            return customersfilepath;
	        case "EMPLOYEE":
	            return employeesfilepath;
	        case "MANAGER":
	            return managersfilepath;
	        default:
	            throw new IllegalArgumentException("Invalid role");
	    }
	}
	

}
