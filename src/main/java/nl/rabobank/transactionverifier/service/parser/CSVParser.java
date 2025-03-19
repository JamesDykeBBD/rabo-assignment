package nl.rabobank.transactionverifier.service.parser;

import nl.rabobank.transactionverifier.model.transaction.Transaction;
import nl.rabobank.transactionverifier.translator.CSVTranslator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Service
public class CSVParser implements TransactionFileParser {

    private final CSVTranslator csvTranslator;

    public CSVParser(CSVTranslator csvTranslator) {
        this.csvTranslator = csvTranslator;
    }

    @Override
    public List<Transaction> parse(MultipartFile file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), Charset.forName("ISO-8859-2")))) {
            //Check if the first line is a header, or an actual record
            String firstLine = reader.readLine();
            boolean hasHeader = false;
            if (firstLine != null) {
                String[] parts = firstLine.split(",");
                //Assume a header if the first column is not numeric
                hasHeader = parts.length > 0 && !parts[0].matches("^[0-9]+$");
            }

            List<Transaction> transactions = new ArrayList<>(reader.lines()
                    .map(csvTranslator::convertForward)
                    .toList());

            //Because we've manually read the first line, we need to check if we should insert a true record at the head
            if (firstLine != null && !hasHeader) {
                transactions.add(0, csvTranslator.convertForward(firstLine));
            }
            return transactions;
        }
    }
}
