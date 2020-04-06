package com.xnpool.setting.domain.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zly
 * @version 1.0
 * @date 2020/4/6 15:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeeSetting implements Serializable {
    private static final long serialVersionUID = 7192129877640077133L;
    private Integer id;

    /**
     * 矿场id
     */
    private Integer mineId;

    /**
     * 费用单价
     */
    private Double feePrice;

    /**
     * 费用名称
     */
    private String feeName;

    /**
     * 是否删除,0:否,1:是
     */
    private Integer isDelete;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

}