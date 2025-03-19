package nl.rabobank.transactionverifier.service.parser;

import nl.rabobank.transactionverifier.model.transaction.Transaction;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TransactionFileParser {

    List<Transaction> parse(MultipartFile file) throws IOException;

}
