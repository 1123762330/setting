package com.xnpool.setting.domain.model;

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
     * 客户名称
     */
    private String userName;

    /**
     * 客户类型
     */
    private String cusType;

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
     * 用户自己的手机号
     */
    private String mobile;

    /**
     * 协议ID,包含多个
     */
    private String agreementId;
    /**
     * 协议名称,包含多个
     */
    private String agreementName;

    /**
     * 分组ID,包含多个
     */
    private String groupId;

    /**
     * 分组名称,包含多个
     */
    private String groupName;

    /**
     * 角色ID,包含多个
     */
    private String roleName;

    /**
     * 认证,0:待审核,1:通过,2:不通过
     */
    private Integer authentication;

    /**
     * 观察者,0:无权限,1:有权限
     */
    private Integer iswatch;

    /**
     * 管理员id
     */
    private Integer managerUserId;
}