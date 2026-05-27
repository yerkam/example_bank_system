package banking.infrastructure;

public class AccountSummary {
	private int totalAccounts;

    private int checkingAccounts;
    private int depositAccounts;
    private int currencyAccounts;

    private int debitCards;
    private int creditCards;

    private double totalBalance;

    public AccountSummary(
            int totalAccounts,
            int checkingAccounts,
            int depositAccounts,
            int currencyAccounts,
            int debitCards,
            int creditCards,
            double totalBalance
    ) {
        this.totalAccounts = totalAccounts;
        this.checkingAccounts = checkingAccounts;
        this.depositAccounts = depositAccounts;
        this.currencyAccounts = currencyAccounts;
        this.debitCards = debitCards;
        this.creditCards = creditCards;
        this.totalBalance = totalBalance;
    }

	public int getTotalAccounts() {
		return totalAccounts;
	}

	public int getCheckingAccounts() {
		return checkingAccounts;
	}

	public int getDepositAccounts() {
		return depositAccounts;
	}

	public int getCurrencyAccounts() {
		return currencyAccounts;
	}

	public int getDebitCards() {
		return debitCards;
	}

	public int getCreditCards() {
		return creditCards;
	}

	public double getTotalBalance() {
		return totalBalance;
	}
}	
