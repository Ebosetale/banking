package com.banking.controllers;

import com.banking.dto.AccountDto;
import com.banking.dto.Transaction;
import com.banking.services.IAccountService;
import com.banking.services.ITransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionsControllerTest {
    @Autowired
    IAccountService _accountService;
    @Autowired
    private TransactionsController _trxnController;

    @Test
    void getTransactions() {
        AccountDto accountDto = AccountDto.builder()
                .accountName("test")
                .phoneNumber("123456765")
                .build();
        _accountService.generateAccounts(accountDto);
        var account = _accountService.getAccounts().values().stream().findFirst();
        Transaction transaction = Transaction.builder()
                .accountNumber(account.get().getAccountNumber())
                .amount(5000000L)
                .build();
        var trxnRes = _trxnController.creditAccount(transaction);
        var res = _trxnController.getTransactions(Optional.of(account.get().getAccountNumber()));
        assertThat(res).isNotEmpty();
    }

    @Test
    void creditAccount() {

        AccountDto accountDto = AccountDto.builder()
                                .accountName("test")
                                .phoneNumber("123456765")
                                .build();
        _accountService.generateAccounts(accountDto);
        var account = _accountService.getAccounts().values().stream().findFirst();
        Transaction transaction = Transaction.builder()
                .accountNumber(account.get().getAccountNumber())
                .amount(5000000L)
                .build();
        var res = _trxnController.creditAccount(transaction);
        assertThat(res).isNotNull();

        var txnRes = res.getTransactions();
        assertThat(txnRes).isNotEmpty();

        var failed = txnRes.stream()
                                          .filter(t -> !t.getStatusMessage().toLowerCase().trim().contains("successful"))
                                          .collect(Collectors.toList());
        assertThat(failed).isEmpty();
    }

    @Test
    void debitAccountNumber() {
        AccountDto accountDto = AccountDto.builder()
                .accountName("test")
                .phoneNumber("123456765")
                .build();
        _accountService.generateAccounts(accountDto);
        var account = _accountService.getAccounts().values().stream().findFirst();
        Transaction transaction = Transaction.builder()
                .accountNumber(account.get().getAccountNumber())
                .amount(5000000L)
                .build();
        var txnRes = _trxnController.creditAccount(transaction);
        var res = _trxnController.debitAccountNumber(transaction);

        assertThat(res).isNotNull();

        var txns = res.getTransactions();
        assertThat(txns).isNotEmpty();
    }

    @Test
    void debitAccountNumberfailed() {
        AccountDto accountDto = AccountDto.builder()
                .accountName("test")
                .phoneNumber("123456765")
                .build();
        _accountService.generateAccounts(accountDto);
        var account = _accountService.getAccounts().values().stream().findFirst();
        Transaction transaction = Transaction.builder()
                .accountNumber(account.get().getAccountNumber())
                .amount(5000000L)
                .build();
        var txnRes = _trxnController.debitAccountNumber(transaction);

        // this should fail as there is 0.0 balance in the account.
        var success = txnRes.getTransactions().stream()
                .filter(t -> t.getStatusMessage().toLowerCase().trim().contains("successful"))
                .collect(Collectors.toList());
        assertThat(success).isEmpty();
    }
}