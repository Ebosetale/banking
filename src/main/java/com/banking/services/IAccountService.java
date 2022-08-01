package com.banking.services;

import com.banking.Entities.Account;
import com.banking.dto.AccountDto;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public interface IAccountService {
    ConcurrentHashMap<String, Account> generateAccounts(AccountDto accountDto);
    ConcurrentHashMap<String, Account> getAccounts();
    void updateAccount(Account newAccounts);
}
