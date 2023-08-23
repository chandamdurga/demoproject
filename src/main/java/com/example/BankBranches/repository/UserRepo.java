package com.example.BankBranches.repository;

import com.example.BankBranches.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<AppUser, Long> {
    Long countDistinctByAccountsListBranchBankId(Long id); //find count of a user of a bank by bank id

    AppUser findByEmail(String username);
    AppUser findByRole(String role);
}
