package com.xnpool.setting.domain.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author  zly
 * @date  2020/2/23 12:52
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerDetailedExample implements Serializable {
    private static final long serialVersionUID = -4062422399480937064L;
    /**
    * 编号
    */
    private Integer id;

    /**
     * 矿工名
     */
    private String miner;

    /**
     * 矿机名
     */
    private String workername;

    /**
     * 矿机ip
     */
    private String workerIp;

    /**
     * 厂房名称
     */
    private String factoryname;

    /**
     * 机架名称
     */
    private String framename;

    /**
    * 矿机架层数
    */
    private Integer framenumber;

    /**
     * 组名
     */
    private String groupname;

    /**
    * 是否入库,0:未入库,1:已入库
    */
    private Integer isComeIn;

    /**
     * 在线离线
     */
    private Integer isOnLine;

    /**
    * 备注
    */
    private String remarks;

}