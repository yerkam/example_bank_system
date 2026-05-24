package banking.application;

import banking.domain.loans.Loan;
import banking.infrastructure.AccountRepo;
import banking.infrastructure.LoanRepo;
import java.time.LocalDateTime;

public class LoanManager {

	private final String accountsFolderPath;
	private final String loansFile;
	private final CreditScoreManager creditScoreManager;
	private final LoanRepo loanRepo = new LoanRepo();
	private final AccountRepo accountRepo = new AccountRepo();

	public LoanManager(String accountsFolderPath, String creditCardsFile, String loansFile) {
		this.accountsFolderPath = accountsFolderPath;
		this.loansFile = loansFile;
		this.creditScoreManager = new CreditScoreManager(creditCardsFile, loansFile);
	}

	public int calculateCreditScore(long userId) {
		return creditScoreManager.calculateCreditScore(userId);
	}

	public String getCreditScoreBand(long userId) {
		return creditScoreManager.getCreditScoreBand(calculateCreditScore(userId));
	}

	public String requestLoan(long userId, double amount, int termMonths) {
		if (amount <= 0) {
			return "Loan request failed: amount must be greater than 0.";
		}

		if (termMonths < 1) {
			return "Loan request failed: term must be at least 1 month.";
		}

		if (loanRepo.findActiveLoanByUserId(userId, loansFile) != null) {
			return "Loan request failed: an active loan already exists for this user.";
		}

		int score = calculateCreditScore(userId);
		double maxAmount = getMaxLoanAmount(score);
		if (score < 650) {
			return "Loan request rejected: credit score is " + score + " (" + getCreditScoreBand(userId) + ").";
		}

		if (amount > maxAmount) {
			return "Loan request rejected: requested amount exceeds maximum allowed for score " + score + ". Max amount: " + maxAmount + ".";
		}

		double interestRate = getInterestRate(score);
		LocalDateTime now = LocalDateTime.now();
		Loan loan = new Loan(userId, amount, amount, interestRate, termMonths, "ACTIVE", now, now);
		if (!accountRepo.adjustFirstActiveCheckingBalance(userId, accountsFolderPath, amount)) {
			return "Loan request failed: could not deposit funds into the user's active checking account.";
		}

		if (loanRepo.saveLoan(loan, loansFile)) {
			return "Loan approved: " + amount + " granted to user " + userId + " at " + interestRate + " annual interest and deposited into the active checking account. Credit score: " + score + " (" + creditScoreManager.getCreditScoreBand(score) + ").";
		}

		accountRepo.adjustFirstActiveCheckingBalance(userId, accountsFolderPath, -amount);

		return "Loan request failed while saving the loan record.";
	}

	public String repayLoan(long userId, double amount) {
		if (amount <= 0) {
			return "Repayment failed: amount must be greater than 0.";
		}

		Loan loan = loanRepo.findActiveLoanByUserId(userId, loansFile);
		if (loan == null) {
			return "Repayment failed: no active loan found for this user.";
		}

		loan.repay(amount);
		if (loanRepo.updateLoan(loan, loansFile)) {
			if (loan.isRepaid()) {
				return "Loan fully repaid for user " + userId + ".";
			}
			return "Repayment successful. Remaining balance: " + loan.getRemainingBalance() + ".";
		}

		return "Repayment failed while updating the loan record.";
	}

	private double getMaxLoanAmount(int score) {
		if (score >= 800) return 10000.0;
		if (score >= 740) return 5000.0;
		if (score >= 670) return 2500.0;
		return 1000.0;
	}

	private double getInterestRate(int score) {
		if (score >= 800) return 0.12;
		if (score >= 740) return 0.16;
		if (score >= 670) return 0.20;
		return 0.24;
	}
}