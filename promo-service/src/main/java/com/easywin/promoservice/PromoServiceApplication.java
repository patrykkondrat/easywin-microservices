package com.easywin.promoservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class PromoServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PromoServiceApplication.class, args);
    }
}
