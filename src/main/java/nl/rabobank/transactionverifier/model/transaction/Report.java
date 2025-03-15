package nl.rabobank.transactionverifier.model.transaction;

import java.util.List;

public class Report {

    private String fileId;
    private String fileName;
    private Integer totalRecords;
    private Integer validRecords;
    private Integer invalidRecords;
    private Long uploadDate;
    private List<Transaction> transactions;

    public Report(String fileId, String fileName, Integer totalRecords, Integer validRecords, Integer invalidRecords, Long uploadDate, List<Transaction> transactions) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.totalRecords = totalRecords;
        this.validRecords = validRecords;
        this.invalidRecords = invalidRecords;
        this.uploadDate = uploadDate;
        this.transactions = transactions;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }

    public Integer getValidRecords() {
        return validRecords;
    }

    public void setValidRecords(Integer validRecords) {
        this.validRecords = validRecords;
    }

    public Integer getInvalidRecords() {
        return invalidRecords;
    }

    public void setInvalidRecords(Integer invalidRecords) {
        this.invalidRecords = invalidRecords;
    }

    public Long getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Long uploadDate) {
        this.uploadDate = uploadDate;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public String toString() {
        return "Report{" +
                "fileId='" + fileId + '\'' +
                ", fileName='" + fileName + '\'' +
                ", totalRecords=" + totalRecords +
                ", validRecords=" + validRecords +
                ", invalidRecords=" + invalidRecords +
                ", uploadDate=" + uploadDate +
                '}';
    }
}
