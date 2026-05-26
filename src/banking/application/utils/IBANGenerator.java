package banking.application.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import banking.infrastructure.FileHandler;

public class IBANGenerator {
	private FileHandler fileHandler = FileHandler.getInstance();
    private String accountsFolderPath = fileHandler.getAccountsFolderPath();

    /**
     * Generates a unique IBAN for new checking accounts.
     * Scans all user files in the accounts folder to ensure uniqueness.
     *
     * @return A unique IBAN number for the new account.
     */
    public int generateIBAN() {
        int randomNum = (int) (Math.random() * 1000000000);
        File accountsFolder = new File(accountsFolderPath);
        File[] userFiles = accountsFolder.listFiles((dir, name) -> name.endsWith(".txt"));

        if (userFiles != null) {
            for (File userFile : userFiles) {
                try (Scanner myReader = new Scanner(userFile)) {
                    // Skip first line (user info)
                    if (myReader.hasNextLine()) myReader.nextLine();

                    while (myReader.hasNextLine()) {
                        String data = myReader.nextLine().trim();
                        if (data.isEmpty()) continue;
                        String[] parts = data.split("#");
                        if (parts[0].equals("Checking") && parts.length >= 5) {
                            int storedIBAN = Integer.parseInt(parts[4]);
                            if (storedIBAN == randomNum) {
                                return generateIBAN(); // Recursively generate a new IBAN
                            }
                        }
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("Error reading file for IBAN check: " + e.getMessage());
                }
            }
        }
        return randomNum;
    }
}
