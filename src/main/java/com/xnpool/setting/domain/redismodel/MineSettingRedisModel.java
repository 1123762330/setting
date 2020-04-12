package com.xnpool.setting.domain.redismodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zly
 * @version 1.0
 * @date 2020/3/5 12:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MineSettingRedisModel implements Serializable {
    private static final long serialVersionUID = 8877349998102275905L;
    /**
     * 矿场编号
     */
    private Integer id;

    /**
     * 矿场名称
     */
    private String mine_name;

    /**
     * 备注
     */
    private String description;

    /**
     * 该矿场下的机架层数
     */
    private Integer frame_num;

    /**
     * 是否删除，0:否，1:是
     */
    private Integer is_delete;

    /**
     * 修改时间
     */
    private String update_time;

    /**
     * 生成时间
     */
    private String create_time;

}