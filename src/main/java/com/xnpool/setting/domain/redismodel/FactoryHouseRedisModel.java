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
 * @date 2020/3/5 12:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FactoryHouseRedisModel implements Serializable {
    private static final long serialVersionUID = -8505889650023365990L;
    /**
     * 厂房编号
     */
    private Integer id;

    /**
     * 厂房名称
     */
    private String factory_name;

    /**
     * 备注
     */
    private String description;

    /**
     * 所属矿场
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

}