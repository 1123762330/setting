package com.xnpool.setting.domain.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author  zly
 * @date  2020/2/6 13:22
 * @version 1.0
 */
@Data
public class WorkerbrandSetting implements Serializable {
    private static final long serialVersionUID = -4081282291830619683L;
    /**
    * 编号
    */
    private Integer id;

    /**
    * 矿机名
    */
    private String workername;

    /**
    * 矿机类型
    */
    private String workertype;

    /**
    * 制造商
    */
    private String business;

    /**
    * 机器尺寸
    */
    private String workersize;

    /**
    * 功耗
    */
    private String powerwaste;

    /**
    * 额定算力
    */
    private String difficulty;

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