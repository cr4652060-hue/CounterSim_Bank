package com.countersim.bank.service;

import com.countersim.bank.domain.dto.CounterCustomerSessionState;
import com.countersim.bank.domain.dto.CounterCustomerView;

public interface CounterCustomerScenarioService {
    CounterCustomerSessionState buildArrivedState(CounterCustomerView customer);
    CounterCustomerSessionState buildInServiceState(CounterCustomerSessionState currentState);
    CounterCustomerSessionState emptyState();
}