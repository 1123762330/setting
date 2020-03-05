package com.xnpool.setting.domain.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zly
 * @version 1.0
 * @date 2020/3/5 12:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FactoryHouse implements Serializable {
    /**
     * 厂房编号
     */
    private Integer id;

    /**
     * 厂房名称
     */
    private String factoryName;

    /**
     * 备注
     */
    private String description;

    /**
     * 所属矿场
     */
    private Integer mineId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 是否删除,0:否,1:是
     */
    private Integer isDelete;

    private static final long serialVersionUID = 1L;
}