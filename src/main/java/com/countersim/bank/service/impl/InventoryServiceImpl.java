package com.countersim.bank.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.countersim.bank.domain.dto.InventoryQueryResult;
import com.countersim.bank.domain.entity.Teller;
import com.countersim.bank.domain.entity.TellerInventory;
import com.countersim.bank.mapper.TellerInventoryMapper;
import com.countersim.bank.mapper.TellerMapper;
import com.countersim.bank.service.InventoryService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final TellerMapper tellerMapper;
    private final TellerInventoryMapper tellerInventoryMapper;

    @Override
    public Optional<InventoryQueryResult> queryCash(String tellerCode) {
        Teller teller = tellerMapper.selectOne(new LambdaQueryWrapper<Teller>()
                .eq(Teller::getTellerCode, tellerCode)
                .last("limit 1"));
        if (teller == null) {
            return Optional.empty();
        }
        return Optional.of(InventoryQueryResult.builder()
                .tellerCode(tellerCode)
                .queryType("cash")
                .currentCash(teller.getCurrentCash())
                .authThreshold(teller.getAuthThreshold())
                .overflowThreshold(teller.getOverflowThreshold())
                .build());
    }

    @Override
    public Optional<InventoryQueryResult> queryVoucher(String tellerCode) {
        TellerInventory inventory = tellerInventoryMapper.selectOne(new LambdaQueryWrapper<TellerInventory>()
                .eq(TellerInventory::getTellerCode, tellerCode)
                .eq(TellerInventory::getVoucherType, "ordinary_cd")
                .last("limit 1"));
        if (inventory == null) {
            return Optional.empty();
        }
        return Optional.of(InventoryQueryResult.builder()
                .tellerCode(tellerCode)
                .queryType("voucher")
                .voucherType("普通版存单")
                .startNo(inventory.getStartNo())
                .endNo(inventory.getEndNo())
                .remainCount(inventory.getRemainCount())
                .build());
    }
}