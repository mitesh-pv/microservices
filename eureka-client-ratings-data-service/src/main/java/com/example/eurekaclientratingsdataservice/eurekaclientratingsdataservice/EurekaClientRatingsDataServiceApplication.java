package com.example.eurekaclientratingsdataservice.eurekaclientratingsdataservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class EurekaClientRatingsDataServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaClientRatingsDataServiceApplication.class, args);
	}

}
