package com.countersim.bank.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.countersim.bank.domain.entity.Teller;
import com.countersim.bank.mapper.TellerMapper;
import com.countersim.bank.service.TellerService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TellerServiceImpl implements TellerService {

    private final TellerMapper tellerMapper;

    @Override
    public List<Teller> listActiveTellers() {
        return tellerMapper.selectList(new LambdaQueryWrapper<Teller>()
                .eq(Teller::getStatus, "ACTIVE")
                .orderByAsc(Teller::getTellerCode));
    }

    @Override
    public Optional<Teller> findByCode(String tellerCode) {
        return Optional.ofNullable(tellerMapper.selectOne(new LambdaQueryWrapper<Teller>()
                .eq(Teller::getTellerCode, tellerCode)
                .last("limit 1")));
    }
}