package com.example.BankBranches.controller;

import com.example.BankBranches.dto.*;
import com.example.BankBranches.service.BankService;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bank-branch")
public class BankServiceController {
    @Autowired
    BankService bankService;

    @PostMapping("/save-bank")
    @PreAuthorize("hasAnyAuthority('GOVERNOR','MANAGER')")
    public GenericResponse saveBank(@RequestBody BankDto request) {
        return bankService.saveBank(request);
    }

    @PostMapping("/save-branch")
    public String saveBranch(@RequestBody BranchDto request) {
        return bankService.saveBranch(request);
    }

    @PostMapping("/save-Account")
    public String saveAccount(@RequestBody AccountDto request) {
        return bankService.saveAccount(request);
    }

    @PostMapping("/save-user")
    public String saveUser(@RequestBody AppUserDto request) {
        return bankService.saveUser(request);
    }

    @GetMapping("/count-of-users-by-bank-id/{id}")
    public Long countOfAUserOfABankByBankId(@PathVariable Long id) {
        return bankService.countOfAUserOfABankByBankId(id);
    }

    @GetMapping("balance-of-every-bank-by-id/{id}")
    public Double balanceOfABankById(@PathVariable Long id) {
        return bankService.balanceOfABankById(id);
    }

    @PutMapping("withdraw-from-account/{accountId}/{userId}/{withDrawAmount}")
    public String matchingAccountIdAndUserIdAndBalance(@PathVariable Long accountId, @PathVariable Long userId, @PathVariable Double withDrawAmount) {
        return bankService.matchingAccountIdAndUserIdAndBalanceAndMakeTranscationLimit(accountId, userId, withDrawAmount);
    }

    @PostMapping("signUp")
    public GenericResponse signUp(@RequestBody AppUserDto request) {
        return bankService.signUp(request);
    }
    @PostMapping("save-minister")
    @PreAuthorize("hasAuthority('GOVERNOR')")
    public GenericResponse saveMinister(@RequestBody AppUserDto request) {
        return bankService.saveMinister(request);
    }
    @PostMapping("save-chairman")
    @PreAuthorize("hasAuthority('MINISTER')")
    public GenericResponse saveChairman(AppUserDto request) {
        return bankService.saveMinister(request);
    }
    @PostMapping("save-manager")
    @PreAuthorize("hasAuthority('CHAIRMAN')")
    public GenericResponse saveManager(AppUserDto request){
        return bankService.saveMinister(request);
    }
    @PostMapping("save-employee")
    @PreAuthorize("hasAuthority('MANAGER')")
    public GenericResponse saveEmployee(AppUserDto request) {
        return bankService.saveMinister(request);
    }

    @PostMapping("sign-in")
    public GenericResponse signIn(@RequestBody AppUserDto request)throws JOSEException {
        return bankService.signIn(request);
    }
    @GetMapping("findAll")
    @PreAuthorize("hasAnyAuthority('GOVERNOR','MINISTER','CHAIRMAN','MANAGER','EMPLOYEE','CUSTOMER')")
    public GenericResponse findAll() {
        return bankService.findAll();
    }
    @DeleteMapping("deletebank/{id}")
    @PreAuthorize("hasAuthority('GOVERNOR')")
    public GenericResponse deleteById(@PathVariable Long id){
        return bankService.deleteBankById(id);
    }

}
