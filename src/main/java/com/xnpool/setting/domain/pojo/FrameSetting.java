package com.xnpool.setting.domain.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zly
 * @version 1.0
 * @date 2020/3/5 13:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FrameSetting implements Serializable {
    private static final long serialVersionUID = -8883990864667195110L;
    /**
     * 机架Id
     */
    private Integer id;

    /**
     * 机架名称
     */
    private String frameName;

    /**
     * 机架层数
     */
    private Integer number;

    /**
     * 厂房Id
     */
    private Integer factoryId;

    /**
     * 矿场Id
     */
    private Integer mineId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 是否删除,0:否,1:是
     */
    private Integer isDelete;

    /**
     * 矿机详细信息
     */
    private String detailed;

}