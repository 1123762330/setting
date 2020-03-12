package com.xnpool.setting.domain.redismodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zly
 * @version 1.0
 * @date 2020/3/12 12:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerDetailedRedisModel implements Serializable {
    private static final long serialVersionUID = -7638620489740163960L;
    /**
     * 编号
     */
    private Integer id;

    /**
     * 矿机ID
     */
    private Integer worker_id;

    /**
     * 用户id
     */
    private Integer user_id;

    /**
     * 矿机品牌id
     */
    private Integer workerbrand_id;

    /**
     * 所属厂房
     */
    private Integer factory_id;

    /**
     * 所属机架
     */
    private Integer frame_id;

    /**
     * 矿机架层数
     */
    private Integer frame_number;

    /**
     * 所属分组
     */
    private Integer group_id;

    /**
     * 矿场id
     */
    private Integer mine_id;

    /**
     * 是否入库,0:未入库,1:已入库
     */
    private Integer is_come_in;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 是否删除,0:否,1:是
     */
    private Integer is_delete;

    /**
     * 创建时间
     */
    private String create_time;

    /**
     * 更新时间
     */
    private String update_time;

    /**
     * 矿机ip
     */
    private String worker_ip;

}