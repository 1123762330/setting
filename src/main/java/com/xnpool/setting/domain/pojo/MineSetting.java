package com.xnpool.setting.domain.pojo;

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
 * @since 2020-04-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MineSetting implements Serializable {

    private static final long serialVersionUID = 777269658102635794L;
    /**
     * 矿场编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 矿场名称
     */
    private String mineName;

    /**
     * 备注
     */
    private String description;

    /**
     * 该矿场下的机架层数
     */
    private Integer frameNum;

    /**
     * 是否删除，0:否，1:是
     */
    private Integer isDelete;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 生成时间
     */
    private LocalDateTime createTime;


}
