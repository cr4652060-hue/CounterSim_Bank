package com.countersim.bank.domain.dto;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CounterCustomerMediumView {
    private String accountCategory;
    private String mediumType;
    private String mediumSubType;
    private String mediumNo;
    private String customerAccountNo;
    private String currency;
    private BigDecimal balanceAmount;
    private String depositType;
    private String mediaStatus;
}