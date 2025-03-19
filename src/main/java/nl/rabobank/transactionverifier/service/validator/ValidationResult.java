package nl.rabobank.transactionverifier.service.validator;

public class ValidationResult<I> {

    private final boolean valid;
    private final String message;
    private final I input;

    public ValidationResult(boolean valid, String message, I input) {
        this.valid = valid;
        this.message = message;
        this.input = input;
    }

    public boolean isValid() {
        return valid;
    }

    public String getMessage() {
        return message;
    }

    public I getInput() {
        return input;
    }
}
