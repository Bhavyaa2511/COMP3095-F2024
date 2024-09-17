package ca.gbc.parentmicroservice;

import org.springframework.boot.SpringApplication;

public class TestParentMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.from(ParentMicroserviceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
