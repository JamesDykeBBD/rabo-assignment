package nl.rabobank.transactionverifier.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidCSVException extends RuntimeException {

    public InvalidCSVException() {
    }

    public InvalidCSVException(String message) {
        super(message);
    }
}
