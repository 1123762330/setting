package com.xnpool.setting.domain.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zly
 * @version 1.0
 * @date 2020/3/5 20:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElectricityMeterSetting implements Serializable {
    private static final long serialVersionUID = -5275399669116759361L;
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
     * 所属厂房
     */
    private Integer factoryId;

    /**
     * 所属客户
     */
    private Integer customerId;

    /**
     * 初始码数
     */
    private String electricityStart;

    /**
     * 线损
     */
    private Double lineLoss;

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

}