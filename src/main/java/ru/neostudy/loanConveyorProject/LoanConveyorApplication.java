package ru.neostudy.loanConveyorProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;



@SpringBootApplication
@EnableFeignClients
//@EnableSwagger2
public class LoanConveyorApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoanConveyorApplication.class, args);
	}

}
