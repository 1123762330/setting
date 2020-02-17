package com.xnpool.setting.domain.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/11 9:56
 */
@Data
public class GroupSetting implements Serializable {
    private static final long serialVersionUID = 4208021076876960510L;
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
     * 所属厂房,会有多个
     */
    private String factoryid;

    /**
     * 所属矿场
     */
    private Integer mineid;

    /**
     * 所属机架
     */
    private String frameid;

    /**
     * 是否删除,0:否,1:是
     */
    private Integer isdelete;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 修改时间
     */
    private Date updatetime;

}