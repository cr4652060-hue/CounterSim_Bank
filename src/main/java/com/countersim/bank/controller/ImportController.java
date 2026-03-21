package com.countersim.bank.controller;

import com.countersim.bank.service.ExcelImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class ImportController {

    private final ExcelImportService excelImportService;

    @GetMapping("/import/customers")
    public String importPage() {
        return "import-customers";
    }

    @PostMapping("/import/customers")
    public String importCustomers(MultipartFile file, Model model) {
        try {
            int count = excelImportService.importCustomers(file.getInputStream());
            model.addAttribute("message", "成功导入 " + count + " 条客户介质记录。");
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
        }
        return "import-customers";
    }
}