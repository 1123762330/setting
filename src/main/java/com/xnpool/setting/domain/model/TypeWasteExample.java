package com.xnpool.setting.domain.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2020-05-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("z_type_waste")
public class TypeWasteExample implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 型号功耗id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 型号
     */
    private String type;

    /**
     * 功耗
     */
    private Integer waste;

    /**
     * 所属矿场
     */
    private Integer mineId;

    /**
     * 所属矿场
     */
    private String mineName;

    /**
     * 是否启用,0:启用,1:禁用
     */
    private Integer isOpen;



}
