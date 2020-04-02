package com.xnpool.setting.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zly
 * @version 1.0
 * @date 2020/3/5 13:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IpSettingExample implements Serializable {

    private static final long serialVersionUID = -9021886554943771078L;
    /**
     * 编号
     */
    private Integer id;

    /**
     * 起始IP
     */
    private String startIp;

    /**
     * 结束IP
     */
    private String endIp;

    /**
     * 矿场id
     */
    private Integer mineId;

    /**
     * 矿场id
     */
    private String mineName;

}