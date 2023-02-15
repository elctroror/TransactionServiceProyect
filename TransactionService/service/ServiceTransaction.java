package com.TransactionService.TransactionService.service;

import com.TransactionService.TransactionService.entity.PayType;
import com.TransactionService.TransactionService.entity.Transaction;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ServiceTransaction {

    public List<Transaction> findAll();
    public ResponseEntity finById(Long id);
    public ResponseEntity findByDate(LocalDateTime startDate, LocalDateTime endDate);
    public ResponseEntity initializeTransaction(Long accountId, Long receiverAccountId , BigDecimal amount, String payType);
}
