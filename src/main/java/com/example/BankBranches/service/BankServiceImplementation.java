package com.example.BankBranches.service;

import com.example.BankBranches.dto.*;
import com.example.BankBranches.entity.Account;
import com.example.BankBranches.entity.Bank;
import com.example.BankBranches.entity.Branch;
import com.example.BankBranches.entity.AppUser;
import com.example.BankBranches.enums.Roles;
import com.example.BankBranches.repository.AccountRepo;
import com.example.BankBranches.repository.BankRepo;
import com.example.BankBranches.repository.BranchRepo;
import com.example.BankBranches.repository.UserRepo;
import com.example.BankBranches.security.configuration.JwtTokenUtils;
import com.nimbusds.jose.JOSEException;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BankServiceImplementation implements BankService {

    @Autowired
    BankRepo bankRepo;
    @Autowired
    BranchRepo branchRepo;
    @Autowired
    AccountRepo accountRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    JwtTokenUtils jwtTokenUtils;

    private BankDto bankToDto(Bank entity) {
        BankDto bankDto = new BankDto();
        bankDto.setId(entity.getId());
        bankDto.setBankName(entity.getBankName());
        bankDto.setHeadQuarters(entity.getHeadQuarters());
        bankDto.setFounder(entity.getFounder());
        bankDto.setEstablishedOn(entity.getEstablishedOn());
        if (entity.getBranchList() != null) {
            bankDto.setBranchList((entity.getBranchList()).stream().peek(a -> a.setBank(null)).map(this::branchToDto).toList());
        }
        return bankDto;
    }

    private Bank dtoToBank(BankDto request) {
        Bank bank = new Bank();
        bank.setId(request.getId());
        bank.setBankName(request.getBankName());
        bank.setHeadQuarters(request.getHeadQuarters());
        bank.setFounder(request.getFounder());
        bank.setEstablishedOn(request.getEstablishedOn());
        if (request.getBranchList() != null) {
            bank.setBranchList((request.getBranchList().stream().peek(a -> a.setId(null)).map(this::dtoToBranch).toList()));
        }
        return bank;
    }

    private BranchDto branchToDto(Branch entity) {
        BranchDto branchDto = new BranchDto();
        branchDto.setId(entity.getId());
        branchDto.setBranchId(entity.getBranchId());
        branchDto.setBranchName(entity.getBranchName());
        branchDto.setEstablishedOn(entity.getEstablishedOn());
        branchDto.setAddress(entity.getAddress());
        if (entity.getBank() != null) {
            entity.getBank().setBranchList(null);
            branchDto.setBank(bankToDto(entity.getBank()));
        }
        if (entity.getAccountsList() != null) {
            branchDto.setAccountsList(entity.getAccountsList().stream().peek(a -> a.setId(null)).map(this::accountToDto).toList());
        }
        return branchDto;
    }

    private Branch dtoToBranch(BranchDto request) {
        Branch branch = new Branch();
        branch.setId(request.getId());
        branch.setBranchId(request.getBranchId());
        branch.setBranchName(request.getBranchName());
        branch.setEstablishedOn(request.getEstablishedOn());
        branch.setAddress(request.getAddress());
        if (request.getBank() != null) {
            branch.setBank(dtoToBank(request.getBank()));
        }
        if (request.getAccountsList() != null) {
            branch.setAccountsList(request.getAccountsList().stream().map(this::dtoToAccount).toList());
        }
        return branch;
    }

    private Account dtoToAccount(AccountDto request) {
        Account account = new Account();
        account.setId(request.getId());
        account.setAccountNumber(request.getAccountNumber());
        account.setHolderName(request.getHolderName());
        account.setOpenedOn(request.getOpenedOn());
        account.setBalance(request.getBalance());
        account.setNominee(request.getNominee());
        if (request.getBranch() != null) {
            account.setBranch(dtoToBranch(request.getBranch()));
        }
        if (request.getUser() != null) {
            account.setAppUser((dtoToUser(request.getUser())));
        }
        return account;
    }

    private AccountDto accountToDto(Account entity) {
        AccountDto accountDto = new AccountDto();
        accountDto.setId(entity.getId());
        accountDto.setAccountNumber(entity.getAccountNumber());
        accountDto.setHolderName(entity.getHolderName());
        accountDto.setOpenedOn(entity.getOpenedOn());
        accountDto.setBalance(entity.getBalance());
        accountDto.setNominee(entity.getNominee());
        if (entity.getBranch() != null) {
            entity.getBranch().setAccountsList(null);
            accountDto.setBranch(branchToDto(entity.getBranch()));
        }
        if (entity.getAppUser() != null) {
            entity.getAppUser().setAccountsList(null);
            accountDto.setUser(userToDto(entity.getAppUser()));
        }
        return accountDto;
    }

    private AppUser dtoToUser(AppUserDto request) {
        AppUser user = new AppUser();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser token = (AppUser) authentication.getPrincipal();
        user.setId(request.getId());
        user.setUserName(request.getUserName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAddress(request.getAddress());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setEmail(request.getEmail());
        if (request.getPassWord() != null) {
            user.setPassWord(bCryptPasswordEncoder.encode(request.getPassWord()));
        }
        if (token.getRole().equals(Roles.GOVERNOR)) {
            user.setRole(Roles.MINISTER);
        }
        if (token.getRole().equals(Roles.MINISTER)) {
            user.setRole(Roles.CHAIRMAN);
        }
        if (token.getRole().equals(Roles.CHAIRMAN)) {
            user.setRole(Roles.MANAGER);
        }
        if (token.getRole().equals(Roles.MANAGER)) {
            user.setRole(Roles.EMPLOYEE);
        }
        if (token.getRole().equals(Roles.EMPLOYEE)) {
            user.setRole(Roles.CUSTOMER);
        }
        if (request.getAccountsList() != null) {
            user.setAccountsList(request.getAccountsList().stream().map(this::dtoToAccount).toList());
        }
        return user;
    }

    private AppUserDto userToDto(AppUser entity) {
        AppUserDto appUserDto = new AppUserDto();
        appUserDto.setId(entity.getId());
        appUserDto.setUserName(entity.getUserName());
        appUserDto.setPhoneNumber(entity.getPhoneNumber());
        appUserDto.setAddress(entity.getAddress());
        appUserDto.setDateOfBirth(entity.getDateOfBirth());
        appUserDto.setEmail(entity.getEmail());
        appUserDto.setPassWord(entity.getPassWord());
        appUserDto.setRole(entity.getRole());
        if (entity.getAccountsList() != null) {
            appUserDto.setAccountsList(entity.getAccountsList().stream().peek(a -> a.getId()).map(this::accountToDto).toList());
        }
        return appUserDto;
    }

    @Override
    public GenericResponse signUp(AppUserDto request) {
        AppUser appUser = new AppUser();
        if (userRepo.findByEmail(request.getEmail()) != null) {
            return new GenericResponse(HttpStatus.FORBIDDEN.value(), "Email already exists", "failed");
        }
        boolean governorExists = userRepo.findAll().stream().anyMatch(user -> user.getRole() == Roles.GOVERNOR);
        if (governorExists && request.getRole() == Roles.GOVERNOR) {
            return new GenericResponse(HttpStatus.FORBIDDEN.value(), "Governer already exists", "failed");
        } else if (request.getRole() == Roles.MINISTER) {
            return new GenericResponse(HttpStatus.FORBIDDEN.value(), "you dont have a access to set role as Minister", "failed to signup");
        } else if (request.getRole() == Roles.CHAIRMAN) {
            return new GenericResponse(HttpStatus.FORBIDDEN.value(), "you dont have a access to set role as chairman", "failed to signup");
        } else if (request.getRole() == Roles.MANAGER) {
            return new GenericResponse(HttpStatus.FORBIDDEN.value(), "you dont have a access to set role as Manager", "failed to signup");
        } else if (request.getRole() == Roles.EMPLOYEE) {
            return new GenericResponse(HttpStatus.FORBIDDEN.value(), "you dont have a access to set role as Employee", "failed to signup");
        }
        appUser.setUserName(request.getUserName());
        appUser.setEmail(request.getEmail());
        appUser.setRole(request.getRole());
        appUser.setPassWord(bCryptPasswordEncoder.encode(request.getPassWord()));
        appUser.setPhoneNumber(request.getPhoneNumber());
        appUser.setDateOfBirth(request.getDateOfBirth());
        appUser.setAddress(request.getAddress());
        userRepo.save(appUser);
        return new GenericResponse(HttpStatus.OK.value(), "SignUp successfull", "success");
    }

    @Override
    public GenericResponse signIn(AppUserDto request) throws JOSEException {
        AppUser appUser = userRepo.findByEmail(request.getEmail());
        if (appUser == null) {
            return new GenericResponse(HttpStatus.UNAUTHORIZED.value(), "Email not found", "Failed");
        }
        if (bCryptPasswordEncoder.matches(request.getPassWord(), appUser.getPassWord())) {
            SignInResponseDto response = new SignInResponseDto(appUser);
            response.setToken(jwtTokenUtils.getToken(appUser));
            return new GenericResponse(HttpStatus.OK.value(), response);
        } else {
            return new GenericResponse(HttpStatus.UNAUTHORIZED.value(), "password is wrong", "failed");
        }
    }

    @Override
    public GenericResponse saveMinister(AppUserDto request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser token = (AppUser) authentication.getPrincipal();
        AppUser appUser = new AppUser();
        if (token.getRole().equals(Roles.GOVERNOR)) {
            appUser.setUserName(request.getUserName());
            appUser.setPhoneNumber(request.getPhoneNumber());
            appUser.setDateOfBirth(request.getDateOfBirth());
            appUser.setAddress(request.getAddress());
            appUser.setEmail(request.getEmail());
            appUser.setPassWord(bCryptPasswordEncoder.encode(request.getPassWord()));
            appUser.setRole(request.getRole());
            AppUser display = userRepo.save(appUser);
            return new GenericResponse(HttpStatus.OK.value(), "saved successfull", "success", display);
        }
        return new GenericResponse(HttpStatus.FORBIDDEN.value(), "unsuccessful", "failed to save");
    }

    @Override
    public GenericResponse saveChairman(AppUserDto request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser token = (AppUser) authentication.getPrincipal();
        AppUser appUser = new AppUser();
        if (token.getRole().equals(Roles.MINISTER)) {
            appUser.setUserName(request.getUserName());
            appUser.setPhoneNumber(request.getPhoneNumber());
            appUser.setDateOfBirth(request.getDateOfBirth());
            appUser.setAddress(request.getAddress());
            appUser.setEmail(request.getEmail());
            appUser.setPassWord(bCryptPasswordEncoder.encode(request.getPassWord()));
            appUser.setRole(request.getRole());
            AppUser display = userRepo.save(appUser);
            return new GenericResponse(HttpStatus.OK.value(), "saved successfull", "success", display);
        }
        return new GenericResponse(HttpStatus.FORBIDDEN.value(), "unsuccessful", "failed to save");
    }

    @Override
    public GenericResponse saveManager(AppUserDto request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser token = (AppUser) authentication.getPrincipal();
        AppUser appUser = new AppUser();
        if (token.getRole().equals(Roles.CHAIRMAN)) {
            appUser.setUserName(request.getUserName());
            appUser.setPhoneNumber(request.getPhoneNumber());
            appUser.setDateOfBirth(request.getDateOfBirth());
            appUser.setAddress(request.getAddress());
            appUser.setEmail(request.getEmail());
            appUser.setPassWord(bCryptPasswordEncoder.encode(request.getPassWord()));
            appUser.setRole(request.getRole());
            AppUser display = userRepo.save(appUser);
            return new GenericResponse(HttpStatus.OK.value(), "saved successfull", "success", display);
        }
        return new GenericResponse(HttpStatus.FORBIDDEN.value(), "unsuccessful", "failed to save");
    }

    @Override
    public GenericResponse saveEmployee(AppUserDto request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser token = (AppUser) authentication.getPrincipal();
        AppUser appUser = new AppUser();
        if (token.getRole().equals(Roles.MANAGER)) {
            appUser.setUserName(request.getUserName());
            appUser.setPhoneNumber(request.getPhoneNumber());
            appUser.setDateOfBirth(request.getDateOfBirth());
            appUser.setAddress(request.getAddress());
            appUser.setEmail(request.getEmail());
            appUser.setPassWord(bCryptPasswordEncoder.encode(request.getPassWord()));
            appUser.setRole(request.getRole());
            AppUser display = userRepo.save(appUser);
            return new GenericResponse(HttpStatus.OK.value(), "saved successfull", "success", display);
        }
        return new GenericResponse(HttpStatus.FORBIDDEN.value(), "unsuccessful", "failed to save");
    }

    @Override
    public GenericResponse findAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser token = (AppUser) authentication.getPrincipal();
        if (token.getRole().equals(Roles.CUSTOMER)) {
            List<AppUserDto> usersDto = userRepo.findAll().stream().filter(f -> f.getRole().equals(Roles.CUSTOMER)).map(this::userToDto).collect(Collectors.toList());
            return new GenericResponse(HttpStatus.OK.value(), usersDto);
        } else if (token.getRole().equals(Roles.EMPLOYEE)) {
            List<AppUserDto> usersDto = userRepo.findAll().stream().filter(f -> f.getRole().equals(Roles.EMPLOYEE) || f.getRole().equals(Roles.CUSTOMER)).map(this::userToDto).collect(Collectors.toList());
            return new GenericResponse(HttpStatus.OK.value(), usersDto);
        } else if (token.getRole().equals(Roles.MANAGER)) {
            List<AppUserDto> usersDto = userRepo.findAll().stream().filter(f -> f.getRole().equals(Roles.MANAGER) || f.getRole().equals(Roles.EMPLOYEE) || f.getRole().equals(Roles.CUSTOMER)).map(this::userToDto).collect(Collectors.toList());
            return new GenericResponse(HttpStatus.OK.value(), usersDto);
        } else if (token.getRole().equals(Roles.CHAIRMAN)) {
            List<AppUserDto> usersDto = userRepo.findAll().stream().filter(f -> f.getRole().equals(Roles.CHAIRMAN) || f.getRole().equals(Roles.MANAGER) || f.getRole().equals(Roles.EMPLOYEE) || f.getRole().equals(Roles.CUSTOMER)).map(this::userToDto).collect(Collectors.toList());
            return new GenericResponse(HttpStatus.OK.value(), usersDto);
        } else if (token.getRole().equals(Roles.MINISTER)) {
            List<AppUserDto> usersDto = userRepo.findAll().stream().filter(f -> f.getRole().equals(Roles.MINISTER) || f.getRole().equals(Roles.CHAIRMAN) || f.getRole().equals(Roles.MANAGER) || f.getRole().equals(Roles.EMPLOYEE) || f.getRole().equals(Roles.CUSTOMER)).map(this::userToDto).collect(Collectors.toList());
            return new GenericResponse(HttpStatus.OK.value(), usersDto);
        } else {
            List<AppUserDto> usersDto = userRepo.findAll().stream().map(this::userToDto).collect(Collectors.toList());
            return new GenericResponse(HttpStatus.OK.value(), usersDto);
        }
    }

    public GenericResponse saveBank(BankDto request) {
        Bank bank = dtoToBank(request);
        if (request.getBranchList() != null) {
            for (Branch branch : bank.getBranchList()) {
                branch.setBank(bank);
            }
        }
        Bank display = bankRepo.save(bank);
        BankDto bankDto = bankToDto(display);
        return new GenericResponse(HttpStatus.OK.value(), "Bank saved successfully", bankDto);
    }

    public String saveBranch(BranchDto request) {
        Branch branch = dtoToBranch(request);
        if (request.getAccountsList() != null) {
            for (Account account : branch.getAccountsList()) {
                account.setBranch(branch);
            }
        }
        branchRepo.save(branch);
        return "branch saved succesfully";
    }

    public String saveAccount(AccountDto request) {
        Account account = dtoToAccount(request);
        accountRepo.save(account);
        return "Accounts saved Successfully";
    }

    public String saveUser(AppUserDto request) {
        AppUser user = dtoToUser(request);
        if (request.getAccountsList() != null) {
            for (Account account : user.getAccountsList()) {
                account.setAppUser(user);
            }

        }
        userRepo.save(user);
        return "User saved Successfully";
    }

    public Long countOfAUserOfABankByBankId(Long id) {
        return userRepo.countDistinctByAccountsListBranchBankId(id);
    }

    public Double balanceOfABankById(Long id) {
        return accountRepo.findByBranchBankId(id).stream().mapToDouble(Account::getBalance).sum();
    }

    public String matchingAccountIdAndUserIdAndBalanceAndMakeTranscationLimit(Long accountId, Long userId, Double withDrawAmount) {
        long currentTime = System.currentTimeMillis();
        // Check if 24 hours have passed since the last reset
        if (currentTime - lastResetTime >= MILLIS_IN_A_DAY) {
            transcationCount = 0; // Reset the transaction count
            lastResetTime = currentTime; // Update the last reset time
        }
        // Check if maximum transaction limit reached for the day
        if (transcationCount >= MAX_WITHDRAWALS_PER_DAY) {
            return "Maximum transaction limit reached for the day";
        }
        Optional<Account> accountIds = accountRepo.findById(accountId);
        if (accountIds.isPresent()) {
            Account account = accountIds.get();
            if (account.getAppUser().getId() == userId) {

                if (account.getBalance() >= withDrawAmount) {//23000>=21000
                    Double newBalance = account.getBalance() - withDrawAmount;//23000-21000
                    account.setBalance(newBalance);
                    accountRepo.save(account);
                    transcationCount++;
                    return "Withdraw Done Successfully";
                }
                return "Low Balance";
            }
            return "Invalid User ID";
        }
        return "check the account id please";
    }

    private Integer transcationCount = 0;
    private static final int MAX_WITHDRAWALS_PER_DAY = 4;
    private static final long MILLIS_IN_A_DAY = 24 * 60 * 60 * 1000; // 24 hours in milliseconds
    private static long lastResetTime = System.currentTimeMillis();


    public GenericResponse deleteBankById(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser token = (AppUser) authentication.getPrincipal();
        if (token.getRole().equals(Roles.GOVERNOR)) {
            boolean exists = bankRepo.existsById(id);
            if (exists) {
                bankRepo.deleteById(id);
                return new GenericResponse(HttpStatus.OK.value(), "Successfully deleted bank with id " + id, "success");
            } else {
                return new GenericResponse(HttpStatus.NOT_FOUND.value(), "Bank with id " + id + " not found.", "failed");
            }
        } else {
            return new GenericResponse(HttpStatus.UNAUTHORIZED.value(), "Don't have the privilege to delete the bank.");
        }
    }
}

