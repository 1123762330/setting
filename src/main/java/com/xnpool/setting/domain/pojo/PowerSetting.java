package com.xnpool.setting.domain.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zly
 * @version 1.0
 * @date 2020/4/6 16:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PowerSetting implements Serializable {
    private static final long serialVersionUID = -1863983510653263844L;
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