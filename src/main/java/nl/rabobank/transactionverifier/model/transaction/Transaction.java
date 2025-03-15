package nl.rabobank.transactionverifier.model.transaction;

import java.io.Serializable;
import java.math.BigDecimal;

public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;
    public String reference;
    public String accountNumber;
    public String description;
    public BigDecimal balance;
    public String mutation;
    public BigDecimal endBalance;

    public Transaction() {
    }

    public Transaction(String reference, String accountNumber, String description, BigDecimal balance, String mutation, BigDecimal endBalance) {
        this.reference = reference;
        this.accountNumber = accountNumber;
        this.description = description;
        this.balance = balance;
        this.mutation = mutation;
        this.endBalance = endBalance;
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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getMutation() {
        return mutation;
    }

    public void setMutation(String mutation) {
        this.mutation = mutation;
    }

    public BigDecimal getEndBalance() {
        return endBalance;
    }

    public void setEndBalance(BigDecimal endBalance) {
        this.endBalance = endBalance;
    }

    public boolean isValid() {
        return true;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "reference='" + reference + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", description='" + description + '\'' +
                ", balance=" + balance +
                ", mutation='" + mutation + '\'' +
                ", endBalance=" + endBalance +
                '}';
    }
}
