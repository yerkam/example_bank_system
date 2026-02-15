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

    private String fileName = "Accounts.txt"; // Default File name

    public FileHandler(){
        Path path = Paths.get(fileName);
        if(!Files.exists(path)){
            CreateFile(fileName);
        }
        

    }


    /**
     * This method creates an account and writes the account information to a file.
     * This method is used for checking accounts.
     * @param name Client's name
     * @param surname Client's surname
     * @param id Client's ID
     * @param password Client's password
     * @param balance Client's account balance
     * @param active Client's account status
     * @param IBAN Client's account IBAN
     */
    public void CreateAccount(String name, String surname,long id,int password, double balance, boolean active){
        // Check if the file exists, if not, print an error message and return
        Path path = Paths.get(fileName);
        if(!Files.exists(path)){
            System.out.println("The file does not exist. Account creation failed.");
            return;
        }
        
        File myObj = new File(fileName);
        // Check for duplicate ID
        try (Scanner myReader = new Scanner(myObj)) {
            long line = 0; 
            while (myReader.hasNextLine()) {
                line++;
                String data = myReader.nextLine();
                String[] parts = data.split("#");
                if (parts.length >= 6) { 
                    long storedId  = Long.parseLong(parts[3]);
                    if (storedId == id) {
                        System.out.println("An account with the same ID already exists. Account creation failed.");
                        return;
                    }
                }else{
                    System.out.println("Invalid account data at line :" + line);
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        // If no duplicate ID is found, write the new account information to the file
        try(FileWriter writer = new FileWriter(fileName, true)){
            writer.write("Checking#" + name + "#" + surname + "#" + id + "#" + password + "#" + balance + "#" + active + "#" + generateIBAN() + "\n");
            writer.close();
        }catch(Exception e){
            System.out.println("An error occurred while creating the account: " + e.getMessage());
        }
    }


    /**
     * This method creates an account and writes the account information to a file.
     * This method is used for deposit accounts.
     * @param name Client's name
     * @param surname Client's surname
     * @param id Client's ID
     * @param password Client's password
     * @param balance Client's account balance
     * @param months Client's account duration in months
     */
    public void CreateAccount(String name, String surname, long id, int password, double balance, int months){
        // Check if the file exists, if not, print an error message and return
        Path path = Paths.get(fileName);
        if(!Files.exists(path)){
            System.out.println("The file does not exist. Account creation failed.");
            return;
        }
        
        File myObj = new File(fileName);
        // Check for duplicate ID
        try (Scanner myReader = new Scanner(myObj)) {
            long line = 0; 
            while (myReader.hasNextLine()) {
                line++;
                String data = myReader.nextLine();
                String[] parts = data.split("#");
                if (parts.length >= 6) { 
                    long storedId  = Long.parseLong(parts[3]);
                    if (storedId == id) {
                        System.out.println("An account with the same ID already exists. Account creation failed.");
                        return;
                    }
                }else{
                    System.out.println("Invalid account data at line :" + line);
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        // If no duplicate ID is found, write the new account information to the file
        try(FileWriter writer = new FileWriter(fileName, true)){
            LocalDate Date = LocalDate.now();
            LocalDate ExpiryDate = Date.plusMonths(months);
            writer.write("Deposit#" + name + "#" + surname + "#" + id + "#" + password + "#" + balance  + "#" + ExpiryDate + "\n");
            writer.close();
        }catch(Exception e){
            System.out.println("An error occurred while creating the account: " + e.getMessage());
        }
    }


    /**
     * This method checks if an account exists in the file based on the provided name, surname, and password.
     * @param id Client's ID
     * @param password Client's password
     * @return true if the account exists and the password matches, false otherwise.
     */
    public boolean CheckAccount(long id, String password){
        try{
            Path path = Paths.get(fileName);
            if (Files.exists(path)) { // Check if the file exists
                for (String line : Files.readAllLines(path)) { 
                    String[] parts = line.split("#"); 
                    if (parts.length >= 3) { 
                        long storedId  = Long.parseLong(parts[3]);
                        String storedPassword = parts[4];
                        if (storedId == id && storedPassword.equals(password)) {
                            return true; 
                        }
                    }else{
                        System.out.println("Invalid account data format in file: " + line);
                    }
                }
            }else{
                System.out.println("The file does not exist.");
            }
        }catch(Exception e){
            System.out.println("An error occurred while checking the account: " + e.getMessage());
        }
        return false;
    }


    /**
     * This method creates a file if it does not exist.
     * @param fileName The name of the file to be created.
     */
    private void CreateFile(String fileName){
        try{
            if (!System.getProperty("os.name").toLowerCase().contains("win") && !fileName.startsWith(".")) {
                fileName = "." + fileName;
            }

            Path path = Paths.get(fileName);

            // Create the file
            Files.createFile(path);

            // If the operating system is Windows, set the file attribute to hidden
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                Files.setAttribute(path, "dos:hidden", true);
            }
        }catch(Exception e){
            System.out.println("An error occurred while creating the file: " + e.getMessage());
        }
    }


    /**
     * This method creates IBAN for the new accounts.
     * @return A unique IBAN number for the new account.
     */
    private int generateIBAN(){

        int randomNum = (int)(Math.random() * 1000000000);
        File myObj = new File(fileName);
        try (Scanner myReader = new Scanner(myObj)) {
            long line = 0; 
            while (myReader.hasNextLine()) {
                line++;
                String data = myReader.nextLine();
                String[] parts = data.split("#");
                if(parts[0].equals("Checking")){
                    if (parts.length >= 6) { 
                        int storedIBANs  = Integer.parseInt(parts[7]);
                        if (storedIBANs == randomNum) {
                            return generateIBAN(); // Generate a new IBAN if it already exists
                        }
                    }else{
                        System.out.println("Invalid account data at line :" + line);
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
