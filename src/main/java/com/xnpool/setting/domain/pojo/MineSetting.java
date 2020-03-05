package com.xnpool.setting.domain.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zly
 * @version 1.0
 * @date 2020/3/5 12:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MineSetting implements Serializable {
    private static final long serialVersionUID = -7977025821251310600L;
    /**
     * 矿场编号
     */
    private Integer id;

    /**
     * 矿场名称
     */
    private String mineName;

    /**
     * 备注
     */
    private String description;

    /**
     * 是否删除，0:否，1:是
     */
    private Integer isDelete;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 生成时间
     */
    private Date createTime;

}