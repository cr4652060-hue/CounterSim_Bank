package com.countersim.bank.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("t_customer_media")
public class CustomerMedia {
    @TableId
    private Long id;
    private String customerKey;
    private String accountCategory;
    private String mediumType;
    private String mediumSubType;
    private String mediumNo;
    private String customerAccountNo;
    private String currency;
    private BigDecimal balanceAmount;
    private LocalDate openDate;
    private LocalDate maturityDate;
    private String termValue;
    private BigDecimal rateValue;
    private String depositType;
    private String autoRenewFlag;
    private String mediaStatus;
    private String voucherNo;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}