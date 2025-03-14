package nl.rabobank.transactionverifier.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidRecordException extends RuntimeException {

    public InvalidRecordException(String message) {
        super(message);
    }
}
