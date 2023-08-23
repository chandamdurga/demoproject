package com.example.BankBranches.service;

import com.example.BankBranches.dto.*;
import com.nimbusds.jose.JOSEException;

public interface BankService {
    public GenericResponse signUp(AppUserDto request);

    GenericResponse signIn(AppUserDto request) throws JOSEException;

    GenericResponse saveMinister(AppUserDto request);

    GenericResponse saveChairman(AppUserDto request);

    GenericResponse saveManager(AppUserDto request);

    GenericResponse saveEmployee(AppUserDto request);

    GenericResponse findAll();

    public GenericResponse saveBank(BankDto request);

    public String saveBranch(BranchDto request);

    public String saveAccount(AccountDto request);
    public String saveUser(AppUserDto request);
    public Long countOfAUserOfABankByBankId(Long id);
    public Double balanceOfABankById(Long id);
    public String matchingAccountIdAndUserIdAndBalanceAndMakeTranscationLimit(Long accountId, Long userId, Double withDrawAmount);
    public GenericResponse deleteBankById(Long id) ;
}
