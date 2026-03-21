package com.countersim.bank.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_sys_config")
public class SysConfig {
    @TableId
    private Long id;
    private String configKey;
    private String configValue;
    private String configDesc;
}