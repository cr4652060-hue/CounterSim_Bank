package com.countersim.bank.service;

import com.countersim.bank.domain.dto.InventoryQueryResult;
import java.math.BigDecimal;
import java.util.Optional;

public interface InventoryService {
    Optional<InventoryQueryResult> queryCash(String tellerCode);
    Optional<InventoryQueryResult> queryVoucher(String tellerCode);
    Optional<InventoryQueryResult> queryByType(String tellerCode, String queryType);
    Optional<InventoryQueryResult> applyTradeResult(String tellerCode, String tradeCode, BigDecimal amount, boolean customerAgreed);
}