package com.countersim.bank.domain.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class TradeSettleRequest {
    private String tradeCode;
    private BigDecimal amount;
    private Boolean customerAgreed;
}