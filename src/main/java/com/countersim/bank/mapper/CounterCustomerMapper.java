package com.countersim.bank.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.countersim.bank.domain.entity.CounterCustomer;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CounterCustomerMapper extends BaseMapper<CounterCustomer> {
}