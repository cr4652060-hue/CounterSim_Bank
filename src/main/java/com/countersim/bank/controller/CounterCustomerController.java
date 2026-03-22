package com.countersim.bank.controller;

import com.countersim.bank.domain.dto.CounterCustomerSessionState;
import com.countersim.bank.domain.dto.CounterCustomerView;
import com.countersim.bank.service.CounterCustomerScenarioService;
import com.countersim.bank.service.CounterCustomerService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class CounterCustomerController {

    public static final String SESSION_STATE_KEY = "counterCustomerSessionState";

    private final CounterCustomerService counterCustomerService;
    private final CounterCustomerScenarioService counterCustomerScenarioService;

    @PostMapping("/counter-customer/arrive")
    public String arrive(HttpSession session) {
        session.setAttribute(SESSION_STATE_KEY, buildArrivedState(session));
        return "redirect:/dashboard";
    }

    @PostMapping("/counter-customer/switch")
    public String change(HttpSession session) {
        session.setAttribute(SESSION_STATE_KEY, buildSwitchedState(session));
        return "redirect:/dashboard";
    }

    @PostMapping("/counter-customer/clear")
    public String clear(HttpSession session) {
        clearCustomerState(session);
        return "redirect:/dashboard";
    }

    @PostMapping("/api/counter-customer/arrive")
    @ResponseBody
    public CounterCustomerSessionState arriveApi(HttpSession session) {
        CounterCustomerSessionState state = buildArrivedState(session);
        session.setAttribute(SESSION_STATE_KEY, state);
        return state;
    }

    @PostMapping("/api/counter-customer/switch")
    @ResponseBody
    public CounterCustomerSessionState switchApi(HttpSession session) {
        CounterCustomerSessionState state = buildSwitchedState(session);
        session.setAttribute(SESSION_STATE_KEY, state);
        return state;
    }

    @PostMapping("/api/counter-customer/start")
    @ResponseBody
    public CounterCustomerSessionState startService(HttpSession session) {
        CounterCustomerSessionState currentState = (CounterCustomerSessionState) session.getAttribute(SESSION_STATE_KEY);
        CounterCustomerSessionState nextState = counterCustomerScenarioService.buildInServiceState(currentState);
        session.setAttribute(SESSION_STATE_KEY, nextState);
        return nextState;
    }

    @PostMapping("/api/counter-customer/clear")
    @ResponseBody
    public CounterCustomerSessionState clearApi(HttpSession session) {
        clearCustomerState(session);
        return counterCustomerScenarioService.emptyState();
    }

    private CounterCustomerSessionState buildArrivedState(HttpSession session) {
        String tellerCode = (String) session.getAttribute("tellerCode");
        CounterCustomerView view = counterCustomerService.arriveRandomCustomer(tellerCode);
        return counterCustomerScenarioService.buildArrivedState(view);
    }

    private CounterCustomerSessionState buildSwitchedState(HttpSession session) {
        String tellerCode = (String) session.getAttribute("tellerCode");
        CounterCustomerView view = counterCustomerService.switchRandomCustomer(tellerCode);
        return counterCustomerScenarioService.buildArrivedState(view);
    }

    private void clearCustomerState(HttpSession session) {
        String tellerCode = (String) session.getAttribute("tellerCode");
        counterCustomerService.clearCurrentCustomer(tellerCode);
        session.setAttribute(SESSION_STATE_KEY, counterCustomerScenarioService.emptyState());
    }
}