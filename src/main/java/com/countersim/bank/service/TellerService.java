package com.countersim.bank.service;

import com.countersim.bank.domain.entity.Teller;
import java.util.List;
import java.util.Optional;

public interface TellerService {
    List<Teller> listActiveTellers();
    Optional<Teller> findByCode(String tellerCode);
}