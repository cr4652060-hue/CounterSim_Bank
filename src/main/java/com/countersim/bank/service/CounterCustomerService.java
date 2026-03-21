package com.countersim.bank.service;

import com.countersim.bank.domain.dto.CounterCustomerView;
import java.util.Optional;

public interface CounterCustomerService {
    Optional<CounterCustomerView> getCurrentCustomer(String tellerCode);
    CounterCustomerView arriveRandomCustomer(String tellerCode);
    CounterCustomerView switchRandomCustomer(String tellerCode);
    void clearCurrentCustomer(String tellerCode);
}