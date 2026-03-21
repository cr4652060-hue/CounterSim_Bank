package com.countersim.bank.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("t_teller")
public class Teller {
    @TableId
    private Long id;
    private String tellerCode;
    private String tellerName;
    private String avatarPath;
    private BigDecimal currentCash;
    private BigDecimal authThreshold;
    private BigDecimal overflowThreshold;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}