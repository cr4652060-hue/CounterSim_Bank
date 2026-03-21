package com.countersim.bank.controller;

import com.countersim.bank.service.CounterCustomerService;
import com.countersim.bank.service.TellerService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final TellerService tellerService;
    private final CounterCustomerService counterCustomerService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        String tellerCode = (String) session.getAttribute("tellerCode");
        if (tellerCode == null) {
            return "redirect:/login";
        }
        tellerService.findByCode(tellerCode).ifPresent(teller -> model.addAttribute("teller", teller));
        counterCustomerService.getCurrentCustomer(tellerCode).ifPresent(view -> model.addAttribute("counterCustomer", view));
        return "dashboard";
    }
}