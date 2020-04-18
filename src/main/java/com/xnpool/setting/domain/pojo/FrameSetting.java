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
 * @since 2020-04-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FrameSetting implements Serializable {

    private static final long serialVersionUID = 8675364598920429203L;
    /**
     * 机架Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 机架名称
     */
    private String frameName;

    /**
     * 货架编号
     */
    private Integer storageRacksNum;

    /**
     * 排数
     */
    private Integer rowNum;

    /**
     * 机架层数
     */
    private Integer number;

    /**
     * 厂房Id
     */
    private Integer factoryId;

    /**
     * 矿场Id
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

    /**
     * 矿机详细信息
     */
    private String detailed;

    /**
     * 企业id
     */
    private Long tenantId;


}
