package nl.rabobank.transactionverifier.service.transaction;

import nl.rabobank.transactionverifier.data.dao.report.ReportDAO;
import nl.rabobank.transactionverifier.data.dao.transaction.TransactionDAO;
import nl.rabobank.transactionverifier.data.entity.ReportEntity;
import nl.rabobank.transactionverifier.data.entity.TransactionEntity;
import nl.rabobank.transactionverifier.model.transaction.Report;
import nl.rabobank.transactionverifier.model.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.util.List;

@Service
public class TransactionService {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionService.class);

    private final TransactionDAO transactionDAO;
    private final ReportDAO reportDAO;


    public TransactionService(TransactionDAO transactionDAO, ReportDAO reportDAO) {
        this.transactionDAO = transactionDAO;
        this.reportDAO = reportDAO;
    }

    public void saveReport(Report report) {
        ReportEntity reportEntity = createReportEntity(report);
        LOG.info("Saving report: {}", reportEntity);
        reportEntity = reportDAO.save(reportEntity);
        LOG.info("Saved report: {}", reportEntity.getFileId());
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
