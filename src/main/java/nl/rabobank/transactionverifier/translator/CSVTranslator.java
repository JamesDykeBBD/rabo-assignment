package nl.rabobank.transactionverifier.translator;

import nl.rabobank.transactionverifier.exceptions.InvalidCSVException;
import nl.rabobank.transactionverifier.exceptions.InvalidRecordException;
import nl.rabobank.transactionverifier.model.transaction.Transaction;
import org.springframework.stereotype.Service;

@Service
public class CSVTranslator implements GenericTranslator<String> {

    @Override
    public Transaction convertForward(String input) {
        if (!input.contains(",")) {
            throw new InvalidCSVException("input record is not a csv");
        }

        String[] record = input.split(",");
        if (record.length != 6) {
            throw new InvalidRecordException("record length invalid");
        }

        Transaction transaction = new Transaction();
        try {
            transaction.setReference(record[0]);
            transaction.setAccountNumber(record[1]);
            transaction.setMutation(record[2]);
            transaction.setBalance(Double.parseDouble(record[3]));
            transaction.setDescription(record[4]);
            transaction.setEndBalance(Double.parseDouble(record[5]));
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
