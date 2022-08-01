package com.banking.controllers;

import com.banking.dto.TransactionResponse;
import com.banking.dto.Transaction;
import com.banking.services.ITransactionService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/transactions", produces = "application/json")
public class TransactionsController {
    private ITransactionService _transactionService;
    private ModelMapper _mapper;

    public TransactionsController(ITransactionService transactionService, ModelMapper mapper) {
        _transactionService = transactionService;
        _mapper = mapper;
    }

    @RequestMapping(value = {"", "/{accountNumber}"}, method = RequestMethod.GET)
    public List<Transaction> getTransactions(@PathVariable("accountNumber") Optional<String> accountNumber){
        try{
            return _transactionService.getTransactions(accountNumber.orElse(null));
        }catch(Exception ex){
            // handle error

            System.out.println(ex);
        }
        return new ArrayList<>();
    }

    @PostMapping("/creditAccount")
    public TransactionResponse creditAccount(@RequestBody Transaction transaction){
        try{
            return _transactionService.creditAccount(transaction.getAccountNumber());
        }catch(AccountNotFoundException ex){
            // handle error
            System.out.println(ex);
        }
        return new TransactionResponse();
    }

    @PostMapping("/debitAccount")
    public TransactionResponse debitAccountNumber(@RequestBody Transaction transaction){
        try{
            return _transactionService.debitAccount(transaction.getAccountNumber(), transaction.getAmount());
        }catch (Exception ex){
            // handle exception//

            System.out.println(ex);
        }
        return new TransactionResponse();
    }
}
