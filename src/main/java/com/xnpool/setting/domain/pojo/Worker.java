package com.xnpool.setting.domain.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/27 10:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Worker implements Serializable {
    /**
     * 矿机id
     */
    private Integer id;

    /**
     * 矿工名
     */
    private String miner;

    /**
     * 矿机名
     */
    private String workername;

    /**
     * 矿机ip
     */
    private String workerip;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 更新时间
     */
    private Date updatetime;

    /**
     * 是否在线,0:离线,1:在线
     */
    private Integer isonline;

    /**
     * 是否删除,0:否,1;是
     */
    private Integer isdelete;

    private static final long serialVersionUID = 1L;
}