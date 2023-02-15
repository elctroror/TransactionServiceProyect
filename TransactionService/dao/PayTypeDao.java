package com.TransactionService.TransactionService.dao;

import com.TransactionService.TransactionService.entity.PayType;
import org.springframework.data.repository.CrudRepository;

public interface PayTypeDao extends CrudRepository<PayType, Long> {
}
