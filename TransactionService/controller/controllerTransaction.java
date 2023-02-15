package com.TransactionService.TransactionService.controller;

import com.TransactionService.TransactionService.entity.Transaction;
import com.TransactionService.TransactionService.service.ServiceImplementTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class controllerTransaction {

    @Autowired
    private ServiceImplementTransaction serviceTransaction;

    @GetMapping("/list")
    public List<Transaction> list(){

        return serviceTransaction.findAll();
    }

    @GetMapping("/list/{id}")
    public ResponseEntity idList(@PathVariable Long id){
      return serviceTransaction.finById(id);
       // return new ResponseEntity ("hola", HttpStatus.ACCEPTED );
    }

    @GetMapping("/listByDate/{startDate}/{endDate}")
    public ResponseEntity idList(@PathVariable LocalDateTime startDate, @PathVariable LocalDateTime endDate){
        return serviceTransaction.findByDate(startDate,endDate);

    }
    @GetMapping("/create/{accountId}/{receiverAccountId}/{amount}/{payType}")
    public ResponseEntity initializeTransaction(@PathVariable("accountId") Long accountId,
            @PathVariable("receiverAccountId") Long receiverAccountId ,
            @PathVariable("amount") BigDecimal amount,@PathVariable("payType") String payType){

            return serviceTransaction.initializeTransaction(accountId, receiverAccountId , amount, payType) ;
    }


}
