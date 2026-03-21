package com.countersim.bank;

import com.countersim.bank.config.CustomerImportProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@MapperScan("com.countersim.bank.mapper")
@SpringBootApplication
@EnableConfigurationProperties(CustomerImportProperties.class)
public class CounterSimBankApplication {

    public static void main(String[] args) {
        SpringApplication.run(CounterSimBankApplication.class, args);
    }
}