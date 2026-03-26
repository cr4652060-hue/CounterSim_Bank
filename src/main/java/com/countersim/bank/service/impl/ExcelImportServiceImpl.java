package com.countersim.bank.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.countersim.bank.config.CustomerImportProperties;
import com.countersim.bank.domain.entity.Customer;
import com.countersim.bank.domain.entity.CustomerMedia;
import com.countersim.bank.mapper.CounterCustomerMapper;
import com.countersim.bank.mapper.CustomerMapper;
import com.countersim.bank.mapper.CustomerMediaMapper;
import com.countersim.bank.service.ExcelImportService;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class ExcelImportServiceImpl implements ExcelImportService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final CustomerMapper customerMapper;
    private final CustomerMediaMapper customerMediaMapper;
    private final CounterCustomerMapper counterCustomerMapper;
    private final CustomerImportProperties customerImportProperties;
    private final ResourceLoader resourceLoader;
    private final DataFormatter dataFormatter = new DataFormatter();

    @Override
    @Transactional
    public int importCustomers() {
        String location = customerImportProperties.getCustomerFile();
        if (!StringUtils.hasText(location)) {
            throw new IllegalStateException("未配置客户导入文件路径，请检查 counter-sim.import.customer-file。");
        }
        try (InputStream inputStream = openInputStream(location)) {
            return importCustomers(inputStream);
        } catch (IOException ex) {
            throw new IllegalStateException("读取客户导入文件失败: " + location, ex);
        }
    }

    @Override
    @Transactional
    public int importCustomers(InputStream inputStream) {
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = sheet.iterator();
            if (iterator.hasNext()) {
                iterator.next();
            }
            clearExistingCustomerData();
            int imported = 0;
            while (iterator.hasNext()) {
                Row row = iterator.next();
                if (isBlank(row.getCell(1))) {
                    continue;
                }
                upsertCustomer(row);
                insertMedia(row);
                imported++;
            }
            return imported;
        } catch (Exception ex) {
            throw new IllegalStateException("导入 Excel 失败: " + ex.getMessage(), ex);
        }
    }


    private void clearExistingCustomerData() {
        counterCustomerMapper.delete(null);
        customerMediaMapper.delete(null);
        customerMapper.delete(null);
    }

    private InputStream openInputStream(String location) throws IOException {
        if (location.startsWith("classpath:") || location.startsWith("file:")) {
            Resource resource = resourceLoader.getResource(location);
            if (!resource.exists()) {
                throw new IOException("文件不存在: " + location);
            }
            return resource.getInputStream();
        }
        Path path = Path.of(location);
        if (!Files.exists(path)) {
            throw new IOException("文件不存在: " + location);
        }
        return Files.newInputStream(path);
    }

    private void upsertCustomer(Row row) {
        String customerKey = text(row.getCell(1));
        Customer existing = customerMapper.selectOne(new LambdaQueryWrapper<Customer>()
                .eq(Customer::getCustomerKey, customerKey)
                .last("limit 1"));
        if (existing == null) {
            existing = new Customer();
            existing.setCustomerKey(customerKey);
            existing.setCreatedAt(LocalDateTime.now());
        }
        existing.setCustomerName(text(row.getCell(2)));
        existing.setIdType(text(row.getCell(3)));
        existing.setIdNo(text(row.getCell(4)));
        existing.setMobile(text(row.getCell(5)));
        existing.setAvatarPath("/images/customer-avatar.png");
        existing.setUpdatedAt(LocalDateTime.now());
        if (existing.getId() == null) {
            customerMapper.insert(existing);
        } else {
            customerMapper.updateById(existing);
        }
    }

    private void insertMedia(Row row) {
        CustomerMedia media = new CustomerMedia();
        media.setCustomerKey(text(row.getCell(1)));
        media.setAccountCategory(text(row.getCell(6)));
        media.setMediumType(text(row.getCell(7)));
        media.setMediumNo(text(row.getCell(8)));
        media.setCustomerAccountNo(text(row.getCell(9)));
        media.setMediumSubType(text(row.getCell(10)));
        media.setCurrency(text(row.getCell(11)));
        media.setBalanceAmount(decimal(row.getCell(12)));
        media.setOpenDate(date(row.getCell(13)));
        media.setMaturityDate(date(row.getCell(14)));
        media.setTermValue(text(row.getCell(15)));
        media.setRateValue(decimal(row.getCell(16)));
        media.setDepositType(text(row.getCell(17)));
        media.setAutoRenewFlag(text(row.getCell(18)));
        media.setMediaStatus(text(row.getCell(19)));
        media.setVoucherNo(text(row.getCell(20)));
        media.setRemark(text(row.getCell(21)));
        media.setCreatedAt(LocalDateTime.now());
        media.setUpdatedAt(LocalDateTime.now());
        customerMediaMapper.insert(media);
    }

    private String text(Cell cell) {
        return cell == null ? "" : dataFormatter.formatCellValue(cell).trim();
    }

    private BigDecimal decimal(Cell cell) {
        String value = text(cell);
        return value.isEmpty() ? BigDecimal.ZERO : new BigDecimal(value.replace(",", ""));
    }

    private LocalDate date(Cell cell) {
        String value = text(cell);
        return value.isEmpty() ? null : LocalDate.parse(value, DATE_FORMATTER);
    }

    private boolean isBlank(Cell cell) {
        return cell == null || cell.getCellType() == CellType.BLANK || text(cell).isEmpty();
    }
}