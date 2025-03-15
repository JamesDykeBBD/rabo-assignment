package nl.rabobank.transactionverifier.translator;

import nl.rabobank.transactionverifier.exceptions.InvalidCSVException;
import nl.rabobank.transactionverifier.exceptions.InvalidRecordException;
import nl.rabobank.transactionverifier.model.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CSVTranslator implements GenericTranslator<String> {

    private static final Logger LOG = LoggerFactory.getLogger(CSVTranslator.class);

    @Override
    public Transaction convertForward(String input) {
        if (!input.contains(",")) {
            throw new InvalidCSVException("input record is not a csv");
        }

        String[] record = input.split(",");
        if (record.length != 6) {
            throw new InvalidRecordException("record length invalid");
        }

        return getTransaction(record);
    }

    private Transaction getTransaction(String[] record) {
        Transaction transaction = new Transaction();
        try {
            transaction.setReference(record[0]);
            transaction.setAccountNumber(record[1]);
            transaction.setDescription(record[2]);
            transaction.setBalance(BigDecimal.valueOf(Double.parseDouble(record[3])));
            transaction.setMutation(record[4]);
            transaction.setEndBalance(BigDecimal.valueOf(Double.parseDouble(record[5])));
        } catch (NumberFormatException nfe) {
            throw new InvalidRecordException(nfe.getMessage());
        }
        return transaction;
    }

    @Override
    public String convertBackward(Transaction t) {
        return "%s,%s,%s,%.2f,%s,%.2f".formatted(t.getReference(), t.getAccountNumber(), t.getMutation(), t.getBalance(), t.getDescription(), t.getEndBalance());
    }
}
