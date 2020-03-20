package com.xnpool.setting.domain.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zly
 * @version 1.0
 * @date 2020/3/20 15:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerbrandSetting implements Serializable {
    private static final long serialVersionUID = -5284458505345753285L;
    /**
     * 编号
     */
    private Integer id;

    /**
     * 矿机品牌名称
     */
    private String brandName;

    /**
     * 矿机类型
     */
    private String workerType;

    /**
     * 制造商
     */
    private String business;

    /**
     * 机器尺寸
     */
    private String workerSize;

    /**
     * 功耗
     */
    private String powerWaste;

    /**
     * 额定算力
     */
    private String difficulty;

    /**
     * 是否删除,0:否,1:是
     */
    private Integer isDelete;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 算法id
     */
    private Integer algorithmId;

}