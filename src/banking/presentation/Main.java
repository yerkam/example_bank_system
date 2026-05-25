package banking.presentation;

import banking.application.AccountManager;
import banking.application.Authentication;
import banking.application.CardManager;
import banking.application.utils.CardGenerator;
import banking.application.utils.IBANGenerator;
import banking.infrastructure.AccountRepository;
import banking.infrastructure.CardRepository;
import banking.infrastructure.FileAccountRepository;
import banking.infrastructure.FileCardRepository;

public class Main {
      public static void main(String[] args) {
    	  // Initiating the Infrastructure layer
    	  AccountRepository accountRepository = new FileAccountRepository();
    	  CardRepository cardRepository = new FileCardRepository();
    	  
    	  // Initiating the Utility classes
    	  IBANGenerator ibanGenerator = new IBANGenerator();
    	  Authentication authentication = new Authentication();
    	  CardGenerator cardGenerator = new CardGenerator();
    	  
    	  // Initiating the Application layer
    	  AccountManager accountManager = new AccountManager(accountRepository, ibanGenerator);
    	  
    	  // Starting the presentation layer
    	  new Login(accountManager, authentication);
    	  
    	  // Testing the Card Creation logic...
//    	  CardManager cardManager = new CardManager(cardRepository, accountRepository, cardGenerator);
//          cardManager.createCreditCard(1234678, "Main Doe", 10000, 28);
//          cardManager.createDebitCard(1234678, 1, "Main Doe");
      }
}
