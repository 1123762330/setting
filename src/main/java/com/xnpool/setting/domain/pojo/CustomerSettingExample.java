package com.xnpool.setting.domain.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zly
 * @version 1.0
 * @date 2020/2/10 10:51
 */
@Data
public class CustomerSettingExample implements Serializable {
    private static final long serialVersionUID = 2212713709376491377L;
    /**
     * 编号
     */
    private Integer id;


    /**
     * 用户ID
     */
    private Integer userid;

    /**
     * 客户名称
     */
    private String userName;

    /**
     * 客户类型
     */
    private String userType;

    /**
     * 联系人
     */
    private String contactPerson;

    /**
     * 联系人电话
     */
    private String contactPersonPhone;

    /**
     * 地址
     */
    private String address;

    /**
     * 协议ID,包含多个
     */
    private String agreementid;
    /**
     * 协议名称,包含多个
     */
    private String agreementName;

    /**
     * 分组ID,也就是菜单栏目ID,包含多个
     */
    private String menuid;

    /**
     * 分组名称,也就是菜单栏目,包含多个
     */
    private String menuName;

    /**
     * 角色ID,包含多个
     */
    private String roleid;

    /**
     * 角色ID,包含多个
     */
    private String roleName;

    /**
     * 认证,0:不通过,1:通过
     */
    private Integer authentication;

    /**
     * 观察者,0:无权限,1:有权限
     */
    private Integer iswatch;
}