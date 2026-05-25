package banking.domain.accounts;

public abstract class Account {

    protected int accountNumber;
    protected double balance;
    protected String name;
    protected String surname;
    protected String password;
    protected long id;

    public Account(int accountNumber, double balance, String name, String surname, String password, long id) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.id = id;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public String getPassword() {
		return password;
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
