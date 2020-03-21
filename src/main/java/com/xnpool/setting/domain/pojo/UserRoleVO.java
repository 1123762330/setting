package com.xnpool.setting.domain.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zly
 * @version 1.0
 * @date 2020/3/6 15:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleVO implements Serializable {
    private static final long serialVersionUID = -4872978249967353439L;
    private String userName;
    private Integer userId;
    private String roleName;
    private Integer roleId;
}
