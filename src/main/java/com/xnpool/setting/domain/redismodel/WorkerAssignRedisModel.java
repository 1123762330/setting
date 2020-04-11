package com.xnpool.setting.domain.redismodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zly
 * @version 1.0
 * @date 2020/4/9 14:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerAssignRedisModel implements Serializable {
    private static final long serialVersionUID = -3985819446101485362L;
    /**
     * 编号
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer user_id;

    /**
     * 矿场id
     */
    private Integer mine_id;

    /**
     * 厂房id
     */
    private Integer factory_id;

    /**
     * 矿机架id
     */
    private Integer frame_id;

    /**
     * 创建时间
     */
    private String create_time;

    /**
     * 修改时间
     */
    private String update_time;

    /**
     * 是否删除,0:未,1:已删除
     */
    private Byte is_del;
}