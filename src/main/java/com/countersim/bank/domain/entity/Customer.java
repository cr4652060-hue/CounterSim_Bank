package com.countersim.bank.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("t_customer")
public class Customer {
    @TableId
    private Long id;
    private String customerKey;
    private String customerName;
    private String idType;
    private String idNo;
    private String mobile;
    private String avatarPath;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}