package com.banking.services;

import com.banking.dto.TransactionResponse;
import com.banking.dto.Transaction;

import javax.naming.InsufficientResourcesException;
import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

public interface ITransactionService {
    List<Transaction> getTransactions(String accountNumber);
    TransactionResponse creditAccount(String accountNumber) throws AccountNotFoundException;
    TransactionResponse debitAccount(String accountNumber, double amount) throws InsufficientResourcesException, AccountNotFoundException;
}
