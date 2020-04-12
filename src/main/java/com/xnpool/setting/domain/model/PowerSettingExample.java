package com.xnpool.setting.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zly
 * @version 1.0
 * @date 2020/4/12 12:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PowerSettingExample implements Serializable {
    private static final long serialVersionUID = 3358273796109874387L;
    private Integer id;

    /**
     * 矿场id
     */
    private Integer mineId;

    /**
     * 所属矿场
     */
    private String mineName;

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
}