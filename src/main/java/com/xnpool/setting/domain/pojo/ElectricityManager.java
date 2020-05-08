package com.xnpool.setting.domain.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author zly
 * @since 2020-05-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_electricity_manager")
public class ElectricityManager implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long customerId;

    private Long userId;

    /**
     * 计费方式
     */
    private Integer type;

    private Integer billType;

    private Integer factoryId;

    /**
     * 电表id
     */
    private Integer meterId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long tenantId;

    private Long mineId;

    private Integer isDelete;


}
