package com.countersim.bank.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("t_teller_inventory")
public class TellerInventory {
    @TableId
    private Long id;
    private String tellerCode;
    private String voucherType;
    private String startNo;
    private String endNo;
    private Integer totalCount;
    private Integer remainCount;
    private Integer usedCount;
    private LocalDateTime updatedAt;
}