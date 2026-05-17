package banking.application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import banking.domain.*;
import banking.infrastructure.FileHandler;

public class TransactionManager {
    FileHandler fileHandler = new FileHandler();
    private final String accountsFolderPath = fileHandler.getAccountsFolderPath();
    private final String transactionsFolderPath = fileHandler.getTransactionHistoryPath();


    
    public void makeTransaction(long senderId, int senderCheckingAccountNumber, int IBAN, double amount) {
        try {
            // Sadece gönderenin dosya yollarını en başta tanımlıyoruz
            String senderFilePath = accountsFolderPath + File.separator + senderId + ".txt";
            Path senderPath = Paths.get(senderFilePath);  
            
            if (!Files.exists(senderPath)) {
                System.out.println("Sender file does not exist.");
                return;
            }

            java.util.List<String> senderLines = Files.readAllLines(senderPath);
            
            if (senderLines.isEmpty()) {
                System.out.println("Sender account file is empty.");
                return;
            }

            boolean senderFound = false;
            boolean receiverFound = false;
            
            // Alıcı için döngüde bulacağımız değişkenleri hazırlıyoruz
            long foundReceiverId = -1;
            Path receiverPath = null;
            java.util.List<String> receiverLines = null;

            // 1. ADIM: Gönderenin Hesabını ve Bakiyesini Kontrol Et
            for (int i = 1; i < senderLines.size(); i++) {
                String[] parts = senderLines.get(i).trim().split("#");
                if (parts.length >= 4 && parts[0].equals("Checking") && Integer.parseInt(parts[1]) == senderCheckingAccountNumber) {
                    double balance = Double.parseDouble(parts[2]);
                    if (balance < amount) {
                        System.out.println("Insufficient funds for the transaction.");
                        return; // Para yetersizse işlemi anında iptal et
                    }
                    // Hafızada bakiyeyi güncelle
                    parts[2] = String.valueOf(balance - amount);
                    senderLines.set(i, String.join("#", parts));
                    senderFound = true;
                    break;
                }
            }

            // 2. ADIM: Alıcının IBAN'ını Tüm Dosyalarda Ara
            if (senderFound) {
                File folder = new File(accountsFolderPath);
                File[] userFiles = folder.listFiles((dir, name) -> name.endsWith(".txt"));

                if (userFiles != null) {
                    for (File file : userFiles) {
                        java.util.List<String> tempLines = Files.readAllLines(file.toPath());
                        for (int j = 1; j < tempLines.size(); j++) {
                            String[] parts = tempLines.get(j).trim().split("#");
                            // Checking hesabı mı ve IBAN eşleşiyor mu?
                            if (parts.length >= 5 && parts[0].equals("Checking") && Integer.parseInt(parts[4]) == IBAN) {
                                double receiverBalance = Double.parseDouble(parts[2]);
                                // Hafızada bakiyeyi güncelle
                                parts[2] = String.valueOf(receiverBalance + amount);
                                tempLines.set(j, String.join("#", parts));
                                
                                receiverFound = true;
                                receiverPath = file.toPath();
                                receiverLines = tempLines;
                                
                                // Dosya isminden ID'yi çıkar (Örn: "987654321.txt" -> 987654321)
                                String filename = file.getName();
                                foundReceiverId = Long.parseLong(filename.substring(0, filename.length() - 4));
                                break;
                            }
                        }
                        if (receiverFound) break; // Bulduysak diğer dosyaları aramaya gerek yok
                    }
                }
            }

            // 3. ADIM: Her İki Taraf da Doğrulandıysa Dosyalara Yaz (Commit)
            if (senderFound && receiverFound) {
                // Bakiyeleri dosyalara yaz
                Files.write(senderPath, senderLines);
                Files.write(receiverPath, receiverLines); // receiverPath burada güvenle kullanılıyor

                // Sadece Gönderenin log dosyasına yaz (AlıcıID#Tutar#Zaman)
                String senderTransactionFilePath = transactionsFolderPath + File.separator + senderId + "_transactions.txt";
                try (FileWriter writer = new FileWriter(senderTransactionFilePath, true)) {
                    writer.write(foundReceiverId + "#" + amount + "#" + java.time.LocalDateTime.now() + "\n");
                }
                
                System.out.println("Transaction completed successfully!");
            } else if (!receiverFound) {
                System.out.println("Transaction failed. Receiver IBAN not found in the system.");
            } else {
                System.out.println("Transaction failed.");
            }

        } catch (Exception e) {
            System.out.println("An error occurred while making transaction: " + e.getMessage());
        }
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
        File myObj = new File(transactionsFolderPath + File.separator + id + "_transactions.txt");
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
