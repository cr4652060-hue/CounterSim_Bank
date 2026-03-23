package com.countersim.bank.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.countersim.bank.domain.dto.CounterCustomerMediumView;
import com.countersim.bank.domain.dto.CounterCustomerView;
import com.countersim.bank.domain.entity.CounterCustomer;
import com.countersim.bank.domain.entity.Customer;
import com.countersim.bank.domain.entity.CustomerMedia;
import com.countersim.bank.mapper.CounterCustomerMapper;
import com.countersim.bank.mapper.CustomerMapper;
import com.countersim.bank.mapper.CustomerMediaMapper;
import com.countersim.bank.service.CounterCustomerService;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CounterCustomerServiceImpl implements CounterCustomerService {

    private final CounterCustomerMapper counterCustomerMapper;
    private final CustomerMapper customerMapper;
    private final CustomerMediaMapper customerMediaMapper;
    private final Random random = new Random();

    @Override
    public Optional<CounterCustomerView> getCurrentCustomer(String tellerCode) {
        CounterCustomer current = counterCustomerMapper.selectOne(new LambdaQueryWrapper<CounterCustomer>()
                .eq(CounterCustomer::getTellerCode, tellerCode)
                .eq(CounterCustomer::getArriveStatus, "ARRIVED")
                .last("limit 1"));
        if (current == null) {
            return Optional.empty();
        }
        return buildView(current.getCustomerKey());
    }

    @Override
    @Transactional
    public CounterCustomerView arriveRandomCustomer(String tellerCode) {
        return assignRandomCustomer(tellerCode, null);
    }

    @Override
    @Transactional
    public CounterCustomerView switchRandomCustomer(String tellerCode) {
        String excludeKey = getCurrentCustomer(tellerCode).map(CounterCustomerView::getCustomerKey).orElse(null);
        return assignRandomCustomer(tellerCode, excludeKey);
    }

    @Override
    @Transactional
    public void clearCurrentCustomer(String tellerCode) {
        counterCustomerMapper.delete(new LambdaQueryWrapper<CounterCustomer>()
                .eq(CounterCustomer::getTellerCode, tellerCode));
    }

    private CounterCustomerView assignRandomCustomer(String tellerCode, String excludeKey) {
        List<Customer> customers = customerMapper.selectList(new LambdaQueryWrapper<Customer>()
                .orderByAsc(Customer::getCustomerKey));
        List<Customer> filtered = customers.stream()
                .filter(customer -> excludeKey == null || !excludeKey.equals(customer.getCustomerKey()))
                .toList();
        if (filtered.isEmpty()) {
            throw new IllegalStateException("客户池为空，请先导入客户 Excel 数据。");
        }
        Customer picked = filtered.get(random.nextInt(filtered.size()));
        counterCustomerMapper.delete(new LambdaQueryWrapper<CounterCustomer>()
                .eq(CounterCustomer::getTellerCode, tellerCode));

        CounterCustomer counterCustomer = new CounterCustomer();
        counterCustomer.setTellerCode(tellerCode);
        counterCustomer.setCustomerKey(picked.getCustomerKey());
        counterCustomer.setArriveStatus("ARRIVED");
        counterCustomer.setArriveTime(LocalDateTime.now());
        counterCustomer.setUpdatedAt(LocalDateTime.now());
        counterCustomerMapper.insert(counterCustomer);
        return buildView(picked.getCustomerKey()).orElseThrow();
    }

    private Optional<CounterCustomerView> buildView(String customerKey) {
        Customer customer = customerMapper.selectOne(new LambdaQueryWrapper<Customer>()
                .eq(Customer::getCustomerKey, customerKey)
                .last("limit 1"));
        if (customer == null) {
            return Optional.empty();
        }
        List<CustomerMedia> mediaList = customerMediaMapper.selectList(new LambdaQueryWrapper<CustomerMedia>()
                .eq(CustomerMedia::getCustomerKey, customerKey)
                .orderByAsc(CustomerMedia::getMediumType, CustomerMedia::getMediumNo));
        Set<String> accountTypes = mediaList.stream().map(CustomerMedia::getAccountCategory).collect(Collectors.toSet());
        List<String> labels = mediaList.stream()
                .sorted(Comparator.comparing(CustomerMedia::getMediumType).thenComparing(CustomerMedia::getMediumNo))
                .map(item -> item.getMediumType() + " / " + item.getMediumNo())
                .toList();
        List<CounterCustomerMediumView> detailList = mediaList.stream()
                .sorted(Comparator.comparing(CustomerMedia::getMediumType).thenComparing(CustomerMedia::getMediumNo))
                .map(item -> CounterCustomerMediumView.builder()
                        .accountCategory(item.getAccountCategory())
                        .mediumType(item.getMediumType())
                        .mediumSubType(item.getMediumSubType())
                        .mediumNo(item.getMediumNo())
                        .customerAccountNo(item.getCustomerAccountNo())
                        .voucherNo(item.getVoucherNo())
                        .currency(item.getCurrency())
                        .balanceAmount(item.getBalanceAmount())
                        .depositType(item.getDepositType())
                        .mediaStatus(item.getMediaStatus())
                        .build())
                .toList();
        return Optional.of(CounterCustomerView.builder()
                .arrived(true)
                .customerKey(customer.getCustomerKey())
                .customerName(customer.getCustomerName())
                .mobile(customer.getMobile())
                .mediumCount(mediaList.size())
                .accountTypeCount(accountTypes.size())
                .idType(customer.getIdType())
                .idNo(customer.getIdNo())
                .avatarPath(customer.getAvatarPath())
                .mediaLabels(labels)
                .mediaDetails(detailList)
                .build());
    }
}