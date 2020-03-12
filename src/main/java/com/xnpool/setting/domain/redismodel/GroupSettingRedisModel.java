package com.xnpool.setting.domain.redismodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zly
 * @version 1.0
 * @date 2020/3/5 19:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupSettingRedisModel implements Serializable {
    private static final long serialVersionUID = 4627124956550680958L;
    /**
     * 分组ID
     */
    private Integer id;

    /**
     * 组名
     */
    private String group_name;

    /**
     * 所属IP段,会有多个
     */
    private String ip_id;

    /**
     * 所属厂房,会有多个
     */
    private String factory_id;

    /**
     * 所属矿场
     */
    private Integer mine_id;

    /**
     * 所属机架
     */
    private String frame_id;

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

}