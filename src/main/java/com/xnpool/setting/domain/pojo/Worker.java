package com.xnpool.setting.domain.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author  zly
 * @date  2020/2/18 12:02
 * @version 1.0
 */
@Data
public class Worker implements Serializable {
    private static final long serialVersionUID = 29326523138337215L;
    /**
    * 矿机id
    */
    private Integer id;

    /**
    * IP
    */
    private String ip;

    /**
    * 矿工名
    */
    private String miner;

    /**
    * 矿机名称
    */
    private String workername;

    /**
    * 所属厂房
    */
    private String factoryname;

    /**
    * 所属机架
    */
    private String framename;

    /**
    * 所属分组
    */
    private String groupname;

    /**
    * 是否在线
    */
    private Integer isOnline;

    /**
    * 是否入库
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