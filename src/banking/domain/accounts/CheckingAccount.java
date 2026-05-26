package banking.domain.accounts;

public class CheckingAccount extends Account {
	private boolean active;
    private int iban;

    public CheckingAccount(int accountNumber, double balance, boolean active, int iban, long userId) {
        super(accountNumber, balance, userId);
        this.active = active;
        this.iban = iban;
    }

    public boolean isActive() {
        return active;
    }

    public int getIban() {
        return iban;
    }
}
