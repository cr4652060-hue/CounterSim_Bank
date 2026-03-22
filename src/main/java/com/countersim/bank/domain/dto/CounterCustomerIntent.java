package com.countersim.bank.domain.dto;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CounterCustomerIntent {
    private String requestType;
    private String requestDisplay;
    private String targetMediumType;
    private String targetMediumNo;
    private String targetAccountNo;
    private String targetDescription;
    private boolean carriesCash;
    private BigDecimal cashAmount;
    private String recommendedTradeCode;
    private String recommendedTradeName;
}