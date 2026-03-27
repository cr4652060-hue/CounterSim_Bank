package com.countersim.bank.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.countersim.bank.domain.dto.InventoryQueryResult;
import com.countersim.bank.domain.entity.Teller;
import com.countersim.bank.domain.entity.TellerInventory;
import com.countersim.bank.mapper.TellerInventoryMapper;
import com.countersim.bank.mapper.TellerMapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import com.countersim.bank.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final TellerMapper tellerMapper;
    private final TellerInventoryMapper tellerInventoryMapper;

    @Override
    public Optional<InventoryQueryResult> queryCash(String tellerCode) {
        String normalizedTellerCode = normalizeTellerCode(tellerCode);
        Teller teller = tellerMapper.selectOne(new LambdaQueryWrapper<Teller>()
                .eq(Teller::getTellerCode, normalizedTellerCode)
                .last("limit 1"));
        if (teller == null) {
            return Optional.empty();
        }
        return Optional.of(InventoryQueryResult.builder()
                .tellerCode(normalizedTellerCode)
                .queryType("cash")
                .currentCash(teller.getCurrentCash())
                .authThreshold(teller.getAuthThreshold())
                .overflowThreshold(teller.getOverflowThreshold())
                .build());
    }


    @Override
    public Optional<InventoryQueryResult> queryVoucher(String tellerCode) {
        String normalizedTellerCode = normalizeTellerCode(tellerCode);
        TellerInventory inventory = tellerInventoryMapper.selectOne(new LambdaQueryWrapper<TellerInventory>()
                .eq(TellerInventory::getTellerCode, normalizedTellerCode)
                .eq(TellerInventory::getVoucherType, "ordinary_cd")
                .last("limit 1"));
        if (inventory == null) {
            return Optional.empty();
        }
        return Optional.of(InventoryQueryResult.builder()
                .tellerCode(normalizedTellerCode)
                .queryType("voucher")
                .voucherType("普通版存单")
                .startNo(inventory.getStartNo())
                .endNo(inventory.getEndNo())
                .remainCount(inventory.getRemainCount())
                .build());
    }

    @Override
    public Optional<InventoryQueryResult> queryByType(String tellerCode, String queryType) {
        if ("2".equals(queryType) || "voucher".equalsIgnoreCase(queryType)) {
            return queryVoucher(tellerCode);
        }
        return queryCash(tellerCode);
    }

    @Override
    @Transactional
    public Optional<InventoryQueryResult> applyTradeResult(String tellerCode, String tradeCode, BigDecimal amount, boolean customerAgreed) {
        if (!customerAgreed) {
            return queryCash(tellerCode);
        }
        String normalizedTellerCode = normalizeTellerCode(tellerCode);
        Teller teller = tellerMapper.selectOne(new LambdaQueryWrapper<Teller>()
                .eq(Teller::getTellerCode, normalizedTellerCode)
                .last("limit 1"));
        if (teller == null) {
            return Optional.empty();
        }
        BigDecimal safeAmount = amount == null ? BigDecimal.ZERO : amount.max(BigDecimal.ZERO);
        BigDecimal currentCash = teller.getCurrentCash() == null ? BigDecimal.ZERO : teller.getCurrentCash();
        switch (tradeCode) {
            case "1061":
            case "1651":
                teller.setCurrentCash(currentCash.add(safeAmount));
                break;
            case "1062":
            case "1657":
                teller.setCurrentCash(currentCash.subtract(safeAmount).max(BigDecimal.ZERO));
                break;
            default:
                break;
        }
        teller.setUpdatedAt(LocalDateTime.now());
        tellerMapper.updateById(teller);

        if ("1651".equals(tradeCode)) {
            TellerInventory inventory = tellerInventoryMapper.selectOne(new LambdaQueryWrapper<TellerInventory>()
                    .eq(TellerInventory::getTellerCode, normalizedTellerCode)
                    .eq(TellerInventory::getVoucherType, "ordinary_cd")
                    .last("limit 1"));
            if (inventory != null) {
                int remain = Math.max(0, (inventory.getRemainCount() == null ? 0 : inventory.getRemainCount()) - 1);
                int used = (inventory.getUsedCount() == null ? 0 : inventory.getUsedCount()) + 1;
                inventory.setRemainCount(remain);
                inventory.setUsedCount(used);
                inventory.setUpdatedAt(LocalDateTime.now());
                tellerInventoryMapper.updateById(inventory);
            }
        }
        return queryCash(normalizedTellerCode);
    }

    private String normalizeTellerCode(String tellerCode) {
        String raw = String.valueOf(tellerCode == null ? "" : tellerCode).trim();
        if (raw.length() >= 2) {
            return raw.substring(raw.length() - 2);
        }
        if (raw.isEmpty()) {
            return "01";
        }
        return raw.length() == 1 ? "0" + raw : raw;
    }
}