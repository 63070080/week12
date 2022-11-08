package com.example.lab12discoveryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class Lab12DiscoveryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(Lab12DiscoveryServiceApplication.class, args);
	}

}
