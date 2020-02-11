package com.xnpool.setting.domain.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author  zly
 * @date  2020/2/6 14:13
 * @version 1.0
 */
@Data
public class PowerSetting implements Serializable {
    private Integer id;

    /**
    * 电费单价/度
    */
    private Double price;

    /**
    * 描述
    */
    private String description;

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

    private static final long serialVersionUID = 1L;
}