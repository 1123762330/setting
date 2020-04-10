package com.xnpool.setting.domain.redismodel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author zly
 * @since 2020-04-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysUserRedisModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String username;

    private String password;

    private String nickname;

    private String head_img_url;

    private String mobile;

    private Boolean sex;

    private Boolean enabled;

    private String type;

    private String create_time;

    private String update_time;

    private String company;

    private String open_id;

    private Boolean is_del;

    /**
     * 企业id
     */
    private String tenant_id;

    /**
     * 联系人
     */
    private String contact_person;

    /**
     * 地址
     */
    private String address;


}
