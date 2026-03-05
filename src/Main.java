package src;

public class Main {
      public static void main(String[] args) {
            FileHandler fileHandler = new FileHandler();
            AccountManager accountManager = new AccountManager(fileHandler.getAccountsFolderPath());
            accountManager.createCheckingAccount("John","Doe", 123456789, 1234, 1000.0, true);
            accountManager.createCurrencyAccount("John", "Doe" , 123456789, 1234, 1000.0, "EUR");
            CardManager cardManager = new CardManager(fileHandler.getAccountsFolderPath(), fileHandler.getCreditCardsFile());
            cardManager.createCreditCard(123456789, "John Doe", 10000, 28);
            cardManager.createDebitCard(123456789, 1, "John Doe");
    
     }
    }
