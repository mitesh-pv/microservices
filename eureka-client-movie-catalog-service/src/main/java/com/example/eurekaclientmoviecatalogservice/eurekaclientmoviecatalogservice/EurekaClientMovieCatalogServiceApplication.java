package com.example.eurekaclientmoviecatalogservice.eurekaclientmoviecatalogservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
public class EurekaClientMovieCatalogServiceApplication {

	@Bean
	@LoadBalanced // gives a hint to discovery server about what service is to be called
	public RestTemplate getRestTemplate(){
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(EurekaClientMovieCatalogServiceApplication.class, args);
	}

}
