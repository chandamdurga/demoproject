package com.example.BankBranches.dto;

import com.example.BankBranches.entity.Branch;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
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
public class BankDto {
    private Long id;
    private String bankName;
    private String headQuarters;
    private LocalDate establishedOn;
    private String founder;
    List<BranchDto> branchList;

}
