package com.banking.services.impl;
import com.banking.Entities.Account;
import com.banking.dto.AccountDto;
import com.banking.services.IAccountService;
import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

/**
 * Generate accounts with pre-assigned account number and available balance of 0.0.
 * Assumptions:
 *              1. New set of accounts are generated and replace existing ones every time generateAccounts() is called.
 *              2. 5004 is the prefix value for the generated account numbers across the bank.
 */
@Service
public class AccountServiceImpl implements IAccountService {
    private ConcurrentHashMap<String, Account> accounts;
    private static final String _PREFIX = "5004";

    @Override
    public ConcurrentHashMap<String, Account> generateAccounts(AccountDto accountDto) {
        accounts = new ConcurrentHashMap<>();
        IntStream.rangeClosed(1, 10)
                .parallel()
                .forEach(x -> generateAccount(x, accountDto));
        return accounts;
    }

    @Override
    public ConcurrentHashMap<String, Account> getAccounts(){
        return accounts;
    }

    @Override
    public void updateAccount(Account newAccount) {
        accounts.put(newAccount.getAccountNumber(), newAccount);
    }

    private void generateAccount(int digit, AccountDto accountDto){
        Account newAccount = new Account();
        long number = digit < 10 ? (long) Math.floor(Math.random() * 9_000_0L) + 1_000_0L : (long) Math.floor(Math.random() * 9_000L) + 1_000L;
        newAccount.setAccountNumber(_PREFIX.concat(String.valueOf(number).concat(String.valueOf(digit))));
        newAccount.setAccountName(accountDto.getAccountName().concat(String.valueOf(digit)));
        newAccount.setPhoneNumber(accountDto.getPhoneNumber());
        accounts.put(newAccount.getAccountNumber(), newAccount);
    }
}
