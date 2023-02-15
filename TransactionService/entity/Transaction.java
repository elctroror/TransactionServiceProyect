package com.TransactionService.TransactionService.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String accountNumber;

    @Column
    private Long accountId;

    @Column
    private Long receiverAccountId;

    @Column
    private BigDecimal amount;

    @Column
    private LocalDateTime date;

    @Column
    private String payType;


    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getReceiverAccountId() {
        return receiverAccountId;
    }

    public void setReceiverAccountId(Long receiverAccountId) {
        this.receiverAccountId = receiverAccountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Transaction(){

    }

    public Transaction( String accountNumber, Long accountId,Long receiverAccountId, LocalDateTime date, BigDecimal amount,String payType ){
           this.accountNumber=accountNumber;
           this.accountId=accountId;
           this.receiverAccountId=receiverAccountId;
           this.date=date;
           this.amount=amount;
           this.payType=payType;
    }

}
