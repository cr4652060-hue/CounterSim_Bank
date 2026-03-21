package com.countersim.bank;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.countersim.bank.mapper")
@SpringBootApplication
public class CounterSimBankApplication {

    public static void main(String[] args) {
        SpringApplication.run(CounterSimBankApplication.class, args);
    }
}