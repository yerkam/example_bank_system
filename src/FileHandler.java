package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Scanner;

/**
 * This class is responsible for handling file operations related to account management.
 */
public class FileHandler {

    private final String fileName;

    public FileHandler() {
        String name = "Accounts.txt";
        // On non-Windows OS, prefix with "." to hide the file
        if (!System.getProperty("os.name").toLowerCase().contains("win") && !name.startsWith(".")) {
            name = "." + name;
        }
        this.fileName = name;

        Path path = Paths.get(fileName);
        if (!Files.exists(path)) {
            createFile(fileName);
        }
    }


    /**
     * This method creates a checking account and writes the account information to a file.
     * 
     * @param name     Client's name
     * @param surname  Client's surname
     * @param id       Client's ID
     * @param password Client's password
     * @param balance  Client's account balance
     * @param active   Client's account status
     */
    public void createCheckingAccount(String name, String surname, long id, int password, double balance, boolean active) {
        Path path = Paths.get(fileName);
        if (!Files.exists(path)) {
            System.out.println("The file does not exist. Account creation failed.");
            return;
        }

        if (isDuplicateId(id)) {
            System.out.println("An account with the same ID already exists. Account creation failed.");
            return;
        }

        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write("Checking#" + name + "#" + surname + "#" + id + "#" + password + "#" + balance + "#" + active + "#" + generateIBAN() + "\n");
        } catch (Exception e) {
            System.out.println("An error occurred while creating the account: " + e.getMessage());
        }
    }


    /**
     * This method creates a deposit account and writes the account information to a file.
     *
     * @param name     Client's name
     * @param surname  Client's surname
     * @param id       Client's ID
     * @param password Client's password
     * @param balance  Client's account balance
     * @param months   Client's account duration in months
     */
    public void createDepositAccount(String name, String surname, long id, int password, double balance, int months) {
        Path path = Paths.get(fileName);
        if (!Files.exists(path)) {
            System.out.println("The file does not exist. Account creation failed.");
            return;
        }

        if (isDuplicateId(id)) {
            System.out.println("An account with the same ID already exists. Account creation failed.");
            return;
        }

        try (FileWriter writer = new FileWriter(fileName, true)) {
            LocalDate date = LocalDate.now();
            LocalDate expiryDate = date.plusMonths(months);
            writer.write("Deposit#" + name + "#" + surname + "#" + id + "#" + password + "#" + balance + "#" + expiryDate + "\n");
        } catch (Exception e) {
            System.out.println("An error occurred while creating the account: " + e.getMessage());
        }
    }

    
    /**
     * This method checks if an account exists in the file based on the provided ID and password.
     *
     * @param id       Client's ID
     * @param password Client's password
     * @return true if the account exists and the password matches, false otherwise.
     */
    public boolean checkAccount(long id, int password) {
        try {
            Path path = Paths.get(fileName);
            if (Files.exists(path)) {
                for (String line : Files.readAllLines(path)) {
                    if (line.trim().isEmpty()) {
                        continue;
                    }
                    String[] parts = line.split("#");
                    if (parts.length >= 5) {
                        long storedId = Long.parseLong(parts[3]);
                        int storedPassword = Integer.parseInt(parts[4]);
                        if (storedId == id && storedPassword == password) {
                            return true;
                        }
                    } else {
                        System.out.println("Invalid account data format in file: " + line);
                    }
                }
            } else {
                System.out.println("The file does not exist.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred while checking the account: " + e.getMessage());
        }
        return false;
    }

    /**
     * Checks whether an account with the given ID already exists in the file.
     *
     * @param id The ID to check for duplicates.
     * @return true if a duplicate ID is found, false otherwise.
     */
    private boolean isDuplicateId(long id) {
        File myObj = new File(fileName);
        try (Scanner myReader = new Scanner(myObj)) {
            long lineNum = 0;
            while (myReader.hasNextLine()) {
                lineNum++;
                String data = myReader.nextLine();
                if (data.trim().isEmpty()) {
                    continue;
                }
                String[] parts = data.split("#");
                if (parts.length >= 5) {
                    long storedId = Long.parseLong(parts[3]);
                    if (storedId == id) {
                        return true;
                    }
                } else {
                    System.out.println("Invalid account data at line: " + lineNum);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while checking for duplicate ID.");
            e.printStackTrace();
        }
        return false;
    }


    /**
     * This method creates a file if it does not exist.
     *
     * @param fileName The name of the file to be created.
     */
    private void createFile(String fileName) {
        try {
            Path path = Paths.get(fileName);

            // Create the file
            Files.createFile(path);

            // If the operating system is Windows, set the file attribute to hidden
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                Files.setAttribute(path, "dos:hidden", true);
            }
        } catch (Exception e) {
            System.out.println("An error occurred while creating the file: " + e.getMessage());
        }
    }


    /**
     * This method generates a unique IBAN for new checking accounts.
     * @return A unique IBAN number for the new account.
     */
    private int generateIBAN() {
        int randomNum = (int) (Math.random() * 1000000000);
        File myObj = new File(fileName);
        try (Scanner myReader = new Scanner(myObj)) {
            long lineNum = 0;
            while (myReader.hasNextLine()) {
                lineNum++;
                String data = myReader.nextLine();
                if (data.trim().isEmpty()) {
                    continue;
                }
                String[] parts = data.split("#");
                if (parts[0].equals("Checking")) {
                    if (parts.length >= 8) {
                        int storedIBAN = Integer.parseInt(parts[7]);
                        if (storedIBAN == randomNum) {
                            return generateIBAN(); // Generate a new IBAN if it already exists
                        }
                    } else {
                        System.out.println("Invalid account data at line: " + lineNum);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return randomNum;
    }
}
