package com.xnpool.setting.domain.redismodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
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
    /**
     * 机架Id
     */
    private Integer id;

    /**
     * 机架名称
     */
    private String frame_name;

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
    private String create_time;

    /**
     * 修改时间
     */
    private String update_time;

    /**
     * 是否删除,0:否,1:是
     */
    private Integer is_delete;

    /**
     * 矿机详细信息
     */
    private String detailed;

}