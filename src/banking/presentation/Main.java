package banking.presentation;

import banking.application.AccountManager;
import banking.application.Authentication;
import banking.application.utils.IBANGenerator;
import banking.infrastructure.AccountRepository;
import banking.infrastructure.FileAccountRepository;

public class Main {
      public static void main(String[] args) {
    	  // Initiating the Infrastructure layer
    	  AccountRepository accountRepository = new FileAccountRepository();
    	  
    	  // Initiating the Utility classes
    	  IBANGenerator ibanGenerator = new IBANGenerator();
    	  Authentication authentication = new Authentication();
    	  
    	  // Initiating the Application layer
    	  AccountManager accountManager = new AccountManager(accountRepository, ibanGenerator);
    	  
    	  // Starting the presentation layer
    	  new Login(accountManager, authentication);
      }
}
