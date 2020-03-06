package com.xnpool.setting.domain.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author  zly
 * @date  2020/3/6 14:57
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerAssign implements Serializable {
    /**
    * 编号
    */
    private Integer id;

    /**
    * 用户id
    */
    private Integer userId;

    /**
    * 矿场id
    */
    private Integer mineId;

    /**
    * 厂房id
    */
    private Integer factoryId;

    /**
    * 矿机架id
    */
    private Integer frameId;

    /**
    * 创建时间
    */
    private Date createTime;

    /**
    * 修改时间
    */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}