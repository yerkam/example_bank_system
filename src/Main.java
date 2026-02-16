package src;

public class Main {
      public static void main(String[] args) {
        BankSystem bankSystem = new BankSystem();
        
        // Checking account örneği
        bankSystem.createCheckingAccount("John", "Doe", 123456789, 1234, 1000.0);
        
        // Currency account örnekleri
        bankSystem.createCurrencyAccount("Alice", "Smith", 987654321, 5678, 500.0, "USD");
        bankSystem.createCurrencyAccount("Bob", "Johnson", 555666777, 9012, 750.0, "EUR");
    }
}
