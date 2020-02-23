package com.xnpool.setting.domain.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author  zly
 * @date  2020/2/23 12:52
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerDetailed implements Serializable {
    private static final long serialVersionUID = -4408951939528442459L;
    /**
    * 编号
    */
    private Integer id;

    /**
    * 矿机ID
    */
    private Integer workerid;

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