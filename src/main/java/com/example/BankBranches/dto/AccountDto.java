package com.example.BankBranches.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private Long id;
    private String accountNumber;
    private String holderName;
    private LocalDate openedOn;
    private Double balance;
    private String nominee;
    private BranchDto branch;
    private AppUserDto user;

}
