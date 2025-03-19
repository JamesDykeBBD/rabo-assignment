package nl.rabobank.transactionverifier.service.validator;

import nl.rabobank.transactionverifier.model.transaction.Transaction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TransactionUniqueReferenceIdValidator implements Validator<Transaction> {

    @Override
    public List<ValidationResult<Transaction>> validate(List<Transaction> transactions) {
        // Find duplicate references
        Set<String> seenReferences = new HashSet<>();
        Set<String> duplicateReferences = transactions.stream()
                .map(Transaction::getReference)
                .filter(reference -> !seenReferences.add(reference))
                .collect(Collectors.toSet());

        // Create validation results
        return transactions.stream()
                .map(transaction -> {
                    boolean isValid = !duplicateReferences.contains(transaction.getReference());
                    String errorMessage = isValid ? null : "Duplicate transaction reference: " + transaction.getReference();
                    return new ValidationResult<>(isValid, errorMessage, transaction);
                })
                .collect(Collectors.toList());
    }
}
