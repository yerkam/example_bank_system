package banking.domain.loans;

import java.time.LocalDateTime;

public class Loan {

	private final long userId;
	private final double principal;
	private double remainingBalance;
	private final double annualInterestRate;
	private final int termMonths;
	private String status;
	private final LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public Loan(long userId, double principal, double remainingBalance, double annualInterestRate,
			int termMonths, String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.userId = userId;
		this.principal = principal;
		this.remainingBalance = remainingBalance;
		this.annualInterestRate = annualInterestRate;
		this.termMonths = termMonths;
		this.status = status;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public void repay(double amount) {
		if (amount <= 0 || isRepaid()) {
			return;
		}

		remainingBalance -= amount;
		if (remainingBalance <= 0) {
			remainingBalance = 0;
			status = "REPAID";
		}
		updatedAt = LocalDateTime.now();
	}

	public boolean isActive() {
		return "ACTIVE".equalsIgnoreCase(status) && remainingBalance > 0;
	}

	public boolean isRepaid() {
		return "REPAID".equalsIgnoreCase(status) || remainingBalance <= 0;
	}

	public long getUserId() {
		return userId;
	}

	public double getPrincipal() {
		return principal;
	}

	public double getRemainingBalance() {
		return remainingBalance;
	}

	public double getAnnualInterestRate() {
		return annualInterestRate;
	}

	public int getTermMonths() {
		return termMonths;
	}

	public String getStatus() {
		return status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
}