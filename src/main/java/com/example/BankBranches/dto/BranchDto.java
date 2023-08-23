package com.example.BankBranches.dto;

import com.example.BankBranches.entity.Account;
import com.example.BankBranches.entity.Bank;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BranchDto {
    private Long id;
    private String branchId;
    private String branchName;
    private LocalDate establishedOn;
    private String address;
    private BankDto bank;
    private List<AccountDto> accountsList;

}
