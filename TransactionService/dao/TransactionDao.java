package com.TransactionService.TransactionService.dao;

import com.TransactionService.TransactionService.entity.Transaction;
import org.springframework.data.repository.CrudRepository;

public interface TransactionDao extends CrudRepository<Transaction, Long> {
}
