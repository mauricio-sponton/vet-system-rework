package com.vet.VetSystemRework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class VetSystemReworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(VetSystemReworkApplication.class, args);
		//System.out.println(new BCryptPasswordEncoder().encode("123"));
	}

}
