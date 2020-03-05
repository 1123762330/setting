package com.xnpool.setting.domain.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author  zly
 * @date  2020/2/5 15:36
 * @version 1.0
 */
@Data
public class GroupSettingExample implements Serializable {
    private static final long serialVersionUID = -4720107876193263696L;
    /**
     * 分组ID
     */
    private Integer id;

    /**
     * 组名
     */
    private String groupName;

    /**
     * 所属IP段,会有多个
     */
    private String ipId;

    /**
     * 所属IP段,会有多个
     */
    private String ipStr;

    /**
     * 起始IP
     */
    private String startIp;

    /**
     * 终止IP
     */
    private String endIp;

    /**
     * 所属厂房,会有多个
     */
    private String factoryId;

    /**
     * 厂房名
     */
    private String factoryName;

    /**
     * 所属矿场
     */
    private Integer mineId;

    /**
     * 矿场名
     */
    private String mineName;

    /**
     * 所属机架
     */
    private String frameId;

    /**
     * 所属机架详细信息
     */
    private String framenameDetailed;
}