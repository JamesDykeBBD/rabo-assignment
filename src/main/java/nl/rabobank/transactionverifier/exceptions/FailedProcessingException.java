package nl.rabobank.transactionverifier.exceptions;

public class FailedProcessingException extends RuntimeException {

    public FailedProcessingException(String message) {
        super(message);
    }

    public FailedProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
