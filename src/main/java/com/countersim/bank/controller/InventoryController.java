package com.countersim.bank.controller;

import com.countersim.bank.service.InventoryService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/inventory/0055")
    public String inventoryPage(@RequestParam(defaultValue = "cash") String type,
                                @RequestParam(defaultValue = "false") boolean embedded,
                                HttpSession session,
                                Model model) {
        String tellerCode = (String) session.getAttribute("tellerCode");
        if (tellerCode == null) {
            return "redirect:/login";
        }
        model.addAttribute("type", type);
        model.addAttribute("embedded", embedded);
        if ("voucher".equals(type)) {
            inventoryService.queryVoucher(tellerCode).ifPresent(result -> model.addAttribute("result", result));
        } else {
            inventoryService.queryCash(tellerCode).ifPresent(result -> model.addAttribute("result", result));
        }
        return "inventory-0055";
    }
}