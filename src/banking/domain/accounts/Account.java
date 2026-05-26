package banking.domain.accounts;

public abstract class Account {

    protected int accountNumber;
    protected double balance;
    protected long userId;

    public Account(int accountNumber, double balance, long userId) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.userId = userId;
    }

    public void deposit(double amount) {
        balance += amount;
    }

	public long getUserId() {
		return userId;
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
