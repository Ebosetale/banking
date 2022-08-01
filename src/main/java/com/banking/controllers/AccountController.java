package com.banking.controllers;

import com.banking.Entities.Account;
import com.banking.dto.AccountDto;
import com.banking.services.IAccountService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/accounts", produces = "application/json")
public class AccountController {

    private IAccountService _accountService;
    private ModelMapper _mapper;

    public AccountController(IAccountService accountService, ModelMapper mapper) {
        _accountService = accountService;
        _mapper = mapper;
    }

    @GetMapping()
    public List<Account> loadAccounts(){
        Map<String, Account>  accounts = _accountService.getAccounts();
        if(accounts != null){
            return accounts.values().stream().toList();
        }
        return new ArrayList<>();
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Account> createAccount(@RequestBody AccountDto accountDto){
        ConcurrentHashMap<String, Account> accounts = _accountService.generateAccounts(accountDto);
        List<Account> accountNumbers = accounts.values()
                                              .stream()
                                              .collect(Collectors.toList());
        return accountNumbers;

    }
}
