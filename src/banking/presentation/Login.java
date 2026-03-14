package banking.presentation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Scanner;

/**
 * The login class handles user authentication and account creation for the banking system.
 * It provides a simple command-line interface for users to log in or create a new account.
 */
public class Login {
	
	private final String loginDetailsFile;
	
	static Scanner scanner = new Scanner(System.in);
	
	/**
	 * Displays the login menu and handles user input for logging in or creating a new account.
	 * The user can choose to log in as a customer, bank employee, or bank manager, or exit the system.
	 */
	public Login(String loginDetailsFile) {
		this.loginDetailsFile = loginDetailsFile;
		System.out.println("Welcome to the Banking System!");
		boolean loginChoice = false;
		boolean personnelType = false; 
		String loginEntity = "";
		
		while(!personnelType) { 
			System.out.println("Please select an option (A, B, C):");
			System.out.println("A. CUSTOMER LOGIN");
			System.out.println("B. BANK EMPLOYEE LOGIN");
			System.out.println("C. BANK MANAGER LOGIN");
			System.out.println("D. Exit");
			
			String choice = scanner.nextLine().trim().toLowerCase();
			
			switch (choice) {
				case "a":
					loginEntity = "customer";
					personnelType = true;
					break;
				case "b":
					loginEntity = "employee";
					personnelType = true;
					break;
				case "c":
					loginEntity = "manager";
					personnelType = true;
					break;
				case "d":
					System.out.println("Thank you for using the Banking System. Goodbye!");
					System.exit(0);
				default:
					System.out.println("Invalid option. Please try again.");
			}
		}
		
		
		while (!loginChoice) {
			if (loginEntity.equalsIgnoreCase("customer")) {
				System.out.println("Please select an option (1, 2, 3):");
				System.out.println("1. Log in ");
				System.out.println("2. Register for a new account");
				System.out.println("3. Exit");
				
				String choice = scanner.nextLine().trim();
				
				switch (choice) {
					case "1":
						loginProcess(loginEntity);
						loginChoice = true;
						break;
					case "2":
						createAccount();
						loginChoice = true;
						break;
					case "3":
						System.out.println("Thank you for using the Banking System. Goodbye!");
						System.exit(0);
					default:
						System.out.println("Invalid option. Please try again.");
				}
			}
			else {
				loginProcess(loginEntity);
				loginChoice = true;
			}
		}
	}
	
	
	/**
	 * Handles the user login process by prompting for user name and password.
	 * In a real implementation, this method would validate the credentials against stored user data and allow access to the banking system if the credentials are correct.
	 * @param loginEntity The type of user logging in (customer, employee, or manager) to tailor the login process accordingly.
	 */
	public void loginProcess(String loginEntity) {
		System.out.println("Welcome to the Banking System!");
		System.out.println("Please enter your username and password to log in.");
		
		System.out.println("Username: ");
		String username = scanner.nextLine().trim();
		
		System.out.println("Password:");
		String password = scanner.nextLine().trim();
		
		if (doesAccountExist(username, password)) {
			System.out.println("Login successful! Welcome, " + username + "!");
			// Proceed to the main menu or dashboard for the logged-in user
		} else {
			System.out.println("Invalid username or password. Please try again.");
			loginProcess(loginEntity); // Prompt again for login
		}
		
	}
	
	/**
	 * Checks if an account with the given username and password exists in the login details file.
	 * @param username The username to check.
	 * @param password The password to check.
	 * @return true if the account exists, false otherwise.
	 */
	public boolean doesAccountExist(String username, String password) {
        String filePath = loginDetailsFile;
        File file = new File(filePath);
        if (!file.exists()) return false;

        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("#");
                if (parts[0].equals(username) && parts[1].equals(password)) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error reading user file: " + e.getMessage());
        }
        return false;
    }
	
	
	/**
	 * Handles the account creation process by prompting the user for their details.
	 * In a real implementation, this method would save the new account information to a file or database and allow the user to log in with their new credentials.
	 */
	public void createAccount() {
		System.out.println("Welcome to the Banking System!");
		System.out.println("Please enter your details to create a new account.");
		
		System.out.println("First Name: ");
		String firstName = scanner.nextLine().trim();
		
		System.out.println("Last Name: ");
		String lastName = scanner.nextLine().trim();
		
		System.out.println("Username: ");
		String username = scanner.nextLine().trim();
		
		System.out.println("Password:");
		String password = scanner.nextLine().trim();
		
		
		String filePath = loginDetailsFile;
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write(username + "#" + password + firstName + "#" + lastName + "#" + LocalDateTime.now() + "\n");
                
                System.out.println("Account created successfully! You can now log in with your credentials.");
            } catch (Exception e) {
                System.out.println("Error creating user file: " + e.getMessage());
            }
        }
		
		
	}	 
}
