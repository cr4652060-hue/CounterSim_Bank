package com.countersim.bank.domain.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CounterCustomerView {
    private boolean arrived;
    private String customerKey;
    private String customerName;
    private String mobile;
    private int mediumCount;
    private int accountTypeCount;
    private String idType;
    private String idNo;
    private String avatarPath;
    private List<String> mediaLabels;
    private List<CounterCustomerMediumView> mediaDetails;
}