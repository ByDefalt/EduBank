package com.example.clientAPI.entity;

public class BankAccountPivotEntity {

    private String bankAccountId;  // FK vers BankAccount
    private Integer accountId;      // FK vers Account

    // ----------------- Getters & Setters -----------------

    public String getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(String bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    // ----------------- toString -----------------
    @Override
    public String toString() {
        return "BankAccountPivotEntity{" +
                "bankAccountId='" + bankAccountId + '\'' +
                ", accountId=" + accountId +
                '}';
    }
}
