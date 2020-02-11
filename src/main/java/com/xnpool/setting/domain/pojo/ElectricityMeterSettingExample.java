package com.xnpool.setting.domain.pojo;

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
    private Integer mineid;

    /**
     * 所属矿场名字
     */
    private String minename;

    /**
     * 所属厂房
     */
    private Integer factoryid;

    /**
     * 所属厂房名字
     */
    private String factoryname;

    /**
     * 所属客户
     */
    private Integer customerid;

    /**
     * 所属客户
     */
    private Integer userid;

    /**
     * 初始码数
     */
    private String electricitystart;

    /**
     * 线损
     */
    private Double line_loss;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 修改时间
     */
    private Date updatetime;
}