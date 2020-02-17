package com.xnpool.setting.domain.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author  zly
 * @date  2020/2/6 16:05
 * @version 1.0
 */
@Data
public class FeeSetting implements Serializable {
    private static final long serialVersionUID = 8780220522404594124L;
    private Integer id;

    /**
    * 费用单价
    */
    private Double feeprice;

    /**
    * 费用名称
    */
    private String feename;

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