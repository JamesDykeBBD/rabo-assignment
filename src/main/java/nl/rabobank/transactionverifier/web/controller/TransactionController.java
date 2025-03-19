package nl.rabobank.transactionverifier.web.controller;

import jakarta.servlet.http.HttpSession;
import nl.rabobank.transactionverifier.exceptions.FailedProcessingException;
import nl.rabobank.transactionverifier.model.transaction.Report;
import nl.rabobank.transactionverifier.model.transaction.Transaction;
import nl.rabobank.transactionverifier.service.transaction.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    public static final String SESSION_KEY_TRANSACTION = "transactions";
    private static Logger LOG = LoggerFactory.getLogger(TransactionController.class);

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
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
            model.addAttribute("error", "Only CSV or XML files are allowed");
            return "validation";
        }

        try {
            Report report = transactionService.saveReportFromFile(file).orElseThrow(() -> new FailedProcessingException("unable to parse records: no data"));

            // Add attributes to model for the view
            model.addAttribute("fileName", fileName);
            model.addAttribute("fileId", report.getFileId());
            model.addAttribute("transactions", report.getTransactions());
            model.addAttribute("totalRecords", report.getTransactions().size());
            model.addAttribute("invalidCount", 0);

        } catch (Exception e) {
            LOG.error("An error occurred: {}", e.getMessage());
            model.addAttribute("error", "An error occurred: " + e.getMessage());
        }
        return "validation";
    }

    @GetMapping("/view/{fileId}")
    public String showReport(@PathVariable String fileId, Model model) {
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
        switch (file.getContentType()) {
            case "text/csv":
            case "application/xml":
            default:
        }

        return new ArrayList<>();
    }



    private List<Transaction> readTransactionsFromXML(MultipartFile file) throws IOException {
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), Charset.forName("ISO-8859-2")))) {
//
//        }
        return new ArrayList<>();
    }
}
