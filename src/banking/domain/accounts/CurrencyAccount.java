package banking.domain.accounts;

public class CurrencyAccount extends Account {

    private String currency;

    public CurrencyAccount(int accountNumber, double balance, String currency, String name, String surname, String password, long id) {

        super(accountNumber, balance, name, surname, password, id);
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }
}