package com.countersim.bank.controller;

import com.countersim.bank.domain.dto.LoginForm;
import com.countersim.bank.service.TellerService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final TellerService tellerService;

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        model.addAttribute("tellers", tellerService.listActiveTellers());
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@Valid @ModelAttribute LoginForm loginForm,
                          BindingResult bindingResult,
                          HttpSession session,
                          Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("tellers", tellerService.listActiveTellers());
            return "login";
        }
        return tellerService.findByCode(loginForm.getTellerCode())
                .map(teller -> {
                    session.setAttribute("tellerCode", teller.getTellerCode());
                    return "redirect:/dashboard";
                })
                .orElseGet(() -> {
                    model.addAttribute("tellers", tellerService.listActiveTellers());
                    model.addAttribute("error", "未找到对应柜员，请选择 01-20 号柜员。");
                    return "login";
                });
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}