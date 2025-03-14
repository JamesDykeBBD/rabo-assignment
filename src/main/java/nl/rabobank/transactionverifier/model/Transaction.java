package nl.rabobank.transactionverifier.model;

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

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getDescription() {
        return description;
    }

    public Double getBalance() {
        return balance;
    }

    public String getMutation() {
        return mutation;
    }

    public Double getEndBalance() {
        return endBalance;
    }
}
