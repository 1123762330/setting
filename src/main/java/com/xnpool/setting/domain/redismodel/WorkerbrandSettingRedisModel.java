package com.xnpool.setting.domain.redismodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zly
 * @version 1.0
 * @date 2020/3/20 15:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerbrandSettingRedisModel implements Serializable {
    private static final long serialVersionUID = -5284458505345753285L;
    /**
     * 编号
     */
    private Integer id;

    /**
     * 矿机品牌名称
     */
    private String brand_name;

    /**
     * 矿机类型
     */
    private String worker_type;

    /**
     * 制造商
     */
    private String business;

    /**
     * 机器尺寸
     */
    private String worker_size;

    /**
     * 功耗
     */
    private String power_waste;

    /**
     * 额定算力
     */
    private String difficulty;

    /**
     * 是否删除,0:否,1:是
     */
    private Integer is_delete;

    /**
     * 创建时间
     */
    private String create_time;

    /**
     * 修改时间
     */
    private String update_time;

    /**
     * 算法id
     */
    private Integer algorithm_id;

}