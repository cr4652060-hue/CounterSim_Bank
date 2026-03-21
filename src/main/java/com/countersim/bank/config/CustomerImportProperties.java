package com.countersim.bank.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "counter-sim.import")
public class CustomerImportProperties {

    /**
     * 支持 classpath: / file: / 绝对路径 / 相对路径。
     */
    private String customerFile = "classpath:import/customers.xlsx";
}