package com.authine.cloudpivot.ext.domain;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 基础数据表
 */
@Data
public class Jc extends BaseDomain {
    private String type;
    private String typeKey;
    private String key;
    private String value;
    private String parentKey;
    private BigDecimal sort;
    /**
     * type                varchar(200)   utf8mb4_general_ci  YES                              select,insert,update,references  类型
     * typeKey             varchar(200)   utf8mb4_general_ci  YES                              select,insert,update,references  类型key
     * key                 varchar(200)   utf8mb4_general_ci  YES                              select,insert,update,references  主键
     * value               varchar(200)   utf8mb4_general_ci  YES                              select,insert,update,references  值
     * sort                decimal(19,6)  (NULL)              YES             (NULL)           select,insert,update,references  排序
     * parentKey           varchar(200)   utf8mb4_general_ci  YES                              select,insert,update,references  父key
     */
}
