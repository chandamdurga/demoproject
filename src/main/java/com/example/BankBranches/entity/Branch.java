package com.example.BankBranches.entity;

import com.example.BankBranches.dto.BankDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String branchId;
    private String branchName;
    private LocalDate establishedOn;
    private String address;
    @ManyToOne(cascade = CascadeType.MERGE,fetch = FetchType.LAZY)
    private Bank bank;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "branch")
    private List<Account> accountsList;
}
