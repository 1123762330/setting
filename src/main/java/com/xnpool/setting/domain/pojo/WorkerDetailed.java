package com.xnpool.setting.domain.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zly
 * @version 1.0
 * @date 2020/3/2 16:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerDetailed implements Serializable {
    /**
     * 编号
     */
    private Integer id;

    /**
     * 矿机ID
     */
    private Integer workerid;

    /**
     * 矿机ip
     */
    private String workerIp="";

    /**
     * 矿机品牌id
     */
    private Integer workerbrandId;

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
     * 矿场id
     */
    private Integer mineid;

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

    private static final long serialVersionUID = 1L;
}