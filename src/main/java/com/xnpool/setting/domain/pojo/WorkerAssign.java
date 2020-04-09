package com.xnpool.setting.domain.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zly
 * @version 1.0
 * @date 2020/4/9 14:34
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

    /**
     * 是否删除,0:未,1:已删除
     */
    private Byte isDel;

    private static final long serialVersionUID = 1L;
}