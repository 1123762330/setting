package com.xnpool.setting.domain.redismodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author zly
 * @version 1.0
 * @date 2020/3/5 13:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FrameSettingRedisModel implements Serializable {
    private static final long serialVersionUID = 2235329115946148787L;
    private Integer id;

    /**
     * 机架名称
     */
    private String frame_name;

    /**
     * 货架编号
     */
    private Integer storage_racks_num;

    /**
     * 排数
     */
    private Integer row_num;

    /**
     * 机架层数
     */
    private Integer number;

    /**
     * 厂房Id
     */
    private Integer factory_id;

    /**
     * 矿场Id
     */
    private Integer mine_id;

    /**
     * 创建时间
     */
    private LocalDateTime create_time;

    /**
     * 修改时间
     */
    private LocalDateTime update_time;

    /**
     * 是否删除,0:否,1:是
     */
    private Integer is_delete;

    /**
     * 矿机详细信息
     */
    private String detailed;

}