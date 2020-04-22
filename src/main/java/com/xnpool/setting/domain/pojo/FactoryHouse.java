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
public class FactoryHouse implements Serializable {


    private static final long serialVersionUID = 4898864496390972670L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 厂房编号
     */
    private Integer factoryNum;

    /**
     * 厂房名称
     */
    private String factoryName;

    /**
     * 备注
     */
    private String description;

    /**
     * 所属矿场
     */
    private Integer mineId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除,0:否,1:是
     */
    private Integer isDelete;


}
