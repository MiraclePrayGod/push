package com.example.mspago;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.example.mspago.feign")
public class MsPagoApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsPagoApplication.class, args);
    }
}