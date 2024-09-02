package com.banking.controllers;

import com.banking.Entities.Account;
import com.banking.dto.AccountDto;
import com.banking.services.IAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.modelmapper.internal.util.Assert.notNull;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    IAccountService _accountService;

    @Autowired
    private AccountController _accountController;

    @Test
    void loadAccounts()  {

        AccountDto accountDto = AccountDto.builder()
                                .accountName("test")
                                .phoneNumber("1234567871")
                                .build();
        var genAccountRes = _accountController.createAccount(accountDto);
        assertThat(genAccountRes).isNotEmpty();

        var accountsRes = _accountController.loadAccounts();
        assertThat(accountsRes).isNotNull().isNotEmpty();

    }

    @Test
    void createAccount() {
        AccountDto account = AccountDto.builder()
                                        .accountName("test")
                                        .phoneNumber("123456787")
                                        .build();
        var res = _accountController.createAccount(account);
        assertThat(res).isNotEmpty();
    }
}