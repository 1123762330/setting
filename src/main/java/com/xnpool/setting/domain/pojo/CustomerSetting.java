package com.xnpool.setting.domain.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/11 10:56
 */
@Data
public class CustomerSetting implements Serializable {
    /**
     * 编号
     */
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userid;

    /**
     * 协议ID,包含多个
     */
    private String agreementid;

    /**
     * 分组ID,也就是菜单栏目ID,包含多个
     */
    private String menuid;

    /**
     * 角色ID,包含多个
     */
    private String roleid;

    /**
     * 认证,0:默认,1:通过,-1是不通过,2是申请
     */
    private Integer authentication;

    /**
     * 观察者,0:无权限,1:有权限
     */
    private Integer iswatch;

    /**
     * 是否删除,0:否,1:是
     */
    private Integer isdelete;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 修改时间
     */
    private Date updatetime;

    /**
     * 管理员id
     */
    private Integer managerUserId;

    private static final long serialVersionUID = 1L;
}