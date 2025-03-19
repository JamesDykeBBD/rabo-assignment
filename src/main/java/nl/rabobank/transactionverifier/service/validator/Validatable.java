package nl.rabobank.transactionverifier.service.validator;

public interface Validatable {

    void setResult(boolean result);

    void setValidationMessage(String validationMessage);
}
