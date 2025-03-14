package nl.rabobank.transactionverifier.service.transaction;

import nl.rabobank.transactionverifier.data.dao.transaction.TransactionDAO;
import nl.rabobank.transactionverifier.model.Transaction;
import nl.rabobank.transactionverifier.translator.CSVTranslator;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionDAO transactionDAO;


    public TransactionService(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

    public void validateTransaction(String report) {
    }

}
