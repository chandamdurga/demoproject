package com.example.BankBranches;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication

/*@OpenAPIDefinition(
		info = @Info(
				title = "GLUT Swagger Test",
				version = "2.0.0",
				description = "This is for Learning Only!!",
				termsOfService = "dastagp",
				contact = @Contact(
						name = "Girija Bheemavaram",
						email = "girija@gmail.com"
				),
				license = @License(
						name = "Girija nil",
						url = "www.girijanil.com"
				)
		)
)*/

public class BankBranchesApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankBranchesApplication.class, args);
	}
	/*public static void main(String[] args) {
		String password = "Durga@123";

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encryptedPassword = encoder.encode(password);

		System.out.println("Original Password: " + password);
		System.out.println("Encrypted Password: " + encryptedPassword);
	}*/


	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder(){
		return new BCryptPasswordEncoder();
	}

}
