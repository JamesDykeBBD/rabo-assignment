package nl.rabobank.transactionverifier.data.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "reports")
public class ReportEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_id", nullable = false, unique = true)
    private String fileId;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "total_records", nullable = false)
    private Integer totalRecords;

    @Column(name = "valid_records", nullable = false)
    private Integer validRecords;

    @Column(name = "invalid_records", nullable = false)
    private Integer invalidRecords;

    @Column(name = "upload_date")
    private LocalDateTime uploadDate;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionEntity> transactions = new ArrayList<>();

    // Default constructor required by JPA
    public ReportEntity() {
    }

    // Constructor with fields
    public ReportEntity(String fileId, String fileName, Integer totalRecords, Integer validRecords, Integer invalidRecords) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.totalRecords = totalRecords;
        this.validRecords = validRecords;
        this.invalidRecords = invalidRecords;
        this.uploadDate = LocalDateTime.now();
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public List<TransactionEntity> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionEntity> transactions) {
        this.transactions = transactions;
    }

    // Helper method to add a transaction
    public void addTransaction(TransactionEntity transaction) {
        transactions.add(transaction);
        transaction.setReport(this);
    }

    // Helper method to remove a transaction
    public void removeTransaction(TransactionEntity transaction) {
        transactions.remove(transaction);
        transaction.setReport(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReportEntity that = (ReportEntity) o;

        return Objects.equals(fileId, that.fileId);
    }

    @Override
    public int hashCode() {
        return fileId != null ? fileId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ReportEntity{" +
                "id=" + id +
                ", fileId='" + fileId + '\'' +
                ", fileName='" + fileName + '\'' +
                ", totalRecords=" + totalRecords +
                ", validRecords=" + validRecords +
                ", invalidRecords=" + invalidRecords +
                ", uploadDate=" + uploadDate +
                '}';
    }
}