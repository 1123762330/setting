package com.xnpool.setting.domain.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/17 10:53
 */
@Data
public class FrameSetting implements Serializable {
    private static final long serialVersionUID = 370100620632075204L;
    /**
     * 机架Id
     */
    private Integer id;

    /**
     * 机架名称
     */
    private String framename;

    /**
     * 机架层数
     */
    private Integer number;

    /**
     * 厂房Id
     */
    private Integer factoryid;

    /**
     * 矿场Id
     */
    private Integer mineid;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 修改时间
     */
    private Date updatetime;

    /**
     * 是否删除,0:否,1:是
     */
    private Integer isdelete;

    /**
     * 企业id
     */
    private Long tenantId;

    /**
     * 矿机详细信息
     */
    private String detailed;

}