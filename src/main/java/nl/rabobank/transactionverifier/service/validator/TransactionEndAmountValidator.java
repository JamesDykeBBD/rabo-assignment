package nl.rabobank.transactionverifier.service.validator;

import nl.rabobank.transactionverifier.model.transaction.Transaction;

import java.math.BigDecimal;
import java.util.List;

public class TransactionEndAmountValidator implements Validator<Transaction> {

    @Override
    public List<ValidationResult<Transaction>> validate(List<Transaction> transactions) {
        return transactions.stream().map(this::validate).toList();
    }

    private ValidationResult<Transaction> validate(Transaction transaction) {
        BigDecimal startBalance = transaction.getBalance();
        BigDecimal endBalance = transaction.getEndBalance();
        BigDecimal mutation = startBalance;
        if (transaction.getMutation().startsWith("+")) {
            mutation = mutation.add(BigDecimal.valueOf(Double.parseDouble(transaction.getMutation().substring(1))));
        } else if (transaction.getMutation().startsWith("-")) {
            mutation = mutation.subtract(BigDecimal.valueOf(Double.parseDouble(transaction.getMutation().substring(1))));
        }
        return mutation.compareTo(endBalance) == 0 ? new ValidationResult<>(true, "End balance matches mutation", transaction) : new ValidationResult<>(false, "End balance does not match mutation", transaction);
    }
}
