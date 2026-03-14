package banking.application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import banking.domain.*;

public class TransactionManager {
    private String filePath;

    TransactionManager(String filePath) {
        this.filePath = filePath;
    }

    
    public void makeTransaction(long senderId, long receiverId, int amount) {
        // This method would contain the logic to perform a transaction between two accounts,
        // including updating balances and recording the transaction in the transaction history.
        // The implementation details would depend on how accounts and transactions are structured in the files.
    }


    /**
     * This method retrieves the transaction history for a given account ID and returns it as a 2D array.
     * arr[Transaction_index][0] = sent or received ID
     * arr[Transaction_index][1] = amount of the transaction
     * arr[Transaction_index][2] = when the transaction happened
     * @param id The account ID for which to retrieve the transaction history.
     * @return A 2D array containing the transaction history, where each row represents a transaction with its ID, amount, and date.
     */
    public String[][] getTransactionHistory(long id) {
        TransactionList transactionList = new TransactionList();
        File myObj = new File(filePath);
        if (!myObj.exists()) {
            return transactionList.toArray(); // Return empty list if no transaction history exists
        }
        try (Scanner myReader = new Scanner(myObj)) {
            long lineNum = 0;
            while (myReader.hasNextLine()) {
                lineNum++;
                String data = myReader.nextLine();
                if (data.trim().isEmpty()) {
                    continue;
                }
                String[] parts = data.split("#");
                if (parts.length == 3) {
                    long transactionId = Long.parseLong(parts[0]);
                    int amount = Integer.parseInt(parts[1]);
                    String date = parts[2];
                    transactionList.add(transactionId, amount, date);
                } else {
                    System.out.println("Invalid transaction data at line: " + lineNum);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading the transaction history.");
            e.printStackTrace();
        }
        return transactionList.toArray();
    }
}
