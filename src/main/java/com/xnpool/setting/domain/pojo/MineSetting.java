package com.xnpool.setting.domain.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zly
 * @version 1.0
 * @date 2020/4/12 17:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MineSetting implements Serializable {
    private static final long serialVersionUID = 7462120523210376937L;
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
     * 该矿场下的机架层数
     */
    private Integer frameNum;

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

    /**
     * 企业id
     */
    private Long tenantId;
}