package com.xnpool.setting.domain.model;

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
     * 用户id
     */
    private Integer userId;

    /**
     * 分组id
     */
    private Integer groupId;

    /**
     * 所属用户
     */
    private String username;

    /**
     * 矿机id
     */
    private Integer workerId;

    /**
     * 矿工名
     */
    private String miner;

    /**
     * 矿机名
     */
    private String workerName;

    /**
     * 矿机品牌id
     */
    private Integer workerbrandId;

    /**
     * 矿机品牌
     */
    private String brandName;

    /**
     * 矿机ip
     */
    private String ip;

    /**
     * 长整型矿机ip
     */
    private Long ipLong;

    /**
     * 厂房名称
     */
    private String factoryName;

    /**
     * 机架名称
     */
    private String frameName;

    /**
    * 矿机架层数
    */
    private Integer frameNumber;

    /**
     * 组名
     */
    private String groupName;

    /**
    * 是否入库,0:未入库,1:已入库
    */
    private Integer isComeIn;

    /**
     * 在线离线
     */
    private Integer state;

    /**
    * 备注
    */
    private String remarks;

    /**
     * 矿场id
     */
    private Integer mineId;

    /**
     * 所属矿场
     */
    private String mineName;



}