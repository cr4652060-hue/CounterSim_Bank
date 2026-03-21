package com.countersim.bank.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.countersim.bank.domain.entity.Teller;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TellerMapper extends BaseMapper<Teller> {
}