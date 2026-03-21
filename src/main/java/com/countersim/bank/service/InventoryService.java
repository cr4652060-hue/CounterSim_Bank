package com.countersim.bank.service;

import com.countersim.bank.domain.dto.InventoryQueryResult;
import java.util.Optional;

public interface InventoryService {
    Optional<InventoryQueryResult> queryCash(String tellerCode);
    Optional<InventoryQueryResult> queryVoucher(String tellerCode);
}