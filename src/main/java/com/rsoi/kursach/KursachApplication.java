package com.rsoi.kursach;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class KursachApplication {

    public KursachApplication() {
    }
    
    public static void main(String[] args) {
		SpringApplication.run(KursachApplication.class, args);
	}
        
      
}
