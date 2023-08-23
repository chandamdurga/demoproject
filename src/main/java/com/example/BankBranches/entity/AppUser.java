package com.example.BankBranches.entity;
import com.example.BankBranches.enums.Roles;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
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
@Table(name = "app_user")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String address;
    @Column(unique = true)
    private String email;
    private String passWord;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "appUser")
    private List<Account> accountsList;
    private Roles role;
}
