package com.xnpool.setting.domain.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zly
 * @version 1.0
 * @date 2020/3/5 13:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IpSetting implements Serializable {
    /**
     * 编号
     */
    private Integer id;

    /**
     * 起始IP
     */
    private String startIp;

    /**
     * 结束IP
     */
    private String endIp;

    /**
     * 矿场id
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

    private static final long serialVersionUID = 1L;
}