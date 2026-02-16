package src;

public class Main {
      public static void main(String[] args) {
        BankSystem bankSystem = new BankSystem();
        
        // Checking account örneği
        bankSystem.createCheckingAccount("John", "Doe", 123456789, 1234, 1000.0);
        
        // Currency account örnekleri
        bankSystem.createCurrencyAccount("Alice", "Smith", 987654321, 5678, 500.0, "USD");
        bankSystem.createCurrencyAccount("Bob", "Johnson", 555666777, 9012, 750.0, "EUR");
        // Debit card oluşturma (checking account'a bağlı)
        String debitCardNumber = bankSystem.createDebitCard(123456789, "John Doe");
        if (debitCardNumber != null) {
            System.out.println("Debit card created successfully: " + debitCardNumber);
        }
        
        // Credit card oluşturma
        String creditCardNumber = bankSystem.createCreditCard(123456789, "John Doe", 5000.0, 15);
        if (creditCardNumber != null) {
            System.out.println("Credit card created successfully: " + creditCardNumber);
        }
    
     }
    }
