package com.xnpool.setting.domain.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/11 10:36
 */
@Data
public class ElectricityMeterSettingExample implements Serializable {

    private static final long serialVersionUID = -8528374259367050524L;
    private Integer id;

    /**
     * 电表名称
     */
    private String electricityMeterName;

    /**
     * 所属矿场
     */
    private Integer mineId;

    /**
     * 所属矿场名字
     */
    private String mineName;

    /**
     * 所属厂房
     */
    private Integer factoryId;

    /**
     * 所属厂房名字
     */
    private String factoryName;

    /**
     * 所属客户
     */
    private Integer customerId;

    /**
     * 所属客户姓名
     */
    private String userName;

    /**
     * 初始码数
     */
    private String electricityStart;

    /**
     * 线损
     */
    private Double lineLoss;
}