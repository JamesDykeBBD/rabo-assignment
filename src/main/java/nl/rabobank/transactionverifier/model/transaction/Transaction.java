package nl.rabobank.transactionverifier.model.transaction;

public class Transaction {

    public String reference;
    public String accountNumber;
    public String description;
    public Double balance;
    public String mutation;
    public Double endBalance;

    public Transaction() {
    }

    public Transaction(String reference, String accountNumber, String description, Double balance, String mutation, Double endBalance) {
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

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getMutation() {
        return mutation;
    }

    public void setMutation(String mutation) {
        this.mutation = mutation;
    }

    public Double getEndBalance() {
        return endBalance;
    }

    public void setEndBalance(Double endBalance) {
        this.endBalance = endBalance;
    }
}
