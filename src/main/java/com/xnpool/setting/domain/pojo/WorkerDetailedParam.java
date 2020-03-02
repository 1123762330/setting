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

    /**
    * 矿机ID
    */
    private String workerid;

    /**
     * 矿机IP
     */
    private String workerIp;

    /**
    * 所属厂房
    */
    private Integer factoryid;

    /**
    * 所属机架
    */
    private Integer frameid;

    /**
    * 矿机架层数
    */
    private Integer framenumber;

    /**
    * 所属分组
    */
    private Integer groupid;

    /**
     * 所属矿场
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
    private Integer isdelete;

    /**
    * 创建时间
    */
    private Date createtime;

    /**
    * 更新时间
    */
    private Date updatetime;

}