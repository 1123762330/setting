package com.xnpool.setting.domain.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zly
 * @version 1.0
 * @date 2020/3/5 19:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupSetting implements Serializable {
    private static final long serialVersionUID = -4867013184861718917L;
    /**
     * 分组ID
     */
    private Integer id;

    /**
     * 组名
     */
    private String groupName;

    /**
     * 所属IP段,会有多个
     */
    private String ipId;

    /**
     * 所属厂房,会有多个
     */
    private String factoryId;

    /**
     * 所属矿场
     */
    private Integer mineId;

    /**
     * 所属机架
     */
    private String frameId;

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