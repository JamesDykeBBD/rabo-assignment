package nl.rabobank.transactionverifier.service.transaction;

import nl.rabobank.transactionverifier.data.dao.transaction.TransactionDAO;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final TransactionDAO transactionDAO;


    public TransactionService(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

    public void validateTransaction(String report) {
    }

}
