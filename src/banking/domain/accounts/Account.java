package banking.domain.accounts;

public abstract class Account {

    protected int accountNumber;
    protected double balance;
    protected long id;

    public Account(int accountNumber, double balance, long id) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.id = id;
    }

    public void deposit(double amount) {
        balance += amount;
    }

	public long getId() {
		return id;
	}

	public void withdraw(double amount) {
        if(amount > balance) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        balance -= amount;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }
}
