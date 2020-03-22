package com.xnpool.setting.domain.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zly
 * @version 1.0
 * @date 2020/3/5 13:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerSetting implements Serializable {
    private static final long serialVersionUID = 5558152303511131448L;
    /**
     * 编号
     */
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

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
     * 协议ID,包含多个
     */
    private String agreementId;

    /**
     * 分组ID,包含多个
     */
    private String groupId;

    /**
     * 角色ID,包含多个
     */
    private String roleId;

    /**
     * 认证,0:待审核,1:通过,2:不通过
     */
    private Integer authentication;

    /**
     * 观察者,0:无权限,1:有权限
     */
    private Integer isWatch;

    /**
     * 备注
     */
    private String remarks;

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

    /**
     * 管理员id
     */
    private Integer managerUserId;

}