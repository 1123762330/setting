package com.xnpool.setting.domain.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zly
 * @version 1.0
 * @date 2020/4/12 12:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PowerSetting implements Serializable {
    private static final long serialVersionUID = 3358273796109874387L;
    private Integer id;

    /**
     * 矿场id
     */
    private Integer mineId;

    /**
     * 电费单价/度
     */
    private Double price;

    /**
     * 电费成本价/度
     */
    private Double basePrice;

    /**
     * 描述
     */
    private String description;

    /**
     * 是否删除,0:否,1:是
     */
    private Integer isDelete;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 企业id
     */
    private Long tenantId;
}