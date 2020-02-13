package com.xnpool.setting.domain.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author  zly
 * @date  2020/2/4 20:04
 * @version 1.0
 */
@Data
public class FrameSettingExample implements Serializable {
    private static final long serialVersionUID = 4129381920667648768L;
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
     * 厂房名称
     */
    private String factoryname;

    /**
     * 矿场名称
     */
    private String minename;

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
}