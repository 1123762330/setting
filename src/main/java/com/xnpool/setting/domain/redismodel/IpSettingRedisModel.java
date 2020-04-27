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
 * @date 2020/3/5 13:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IpSettingRedisModel implements Serializable {
    private static final long serialVersionUID = -3019941204883986484L;
    /**
     * 编号
     */
    private Integer id;

    /**
     * 起始IP
     */
    private String start_ip;

    /**
     * 结束IP
     */
    private String end_ip;

    /**
     * 矿场id
     */
    private Integer mine_id;

    /**
     * 是否去整
     */
    private Boolean is_to_int;

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