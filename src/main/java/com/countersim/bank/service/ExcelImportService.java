package com.countersim.bank.service;

import java.io.InputStream;

public interface ExcelImportService {

    int importCustomers(InputStream inputStream);

    int importCustomers();
}