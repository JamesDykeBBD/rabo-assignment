package nl.rabobank.transactionverifier.service.transaction;

import nl.rabobank.transactionverifier.data.dao.transaction.TransactionDAO;
import nl.rabobank.transactionverifier.model.Transaction;
import nl.rabobank.transactionverifier.translator.CSVTranslator;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionDAO transactionDAO;
    private final CSVTranslator csvTranslator;


    public TransactionService(TransactionDAO transactionDAO, CSVTranslator csvTranslator) {
        this.transactionDAO = transactionDAO;
        this.csvTranslator = csvTranslator;
    }

}
