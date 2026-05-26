package banking.domain.accounts;

public class CurrencyAccount extends Account {

    private String currency;

    public CurrencyAccount(int accountNumber, double balance, String currency, long userId) {

        super(accountNumber, balance, userId);
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }
}