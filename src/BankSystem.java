package src;

public class BankSystem {
    FileHandler fh = new FileHandler();
    
    public void createCheckingAccount(String name,String surname, long id, int password, double initialDeposit) {
        fh.createCheckingAccount(name, surname, id, password, initialDeposit, false);
    }

    public void createSavingsAccount(String name,String surname, long id, int password, double initialDeposit, int months) {
        fh.createDepositAccount(name, surname, id, password, initialDeposit, months);
    }
    public void createCurrencyAccount(String name, String surname, long id, int password, double initialDeposit, String currency) {
        fh.createCurrencyAccount(name, surname, id, password, initialDeposit, currency);
    }
    public String createDebitCard(long accountId, String holderName) {
        return fh.createDebitCard(accountId, holderName);
    }

    public String createCreditCard(long accountId, String holderName, double creditLimit, int paymentDay) {
        return fh.createCreditCard(accountId, holderName, creditLimit, paymentDay);
    }
}

