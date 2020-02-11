package com.xnpool.setting.domain.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/6 13:21
 */
@Data
public class IpSetting implements Serializable {
    /**
     * 编号
     */
    private Integer id;

    /**
     * 起始IP
     */
    private String startip;

    /**
     * 结束IP
     */
    private String endip;

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

    private static final long serialVersionUID = 1L;
}