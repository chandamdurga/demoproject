package com.example.BankBranches.dto;

import com.example.BankBranches.enums.Roles;
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
public class AppUserDto {
    private Long id;
    private String userName;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String address;
    private String email;
    private String passWord;
    private List<AccountDto> accountsList;
    private Roles role;


}
