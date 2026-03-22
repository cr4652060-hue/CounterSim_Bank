package com.countersim.bank.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CounterCustomerSessionState {
    private String serviceStatus;
    private CounterCustomerView customer;
    private CounterCustomerIntent intent;
}