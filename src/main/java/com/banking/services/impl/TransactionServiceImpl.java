package com.banking.services.impl;

import com.banking.Entities.Account;
import com.banking.dto.TransactionResponse;
import com.banking.dto.Transaction;
import com.banking.services.IAccountService;
import com.banking.services.ITransactionService;
import org.springframework.stereotype.Service;

import javax.naming.InsufficientResourcesException;
import javax.security.auth.login.AccountNotFoundException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

@Service
public class TransactionServiceImpl implements ITransactionService {

    private IAccountService _accountService;
    private static final double _TRANSACTION_AMOUNT = 200.0;
    private Map<String, List<Transaction>> _transactions = new ConcurrentHashMap<>();

    public TransactionServiceImpl(IAccountService accountService) {
        _accountService = accountService;
    }

    @Override
    public List<Transaction> getTransactions(String accountNumber) {
        if(accountNumber != null ){
            if(_transactions.containsKey(accountNumber)){
                return _transactions.get(accountNumber);
            }
        }else{
            List<Transaction> allTrxns = new ArrayList<>();
            _transactions.keySet().forEach(accntNumber -> allTrxns.addAll(_transactions.get(accntNumber)));
            return allTrxns;
        }
        return new ArrayList<>();
    }

    @Override
    public TransactionResponse creditAccount(String accountNumber) throws AccountNotFoundException {
        Account selectedAccountNullable = getAccount(accountNumber);
        if(selectedAccountNullable == null){
            throw new AccountNotFoundException("Account not found.");
        }
        IntStream.rangeClosed(1, 20)
                .parallel()
                .forEach(txn -> creditTransaction(accountNumber));

        TransactionResponse response = TransactionResponse.builder()
                                    .transactions(_transactions.get(accountNumber))
                                    .accounts(_accountService.getAccounts().values().stream().toList())
                                    .build();
        return response;
    }

    @Override
    public TransactionResponse debitAccount(String accountNumber, double amount) throws InsufficientResourcesException, AccountNotFoundException {
        Account selectedAccountNullable = getAccount(accountNumber);
        if(selectedAccountNullable == null){
            throw new AccountNotFoundException("Account not found.");
        }
        IntStream.rangeClosed(1, 25)
                .parallel()
                .forEach(txn -> debitTransaction(accountNumber));

        TransactionResponse response = TransactionResponse.builder()
                .transactions(_transactions.get(accountNumber))
                .accounts(_accountService.getAccounts().values().stream().toList())
                .build();
        return response;
    }

    private void debitTransaction(String accountNumber){
        Account selectedAccount = getAccount(accountNumber);
        double amount = getRandomAmount();

        Transaction newTransaction = Transaction.builder()
                .accountNumber(accountNumber)
                .amount(-amount).accountName(selectedAccount.getAccountName())
                .build();

        if(selectedAccount.getAvailableBalance() >= amount){
            selectedAccount.setAvailableBalance(selectedAccount.getAvailableBalance() - amount);
            _accountService.updateAccount(selectedAccount);
            newTransaction.setStatusMessage("Successful");
        }
        else{
            newTransaction.setStatusMessage("Insufficient balance");
        }

        List<Transaction> existingTxns = _transactions.getOrDefault(accountNumber, new ArrayList<>());
        existingTxns.add(newTransaction);
        _transactions.put(accountNumber, existingTxns);

    }
    private void creditTransaction(String accountNumber) {
        double amount = getRandomAmount();
        Account selectedAccount = getAccount(accountNumber);
        selectedAccount.setAvailableBalance(selectedAccount.getAvailableBalance() + amount);
        _accountService.updateAccount(selectedAccount);

        Transaction newTransaction = new Transaction();
        newTransaction.setAccountNumber(accountNumber);
        newTransaction.setAmount(amount);
        newTransaction.setStatusMessage("Successful");
        newTransaction.setAccountName(selectedAccount.getAccountName());

        List<Transaction> existingTxns = _transactions.getOrDefault(accountNumber, new ArrayList<>());
        existingTxns.add(newTransaction);
        _transactions.put(accountNumber, existingTxns);
    }

    private double getRandomAmount(){
        return Math.floor(Math.random() * 9_000) + 1_000;
    }
    private Account getAccount(String accountNumber){
        ConcurrentHashMap<String, Account> accounts = _accountService.getAccounts();
        if(accounts == null || accountNumber == null){
            return null;
        }
        Account selectedAccountNullable = accounts.getOrDefault(accountNumber, null);
        return selectedAccountNullable;
    }
}
