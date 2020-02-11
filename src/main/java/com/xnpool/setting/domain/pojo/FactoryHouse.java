package com.xnpool.setting.domain.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/6 13:18
 */
@Data
public class FactoryHouse implements Serializable {
    private static final long serialVersionUID = 7809799891460182920L;
    /**
     * 厂房编号
     */
    private Integer id;

    /**
     * 厂房名称
     */
    private String factoryname;

    /**
     * 备注
     */
    private String description;

    /**
     * 所属矿场
     */
    private Integer mineid;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 修改时间
     */
    private Date updatetime;

    /**
     * 是否删除,0:否,1:是
     */
    private Integer isdelete;
}