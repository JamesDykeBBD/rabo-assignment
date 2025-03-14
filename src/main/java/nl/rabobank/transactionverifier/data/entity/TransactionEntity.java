package nl.rabobank.transactionverifier.data.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reference", nullable = false, unique = true)
    private String reference;

    @Column(name = "account_number", nullable = false, length = 18)
    private String accountNumber;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "start_balance", nullable = false, precision = 10, scale = 2)
    private BigDecimal startBalance;

    @Column(name = "mutation", nullable = false, precision = 10, scale = 2)
    private BigDecimal mutation;

    @Column(name = "end_balance", nullable = false, precision = 10, scale = 2)
    private BigDecimal endBalance;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Default constructor
    public TransactionEntity() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getStartBalance() {
        return startBalance;
    }

    public void setStartBalance(BigDecimal startBalance) {
        this.startBalance = startBalance;
    }

    public BigDecimal getMutation() {
        return mutation;
    }

    public void setMutation(BigDecimal mutation) {
        this.mutation = mutation;
    }

    public BigDecimal getEndBalance() {
        return endBalance;
    }

    public void setEndBalance(BigDecimal endBalance) {
        this.endBalance = endBalance;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", reference='" + reference + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", description='" + description + '\'' +
                ", startBalance=" + startBalance +
                ", mutation=" + mutation +
                ", endBalance=" + endBalance +
                ", createdAt=" + createdAt +
                '}';
    }
}