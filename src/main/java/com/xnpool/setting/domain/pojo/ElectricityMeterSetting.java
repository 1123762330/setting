package com.xnpool.setting.domain.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/11 10:36
 */
@Data
public class ElectricityMeterSetting implements Serializable {
    private static final long serialVersionUID = 6771745017499158125L;
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
     * 所属厂房
     */
    private Integer factoryid;

    /**
     * 所属客户
     */
    private Integer customerid;

    /**
     * 初始码数
     */
    private String electricitystart;

    /**
     * 线损
     */
    private Double lineLoss;

    /**
     * 是否删除,0:否,1:是
     */
    private Integer isdelete;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 修改时间
     */
    private Date updatetime;
}