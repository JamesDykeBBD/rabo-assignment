package nl.rabobank.transactionverifier.web.controller;

import nl.rabobank.transactionverifier.model.transaction.Transaction;
import nl.rabobank.transactionverifier.service.transaction.TransactionService;
import nl.rabobank.transactionverifier.translator.CSVTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionVerificatonController {

    private static Logger LOG = LoggerFactory.getLogger(TransactionVerificatonController.class);

    private final CSVTranslator csvTranslator;
    private final TransactionService transactionService;

    public TransactionVerificatonController(CSVTranslator csvTranslator, TransactionService transactionService) {
        this.csvTranslator = csvTranslator;
        this.transactionService = transactionService;
    }

    @PostMapping(value = "/validate")
    public ResponseEntity<?> validateTransactions(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid request: Empty file");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
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
            LOG.info("Process {} transactions", transactions.size());

            return ResponseEntity.ok("Valid");
        } catch (IOException ex) {
            return ResponseEntity.internalServerError().body("Internal Error: %s".formatted(ex.getMessage()));
        }
    }
}
