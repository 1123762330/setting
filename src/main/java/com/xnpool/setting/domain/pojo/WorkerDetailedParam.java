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
public class WorkerDetailedParam implements Serializable {
    private static final long serialVersionUID = -370117579611317755L;

    private Integer id;
    /**
     * 矿机ID
     */
    private String workerId;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 矿机品牌id
     */
    private Integer workerbrandId;

    /**
     * 所属厂房
     */
    private Integer factoryId;

    /**
     * 所属机架
     */
    private Integer frameId;

    /**
     * 矿机架层数
     */
    private Integer frameNumber;

    /**
     * 所属分组
     */
    private Integer groupId;

    /**
     * 矿场id
     */
    private Integer mineId;

    /**
     * 是否入库,0:未入库,1:已入库
     */
    private Integer isComeIn;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 是否删除,0:否,1:是
     */
    private Integer isDelete;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 矿机ip
     */
    private String workerIp;

}