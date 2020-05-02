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
 * @since 2020-04-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class IpSetting implements Serializable {


    private static final long serialVersionUID = -5242883243323913752L;
    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 起始IP
     */
    private String startIp;

    /**
     * 结束IP
     */
    private String endIp;

    /**
     * 矿场id
     */
    private Integer mineId;

    /**
     * 厂房编号
     */
    private Integer factoryNum;

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

    /**
     * 是否去整,1:是,默认:0
     */
    private Boolean isToInt;


}
