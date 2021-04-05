package com.swida.documetation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class DocumetationApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocumetationApplication.class, args);
	}

}
