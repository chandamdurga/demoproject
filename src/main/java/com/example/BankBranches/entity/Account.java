package com.example.BankBranches.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountNumber;
    private String holderName;
    private LocalDate openedOn;
    private Double balance;
    private String nominee;
    @ManyToOne(cascade = CascadeType.MERGE)
    private Branch branch;
    @ManyToOne(cascade = CascadeType.MERGE)
    private AppUser appUser;
}
