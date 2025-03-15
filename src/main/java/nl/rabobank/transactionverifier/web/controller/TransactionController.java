package nl.rabobank.transactionverifier.web.controller;

import jakarta.servlet.http.HttpSession;
import nl.rabobank.transactionverifier.model.transaction.Report;
import nl.rabobank.transactionverifier.model.transaction.Transaction;
import nl.rabobank.transactionverifier.service.transaction.TransactionService;
import nl.rabobank.transactionverifier.translator.CSVTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    public static final String SESSION_KEY_TRANSACTION = "transactions";
    private static Logger LOG = LoggerFactory.getLogger(TransactionController.class);

    private final CSVTranslator csvTranslator;
    private final TransactionService transactionService;

    public TransactionController(CSVTranslator csvTranslator, TransactionService transactionService) {
        this.csvTranslator = csvTranslator;
        this.transactionService = transactionService;
    }

    @GetMapping("/validate")
    public String showValidationForm() {
        return "validation";
    }

    @GetMapping()
    public String showReports(HttpSession session, Model model) {
        @SuppressWarnings("unchecked")
        List<Report> reports = transactionService.getReports();
        LOG.info("Found {} reports", reports.size());
        model.addAttribute("reports", reports);
        return "reports";
    }

    @PostMapping("/validate")
    public String validateFile(@RequestParam("file") MultipartFile file, Model model, HttpSession session) {
        // Check if file is empty
        if (file.isEmpty()) {
            model.addAttribute("error", "Please select a file to upload");
            return "validation";
        }

        // Check file extension
        String fileName = file.getOriginalFilename();
        if (fileName == null || !(fileName.endsWith(".csv") || fileName.endsWith(".xml"))) {
            model.addAttribute("error", "Only CSV and XML files are allowed");
            return "validation";
        }

        try {
            List<Transaction> transactions = readTransactions(file);
            String fileId = UUID.randomUUID().toString();
            // Add attributes to model for the view
            model.addAttribute("fileName", fileName);
            model.addAttribute("fileId", fileId);
            model.addAttribute("transactions", transactions);
            model.addAttribute("totalRecords", transactions.size());
            model.addAttribute("invalidCount", 0);

            Report report = new Report(fileId, fileName, transactions.size(), 0, 0, System.currentTimeMillis(), transactions);
            transactionService.saveReport(report);

            return "validation";
        } catch (IOException e) {
            model.addAttribute("error", "Failed to process file: " + e.getMessage());
            LOG.error("Failed to process file: {}", e.getMessage());
            return "validation";
        } catch (Exception e) {
            LOG.error("An error occurred: {}", e.getMessage());
            model.addAttribute("error", "An error occurred: " + e.getMessage());
            return "validation";
        }
    }

    @GetMapping("/view/{fileId}")
    public String showReport(@PathVariable String fileId, HttpSession session, Model model) {
        @SuppressWarnings("unchecked")
        Report report = transactionService.getReport(fileId);
        model.addAttribute("fileName", report.getFileName());
        model.addAttribute("fileId", fileId);
        model.addAttribute("transactions", report.getTransactions());
        model.addAttribute("totalRecords", report.getTotalRecords());
        model.addAttribute("invalidCount", 0);
        return "validation";
    }

    private List<Transaction> readTransactions(MultipartFile file) throws IOException {
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
            LOG.info("Process {} transactions", transactions.size());
            return transactions;
        }
    }
}
