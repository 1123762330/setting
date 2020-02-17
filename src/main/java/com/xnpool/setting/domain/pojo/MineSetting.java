package com.xnpool.setting.domain.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/10 14:49
 */
@Data
public class MineSetting implements Serializable {
    private static final long serialVersionUID = -3170853676349397757L;
    /**
     * 矿场编号
     */
    private Integer id;

    /**
     * 矿场名称
     */
    private String minename;

    /**
     * 备注
     */
    private String description;

    /**
     * 是否删除，0:否，1:是
     */
    private Integer isdelete;

    /**
     * 修改时间
     */
    private Date updatetime;

    /**
     * 生成时间
     */
    private Date createtime;

}