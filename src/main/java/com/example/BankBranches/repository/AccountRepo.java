package com.example.BankBranches.repository;

import com.example.BankBranches.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepo extends JpaRepository<Account,Long> {
    List<Account> findByBranchBankId(Long id);
}
