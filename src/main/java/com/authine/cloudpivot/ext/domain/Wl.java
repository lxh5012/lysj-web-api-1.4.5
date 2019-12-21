package com.authine.cloudpivot.ext.domain;

import lombok.Data;

/**
 * 外链信息表
 */
@Data
public class Wl extends BaseDomain {
    private String gxbBm;
    private String gxbUrl;
    private String gxbMc;
    private String gxbLx;
    private String gxbLxXz;
    /**
     * gxbBm               varchar(200)   编码
     * gxbUrl              mediumtext     url
     * gxbMc               varchar(200)   名称
     * gxbLx               varchar(200)   类型
     * gxbLxXz             varchar(200)   类型选择
     */
}
