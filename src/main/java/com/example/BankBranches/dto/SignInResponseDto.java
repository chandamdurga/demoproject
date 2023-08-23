package com.example.BankBranches.dto;

import com.example.BankBranches.entity.AppUser;
import com.example.BankBranches.enums.Roles;
import lombok.Data;
import org.springframework.boot.SpringApplication;
@Data
public class SignInResponseDto {
    private Long id;
    private String email;
    private Roles roleType;
    private String name;
    private String token;

    public SignInResponseDto(AppUser appUser) {
        this.id = appUser.getId();
        this.email = appUser.getEmail();
        this.roleType = appUser.getRole();
        this.name = appUser.getUserName();
    }
    public SignInResponseDto(){
        super();
    }
}
