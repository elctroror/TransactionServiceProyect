package com.TransactionService.TransactionService.service;

import brave.Tracer;
import com.TransactionService.TransactionService.dao.PayTypeDao;
import com.TransactionService.TransactionService.dao.TransactionDao;
import com.TransactionService.TransactionService.entity.PayType;
import com.TransactionService.TransactionService.entity.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceImplementTransaction implements ServiceTransaction {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    TransactionDao transactionDao;

    @Autowired
    PayTypeDao payTypeDao;

    @Autowired
    private Tracer tracer;

    private static String PATTERN_AMOUNT_TRANSACTION = "[0-9]+\\.[0-9]{2}";
    @Override
    public List<Transaction> findAll() {

        return (List<Transaction>)transactionDao.findAll();

    }

    @Override
    public ResponseEntity finById(Long id) {

        try {
            Optional<Transaction> transaction = transactionDao.findById(id);
            if(transaction.isPresent()==true){
                tracer.currentSpan().tag("transaction find","HttpStatus.ACCEPTED");
                return new ResponseEntity( transaction, HttpStatus.ACCEPTED);
            }
            tracer.currentSpan().tag("there are not Transactions that match this id","HttpStatus.BAD_REQUEST");
            return new ResponseEntity ("there are not Transactions that match this id" , HttpStatus.BAD_REQUEST);

        }catch (RuntimeException e){

            System.out.println("Exception: "+e);
            tracer.currentSpan().tag("something go bad","HttpStatus.BAD_REQUEST");
            return new ResponseEntity ("something go bad" , HttpStatus.BAD_REQUEST);
        }

    }
    @Override
    public ResponseEntity findByDate(LocalDateTime startDate, LocalDateTime endDate){

        try {
            List<Transaction> listTransaction = (List<Transaction>) transactionDao.findAll();
            List<Transaction>  returnListTransaction = new ArrayList<>();

            for(int i=0; i<listTransaction.size(); i++){
                if(listTransaction.get(i).getDate().isBefore(endDate) && listTransaction.get(i).getDate().isAfter(startDate) ){
                    returnListTransaction.add(listTransaction.get(i));
                }
            }
            if (returnListTransaction.size()>0){
                tracer.currentSpan().tag("list transaction","HttpStatus.BAD_REQUEST");
                return new ResponseEntity(listTransaction, HttpStatus.ACCEPTED) ;
            }
            tracer.currentSpan().tag("There is not a Transaction in this date","HttpStatus.BAD_REQUEST");
            return new ResponseEntity("There is not a Transaction in this date", HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            System.out.println("Exception: "+e);
            tracer.currentSpan().tag("Something go bad","HttpStatus.BAD_REQUEST");
            return new ResponseEntity("Something go bad",HttpStatus.BAD_REQUEST);
        }

    }
   @Override
    public ResponseEntity initializeTransaction(Long accountId, Long receiverAccountId , BigDecimal amount, String payType) {

        try{
           if(checkAccountExist(accountId,receiverAccountId)){

               Boolean amountCheck = checkAmount(amount);

               if(amountCheck){

                   PayType myPayType = checkPayType(payType);
                   if(myPayType != null){

                       Boolean transactionCompleted = restTemplate.getForObject("http://account-service/api/account/transaction/"+accountId+"/"+receiverAccountId+"/"+amount, Boolean.class);
                       if(transactionCompleted){
                           String accountNumber = restTemplate.getForObject("http://account-service/api/account/findAccountNumber/"+accountId, String.class);

                           Transaction transaction = new Transaction( accountNumber,accountId,  receiverAccountId, LocalDateTime.now() , amount, myPayType.getType());
                           transactionDao.save(transaction);
                           tracer.currentSpan().tag("transaction created","HttpStatus.ACCEPTED");
                           return new ResponseEntity( transaction, HttpStatus.ACCEPTED);
                       }
                       tracer.currentSpan().tag("Not Enough salary","HttpStatus.BAD_REQUEST");
                       return new ResponseEntity("Not Enough salary", HttpStatus.BAD_REQUEST);
                   }
                   tracer.currentSpan().tag("Not Enough salary","HttpStatus.BAD_REQUEST");
                   return new ResponseEntity("payType not accepted", HttpStatus.BAD_REQUEST);
               }
               tracer.currentSpan().tag("amount not accepted","HttpStatus.BAD_REQUEST");
               return new ResponseEntity("amount not accepted", HttpStatus.BAD_REQUEST);
           }
            tracer.currentSpan().tag("accounts not available","HttpStatus.BAD_REQUEST");
            return new ResponseEntity("accounts not available", HttpStatus.BAD_REQUEST);

        }catch(Exception e){
            System.out.println("Exception: "+e);
            tracer.currentSpan().tag("something go bad","HttpStatus.BAD_REQUEST");
            return new ResponseEntity ("something go bad" , HttpStatus.BAD_REQUEST);
       }

    }

    private Boolean checkAccountExist(Long accountId,Long receiverId){

        Boolean accountValidation = restTemplate.getForObject("http://account-service/api/account/findActive/"+accountId, Boolean.class);
        Boolean receiverAccountValidation = restTemplate.getForObject("http://account-service/api/account/findActive/"+receiverId, Boolean.class);

        if(accountValidation && receiverAccountValidation ){
            return true;
        }
        return false;

    }

    private Boolean checkAmount(BigDecimal amount){

        BigDecimal zero =new BigDecimal(0);

        try {

            if(amount.compareTo(zero)>0 && amount.toPlainString().matches(PATTERN_AMOUNT_TRANSACTION)){
                BigDecimal numberZero = new BigDecimal(0);
                return true;
            }else if(amount.compareTo(zero)<=0){
                throw new RuntimeException("the amount need to be more than 0");
            }else{
                throw new RuntimeException("the amount need 2 decimals");
            }

        }catch (Exception e){
            System.out.println("Exception"+e);
            return false;
        }

    }

    private PayType checkPayType(String payType){

        List<PayType> listPayType = (List<PayType>)payTypeDao.findAll();
        PayType myPayType = null ;

        try {
            if(listPayType.size()>0) {
                for (int i = 0; i <= listPayType.size(); i++) {

                    if (listPayType.get(i).getType().equals(payType)) {
                        System.out.println("hola"+listPayType.get(i));
                        myPayType = listPayType.get(i);
                        return myPayType;
                    }
                }
            }

            throw new RuntimeException("payType not valid");

        }catch (Exception e){
            System.out.println("Exception: "+e);
            return myPayType;
        }

    }

}
