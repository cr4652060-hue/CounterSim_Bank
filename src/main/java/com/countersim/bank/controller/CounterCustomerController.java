package com.countersim.bank.controller;

import com.countersim.bank.service.CounterCustomerService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class CounterCustomerController {

    private final CounterCustomerService counterCustomerService;

    @PostMapping("/counter-customer/arrive")
    public String arrive(HttpSession session) {
        counterCustomerService.arriveRandomCustomer((String) session.getAttribute("tellerCode"));
        return "redirect:/dashboard";
    }

    @PostMapping("/counter-customer/switch")
    public String change(HttpSession session) {
        counterCustomerService.switchRandomCustomer((String) session.getAttribute("tellerCode"));
        return "redirect:/dashboard";
    }

    @PostMapping("/counter-customer/clear")
    public String clear(HttpSession session) {
        counterCustomerService.clearCurrentCustomer((String) session.getAttribute("tellerCode"));
        return "redirect:/dashboard";
    }
}