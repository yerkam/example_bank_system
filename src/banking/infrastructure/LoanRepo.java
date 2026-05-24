package banking.infrastructure;

import banking.domain.loans.Loan;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

public class LoanRepo {

	public boolean saveLoan(Loan loan, String filePath) {
		try {
			ensureStorage(filePath);
			try (FileWriter writer = new FileWriter(filePath, true)) {
				writer.write(serializeLoan(loan) + "\n");
				return true;
			}
		} catch (Exception e) {
			System.out.println("An error occurred while saving the loan: " + e.getMessage());
			return false;
		}
	}

	public Loan findActiveLoanByUserId(long userId, String filePath) {
		try {
			Path path = Paths.get(filePath);
			if (!Files.exists(path)) {
				return null;
			}

			for (String line : Files.readAllLines(path)) {
				Loan loan = deserializeLoan(line);
				if (loan != null && loan.getUserId() == userId && loan.isActive()) {
					return loan;
				}
			}
		} catch (Exception e) {
			System.out.println("An error occurred while reading the loan: " + e.getMessage());
		}

		return null;
	}

	public int countActiveLoans(long userId, String filePath) {
		return countLoansByStatus(userId, filePath, "ACTIVE");
	}

	public int countRepaidLoans(long userId, String filePath) {
		return countLoansByStatus(userId, filePath, "REPAID");
	}

	public boolean updateLoan(Loan loan, String filePath) {
		try {
			Path path = Paths.get(filePath);
			if (!Files.exists(path)) {
				return false;
			}

			java.util.List<String> lines = Files.readAllLines(path);
			for (int i = 0; i < lines.size(); i++) {
				Loan existingLoan = deserializeLoan(lines.get(i));
				if (existingLoan != null && existingLoan.getUserId() == loan.getUserId() && existingLoan.getCreatedAt().equals(loan.getCreatedAt())) {
					lines.set(i, serializeLoan(loan));
					Files.write(path, lines);
					return true;
				}
			}
		} catch (Exception e) {
			System.out.println("An error occurred while updating the loan: " + e.getMessage());
		}

		return false;
	}

	private int countLoansByStatus(long userId, String filePath, String status) {
		try {
			Path path = Paths.get(filePath);
			if (!Files.exists(path)) {
				return 0;
			}

			int count = 0;
			for (String line : Files.readAllLines(path)) {
				Loan loan = deserializeLoan(line);
				if (loan != null && loan.getUserId() == userId && status.equalsIgnoreCase(loan.getStatus())) {
					count++;
				}
			}
			return count;
		} catch (Exception e) {
			System.out.println("An error occurred while counting loans: " + e.getMessage());
			return 0;
		}
	}

	private void ensureStorage(String filePath) throws Exception {
		Path path = Paths.get(filePath);
		Path parent = path.getParent();
		if (parent != null && !Files.exists(parent)) {
			Files.createDirectories(parent);
		}
		if (!Files.exists(path)) {
			Files.createFile(path);
		}
	}

	private String serializeLoan(Loan loan) {
		return "Loan#" + loan.getUserId() + "#" + loan.getPrincipal() + "#" + loan.getRemainingBalance()
			+ "#" + loan.getAnnualInterestRate() + "#" + loan.getTermMonths() + "#" + loan.getStatus()
			+ "#" + loan.getCreatedAt() + "#" + loan.getUpdatedAt();
	}

	private Loan deserializeLoan(String line) {
		try {
			String trimmedLine = line.trim();
			if (trimmedLine.isEmpty()) {
				return null;
			}

			String[] parts = trimmedLine.split("#");
			if (parts.length < 9 || !parts[0].equals("Loan")) {
				return null;
			}

			return new Loan(
				Long.parseLong(parts[1]),
				Double.parseDouble(parts[2]),
				Double.parseDouble(parts[3]),
				Double.parseDouble(parts[4]),
				Integer.parseInt(parts[5]),
				parts[6],
				LocalDateTime.parse(parts[7]),
				LocalDateTime.parse(parts[8])
			);
		} catch (Exception e) {
			System.out.println("An error occurred while parsing a loan record: " + e.getMessage());
			return null;
		}
	}
}