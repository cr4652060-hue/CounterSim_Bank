package com.countersim.bank.domain.dto;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InventoryQueryResult {
    private String tellerCode;
    private String queryType;
    private BigDecimal currentCash;
    private BigDecimal authThreshold;
    private BigDecimal overflowThreshold;
    private String voucherType;
    private String startNo;
    private String endNo;
    private Integer remainCount;
}