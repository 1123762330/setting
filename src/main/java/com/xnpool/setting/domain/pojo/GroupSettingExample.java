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
    private Integer groupid;

    /**
     * 组名
     */
    private String groupname;

    /**
     * 所属IP段,会有多个
     */
    private String ipid;

    /**
     * 所属IP段,会有多个
     */
    private String ipStr;

    /**
     * 起始IP
     */
    private String startip;

    /**
     * 终止IP
     */
    private String endip;

    /**
     * 所属厂房,会有多个
     */
    private String factoryid;

    /**
     * 厂房名
     */
    private String factoryname;

    /**
     * 所属矿场
     */
    private Integer mineid;

    /**
     * 矿场名
     */
    private String minename;

    /**
     * 所属机架
     */
    private String frameid;

    /**
     * 机架层数
     */
    private Integer number;

    /**
     * 所属机架名称
     */
    private String framename;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 修改时间
     */
    private Date updatetime;
}