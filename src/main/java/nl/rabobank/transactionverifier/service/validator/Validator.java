package nl.rabobank.transactionverifier.service.validator;

import nl.rabobank.transactionverifier.model.transaction.Transaction;

import java.util.List;

public interface Validator<I> {

    List<ValidationResult<I>> validate(List<I> i);
}
