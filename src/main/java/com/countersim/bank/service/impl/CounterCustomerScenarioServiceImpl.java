package com.countersim.bank.service.impl;

import com.countersim.bank.domain.dto.CounterCustomerIntent;
import com.countersim.bank.domain.dto.CounterCustomerMediumView;
import com.countersim.bank.domain.dto.CounterCustomerSessionState;
import com.countersim.bank.domain.dto.CounterCustomerView;
import com.countersim.bank.service.CounterCustomerScenarioService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Service;

@Service
public class CounterCustomerScenarioServiceImpl implements CounterCustomerScenarioService {

    private final Random random = new Random();

    @Override
    public CounterCustomerSessionState buildArrivedState(CounterCustomerView customer) {
        return CounterCustomerSessionState.builder()
                .serviceStatus("ARRIVED")
                .customer(customer)
                .intent(generateIntent(customer))
                .build();
    }

    @Override
    public CounterCustomerSessionState buildInServiceState(CounterCustomerSessionState currentState) {
        if (currentState == null || currentState.getCustomer() == null) {
            return emptyState();
        }
        return CounterCustomerSessionState.builder()
                .serviceStatus("IN_SERVICE")
                .customer(currentState.getCustomer())
                .intent(currentState.getIntent() == null ? generateIntent(currentState.getCustomer()) : currentState.getIntent())
                .build();
    }

    @Override
    public CounterCustomerSessionState emptyState() {
        return CounterCustomerSessionState.builder()
                .serviceStatus("NONE")
                .customer(null)
                .intent(null)
                .build();
    }

    private CounterCustomerIntent generateIntent(CounterCustomerView customer) {
        List<String> available = new ArrayList<>(List.of("DEPOSIT", "WITHDRAW", "OPEN_CD"));
        if (customer.getMediaDetails() != null && customer.getMediaDetails().stream().anyMatch(item -> "存单".equals(item.getMediumType()))) {
            available.add("WITHDRAW_CD");
        }
        String picked = available.get(random.nextInt(available.size()));
        return switch (picked) {
            case "DEPOSIT" -> buildDepositIntent(customer);
            case "WITHDRAW" -> buildWithdrawIntent(customer);
            case "WITHDRAW_CD" -> buildWithdrawCdIntent(customer);
            default -> buildOpenCdIntent();
        };
    }

    private CounterCustomerIntent buildDepositIntent(CounterCustomerView customer) {
        CounterCustomerMediumView target = pickSettlementMedium(customer);
        BigDecimal amount = randomCash(500, 50000);
        return CounterCustomerIntent.builder()
                .requestType("存钱")
                .requestDisplay("客户诉求：存钱")
                .targetMediumType(target != null ? target.getMediumType() : "借记卡")
                .targetMediumNo(target != null ? target.getMediumNo() : "待柜员指定")
                .targetAccountNo(target != null ? target.getCustomerAccountNo() : null)
                .targetDescription(target == null ? "目标介质：借记卡（待指定）" : "目标介质：" + target.getMediumType() + " " + target.getMediumNo())
                .carriesCash(true)
                .cashAmount(amount)
                .recommendedTradeCode("1061")
                .recommendedTradeName("个人活期存款续存")
                .build();
    }

    private CounterCustomerIntent buildWithdrawIntent(CounterCustomerView customer) {
        CounterCustomerMediumView target = pickSettlementMedium(customer);
        BigDecimal amount = deriveWithdrawAmount(target);
        return CounterCustomerIntent.builder()
                .requestType("取钱")
                .requestDisplay("客户诉求：支取 " + amount.setScale(2, RoundingMode.HALF_UP) + " 元")
                .targetMediumType(target != null ? target.getMediumType() : "借记卡")
                .targetMediumNo(target != null ? target.getMediumNo() : "待柜员指定")
                .targetAccountNo(target != null ? target.getCustomerAccountNo() : null)
                .targetDescription(target == null ? "目标账户：借记卡（待指定）" : "目标账户：" + target.getMediumType() + " " + target.getMediumNo())
                .carriesCash(false)
                .cashAmount(null)
                .recommendedTradeCode("1062")
                .recommendedTradeName("个人活期存款现金支取")
                .build();
    }

    private CounterCustomerIntent buildOpenCdIntent() {
        BigDecimal amount = randomCash(1000, 100000);
        return CounterCustomerIntent.builder()
                .requestType("开立存单")
                .requestDisplay("客户诉求：开立存单")
                .targetMediumType("存单")
                .targetMediumNo("新开存单")
                .targetDescription("目标介质：新开存单")
                .carriesCash(true)
                .cashAmount(amount)
                .recommendedTradeCode("1651")
                .recommendedTradeName("个人定期存款开户")
                .build();
    }

    private CounterCustomerIntent buildWithdrawCdIntent(CounterCustomerView customer) {
        CounterCustomerMediumView certificate = customer.getMediaDetails().stream()
                .filter(item -> "存单".equals(item.getMediumType()))
                .findFirst()
                .orElse(null);
        return CounterCustomerIntent.builder()
                .requestType("取存单")
                .requestDisplay("客户诉求：取存单")
                .targetMediumType(certificate != null ? certificate.getMediumType() : "存单")
                .targetMediumNo(certificate != null ? certificate.getMediumNo() : "待指定")
                .targetAccountNo(certificate != null ? certificate.getCustomerAccountNo() : null)
                .targetDescription(certificate == null ? "目标存单：待指定" : "目标存单：" + certificate.getMediumNo())
                .carriesCash(false)
                .cashAmount(null)
                .recommendedTradeCode("1657")
                .recommendedTradeName("个人定期存款支取")
                .build();
    }

    private CounterCustomerMediumView pickSettlementMedium(CounterCustomerView customer) {
        if (customer.getMediaDetails() == null || customer.getMediaDetails().isEmpty()) {
            return null;
        }
        return customer.getMediaDetails().stream()
                .filter(item -> "借记卡".equals(item.getMediumType()) || "存折".equals(item.getMediumType()))
                .findFirst()
                .orElse(customer.getMediaDetails().get(0));
    }

    private BigDecimal randomCash(int min, int max) {
        int steps = (max - min) / 100;
        int amount = min + random.nextInt(Math.max(steps, 1) + 1) * 100;
        return BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal deriveWithdrawAmount(CounterCustomerMediumView target) {
        if (target == null || target.getBalanceAmount() == null || target.getBalanceAmount().compareTo(BigDecimal.valueOf(100)) < 0) {
            return BigDecimal.valueOf(500).setScale(2, RoundingMode.HALF_UP);
        }
        BigDecimal cap = target.getBalanceAmount().min(BigDecimal.valueOf(20000));
        int min = 100;
        int max = cap.intValue();
        if (max < min) {
            max = min;
        }
        return randomCash(min, max - (max % 100));
    }
}