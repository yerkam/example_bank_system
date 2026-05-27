package banking.application;

import banking.domain.cards.CreditCard;
import banking.infrastructure.CardRepository;
import banking.infrastructure.FileCardRepository;
import banking.infrastructure.LoanRepo;

public class CreditScoreManager {

	private final String creditCardsFile;
	private final String loansFile;
	private final CardRepository cardRepo = new FileCardRepository();
	private final LoanRepo loanRepo = new LoanRepo();

	public CreditScoreManager(String creditCardsFile, String loansFile) {
		this.creditCardsFile = creditCardsFile;
		this.loansFile = loansFile;
	}

	public int calculateCreditScore(long userId) {
		int score = 600;

		CreditCard card = cardRepo.findCreditCardByUserId(userId);
		if (card == null) {
			score -= 100;
		} else {
			if (card.getCreditLimit() > 0) {
				double utilization = card.getDebt() / card.getCreditLimit();
				score -= (int) Math.round(utilization * 250);
			}

			if (card.getDebt() == 0) {
				score += 40;
			} else {
				score -= 20;
			}
		}

		int activeLoans = loanRepo.countActiveLoans(userId, loansFile);
		int repaidLoans = loanRepo.countRepaidLoans(userId, loansFile);

		score -= activeLoans * 80;
		score += Math.min(repaidLoans * 15, 45);

		if (score < 300) {
			score = 300;
		}
		if (score > 850) {
			score = 850;
		}

		return score;
	}

	public String getCreditScoreBand(int score) {
		if (score >= 800) return "Excellent";
		if (score >= 740) return "Very Good";
		if (score >= 670) return "Good";
		if (score >= 600) return "Fair";
		return "Poor";
	}
}