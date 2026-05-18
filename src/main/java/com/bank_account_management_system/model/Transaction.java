package com.bank_account_management_system.model;

import java.time.LocalDateTime;
import java.util.ArrayList;


public class Transaction implements Printable {

    private TransactionType type;
    private double amount;
    private LocalDateTime date;
    private int AccountId;
    private double BalanceBefore;
    private double BalanceAfter;
    private String currentUser;

    public Transaction(int AccountId, TransactionType type, double amount,
                double BalanceBefore,double BalanceAfter , String currentUser) {

        this.type = type;
        this.amount = amount;
        this.date = LocalDateTime.now();
        this.AccountId = AccountId;
        this.BalanceBefore = BalanceBefore;
        this.BalanceAfter = BalanceAfter;
        date = LocalDateTime.now();
        this.currentUser = currentUser;
    }

    public Transaction( int AccountId, TransactionType type, double amount,
                       double BalanceBefore,double BalanceAfter ,LocalDateTime date ,  String currentUser) {
        this.AccountId = AccountId;
        this.type = type;
        this.amount = amount;
        this.BalanceBefore = BalanceBefore;
        this.BalanceAfter = BalanceAfter;
        this.date = date;
        this.currentUser = currentUser;
    }


    public int getAccountId() {
        return AccountId;
    }
    public TransactionType getType() {
        return type;
    }
    public double getBalanceBefore() {
        return BalanceBefore;
    }
    public double getBalanceAfter() {
        return BalanceAfter;
    }
    public double getAmount() {
        return amount;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public String getCurrentUser() {
        return currentUser;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    public void setType(TransactionType type) {
        this.type = type;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public void setAccountId(int AccountId) {
        this.AccountId = AccountId;
    }
    public void setBalanceBefore(double BalanceBefore) {
        this.BalanceBefore = BalanceBefore;
    }
    public void setBalanceAfter(double BalanceAfter) {
        this.BalanceAfter = BalanceAfter;
    }
    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }


    @Override
    public String printDetails()
    {
        return
                " Account Id: "+AccountId+
                " | Type: "+type.toString()+
                " | Amount: "+ amount+
                " | BalanceBefore: "+BalanceBefore+
                " | BalanceAfter: "+BalanceAfter+
                " | Date: "+date.toString();

    }



}

