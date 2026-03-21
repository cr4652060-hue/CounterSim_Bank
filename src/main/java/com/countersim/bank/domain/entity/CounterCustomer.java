package com.countersim.bank.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("t_counter_customer")
public class CounterCustomer {
    @TableId
    private Long id;
    private String tellerCode;
    private String customerKey;
    private String arriveStatus;
    private LocalDateTime arriveTime;
    private LocalDateTime updatedAt;
}