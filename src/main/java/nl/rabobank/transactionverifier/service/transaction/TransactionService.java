package nl.rabobank.transactionverifier.service.transaction;

import nl.rabobank.transactionverifier.data.dao.report.ReportDAO;
import nl.rabobank.transactionverifier.data.dao.transaction.TransactionDAO;
import nl.rabobank.transactionverifier.data.entity.ReportEntity;
import nl.rabobank.transactionverifier.data.entity.TransactionEntity;
import nl.rabobank.transactionverifier.model.transaction.Report;
import nl.rabobank.transactionverifier.model.transaction.Transaction;
import nl.rabobank.transactionverifier.service.parser.CSVParser;
import nl.rabobank.transactionverifier.service.validator.ValidationResult;
import nl.rabobank.transactionverifier.service.validator.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransactionService {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionService.class);

    private final TransactionDAO transactionDAO;
    private final ReportDAO reportDAO;
    private final List<Validator<Transaction>> validators;
    private final CSVParser csvParser;


    public TransactionService(TransactionDAO transactionDAO, ReportDAO reportDAO, List<Validator<Transaction>> validators, CSVParser csvParser) {
        this.transactionDAO = transactionDAO;
        this.reportDAO = reportDAO;
        this.validators = validators;
        this.csvParser = csvParser;
    }

    public Optional<Report> saveReportFromFile(MultipartFile file) {
        try {
            List<Transaction> transactions = csvParser.parse(file);
            List<ValidationResult<Transaction>> validationResults = validators.stream().flatMap(validator -> validator.validate(transactions).stream()).toList();
            List<ValidationResult<Transaction>> invalidTransactions = validationResults.stream().filter(result -> !result.isValid()).toList();
            Report report = new Report(UUID.randomUUID().toString(), file.getName(), transactions.size(), transactions.size() - invalidTransactions.size(), invalidTransactions.size(), System.currentTimeMillis(), transactions);
            reportDAO.save(createReportEntity(report));
            return Optional.of(report);
        } catch (IOException ioe) {
            return Optional.empty();
        }
    }

    public List<Transaction> getTransactionsForReport(long reportId) {
        return transactionDAO.findAllForReport(reportId).stream().map(this::createTransaction).toList();
    }

    public List<Report> getReports() {
        return reportDAO.findAll().stream().map(this::createReport).toList();
    }

    public Report getReport(String fileId) {
        return reportDAO.findByReportId(fileId).map(this::createReport).orElseThrow(() -> new IllegalArgumentException("Report not found"));
    }

    private TransactionEntity createTransactionEntity(Transaction transaction) {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setReference(transaction.getReference());
        transactionEntity.setAccountNumber(transaction.getAccountNumber());
        transactionEntity.setDescription(transaction.getDescription());
        transactionEntity.setStartBalance(transaction.getBalance());
        transactionEntity.setMutation(transaction.getMutation());
        transactionEntity.setEndBalance(transaction.getEndBalance());
        return transactionEntity;
    }

    private Transaction createTransaction(TransactionEntity transactionEntity) {
        Transaction transaction = new Transaction();
        transaction.setReference(transactionEntity.getReference());
        transaction.setAccountNumber(transactionEntity.getAccountNumber());
        transaction.setDescription(transactionEntity.getDescription());
        transaction.setBalance(transactionEntity.getStartBalance());
        transaction.setMutation(transactionEntity.getMutation());
        transaction.setEndBalance(transactionEntity.getEndBalance());
        return transaction;
    }

    private Report createReport(ReportEntity reportEntity) {
        Report report = new Report();
        report.setFileId(reportEntity.getFileId());
        report.setFileName(reportEntity.getFileName());
        report.setTotalRecords(reportEntity.getTotalRecords());
        report.setValidRecords(reportEntity.getValidRecords());
        report.setInvalidRecords(reportEntity.getInvalidRecords());
        report.setTransactions(reportEntity.getTransactions().stream().map(this::createTransaction).toList());
        report.setUploadDate(reportEntity.getUploadDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return report;
    }

    private ReportEntity createReportEntity(Report report) {
        ReportEntity reportEntity = new ReportEntity();
        reportEntity.setUploadDate(LocalDateTime.now());
        reportEntity.setFileId(report.getFileId());
        reportEntity.setFileName(report.getFileName());
        reportEntity.setTotalRecords(report.getTotalRecords());
        reportEntity.setValidRecords(report.getValidRecords());
        reportEntity.setInvalidRecords(report.getInvalidRecords());
        reportEntity.setTransactions(report.getTransactions().stream().map(transaction -> {
            TransactionEntity transactionEntity = this.createTransactionEntity(transaction);
            transactionEntity.setReport(reportEntity);
            return transactionEntity;
        }).toList());
        return reportEntity;
    }

}
