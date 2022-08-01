package com.banking.Entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    private String accountName;
    private String phoneNumber;
    private String accountNumber;
    private double availableBalance;
}
