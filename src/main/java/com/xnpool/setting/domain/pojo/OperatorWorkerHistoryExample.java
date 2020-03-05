package com.xnpool.setting.domain.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author  zly
 * @date  2020/2/19 11:34
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperatorWorkerHistoryExample implements Serializable {
    private static final long serialVersionUID = 8683833744815963000L;
    /**
    * 编号
    */
    private Integer id;

    /**
     * 所属厂房
     */
    private String factoryName;

    /**
     * 所属详细矿机架
     */
    private String detailed;

    /**
     * 矿工名
     */
    private String miner;

    /**
     * 矿机名
     */
    private String workerName;

    /**
     * 所属分组
     */
    private String groupName;


    /**
    * 出库时间
    */
    private String moveOutTime;

    /**
    * 入库时间
    */
    private String comeInTime;

    /**
     * 总耗时
     */
    private String totalTime;

    /**
    * 出库原因
    */
    private String reason;

    /**
    * 出库员
    */
    private Integer operatorId;
}