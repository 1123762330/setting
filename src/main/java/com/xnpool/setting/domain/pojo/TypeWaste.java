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
 * @since 2020-05-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("z_type_waste")
public class TypeWaste implements Serializable {

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
     * 是否删除 0未删除 1 已删除
     */
    private Integer isDelete;

    /**
     * 所属矿场
     */
    private Integer mineId;

    /**
     * 是否启用,0:启用,1:禁用
     */
    private Integer isOpen;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 企业id
     */
    private Long tenantId;


}
