package com.xnpool.setting.domain.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/4 15:42
 */
@Data
public class FactoryHouseExample implements Serializable {
    private static final long serialVersionUID = 5665965644910905456L;
    /**
     * 主键
     */
    private Integer id;

    /**
     * 厂房编号
     */
    private Integer factoryNum;

    /**
     * 厂房名称
     */
    private String factoryName;

    /**
     * 备注
     */
    private String description;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 所属矿场
     */
    private String mineName;

    /**
     * 所属矿场ID
     */
    private Integer mineId;
}